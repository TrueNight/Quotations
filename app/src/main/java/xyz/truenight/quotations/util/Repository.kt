package xyz.truenight.quotations.util

import android.util.Log
import com.appunite.websocket.rx.RxMoreObservables
import com.appunite.websocket.rx.messages.RxEvent
import com.appunite.websocket.rx.messages.RxEventConnected
import com.appunite.websocket.rx.messages.RxEventStringMessage
import okhttp3.WebSocket
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.internal.util.SubscriptionList
import rx.schedulers.Schedulers
import xyz.truenight.quotations.model.Payload
import xyz.truenight.quotations.model.Quotation
import xyz.truenight.quotations.model.Ticks
import xyz.truenight.quotations.model.Tools
import xyz.truenight.quotations.viewmodel.QuotationViewModel
import xyz.truenight.utils.Utils
import xyz.truenight.utils.interfaces.Source
import java.util.*

/**
 * Created by true
 * date: 18/09/2017
 * time: 03:14
 *
 * Copyright © Mikhail Frolov
 */
object Repository {

    private val TAG = "Repository"

    /**
     * Put quotations to cache
     */
    private fun putQuotations(items: List<Quotation>?) {
        if (Utils.isNotEmpty(items)) {
            val get = Storage.get<List<Quotation>>(Quotation.list)
            val list = ArrayList(get ?: Collections.emptyList())
            items!!.forEach { item ->
                cleanOld(list, item)
                list.add(item)
            }
            Storage.put(Quotation.list, list)
        }
    }

    /**
     * Remove old quotation to replace with new
     */
    private fun cleanOld(list: MutableList<Quotation>, item: Quotation) {
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            val quotation = iterator.next()

            if (quotation.type == item.type) {
                iterator.remove()
            }
        }
    }

    /**
     * Put quotations to cache from subscribe/unsubscribe response and for every tick
     */
    fun cacheQuotations(share: Observable<RxEvent>): Subscription? = SubscriptionList(
            share.compose(SocketUtil.toModel(Ticks::class.java))
                    .map { (items1) -> items1 }
                    .subscribe({ items -> putQuotations(items) }, { t -> Log.e(TAG, "", t) }),
            share.compose(RxUtil.filterAndMap(RxEventStringMessage::class.java))
                    .filter { event -> event.message().contains("\"subscribed_count\"") }
                    .compose(SocketUtil.toModel(Tools::class.java))
                    .map { tools -> tools.ticks?.items }
                    .subscribe({ items -> putQuotations(items) }, { t -> Log.e(TAG, "", t) })
    )

    /**
     * List of cached quotations
     */
    fun quotationList(): Observable<List<QuotationViewModel>> = Observable.combineLatest(
            Storage.observe<List<Quotation>>(Quotation.list, Collections.emptyList()),
            Storage.observe<Quotation.SortField>(Quotation.sortField),
            Storage.observe<Boolean>(Quotation.sortTypeAsc),
            { t1, t2, t3 -> t1 })
            .map { items -> Utils.filtered(items, { item -> Quotation.selectedTools().contains(item.type) }) }
            .map { items -> ArrayList(items ?: Collections.emptyList()) }
            .map { items -> Utils.sort(items, Quotation.sortComparator()) }
            .map { items -> QuotationViewModel.convert(items) }
            .observeOn(AndroidSchedulers.mainThread())

    /**
     * Subscribe on checking tool
     */
    fun toolChecked(source: Source<WebSocket?>): Observable<Any> =
            RxPublisher.observe<Quotation.Type>(Quotation.select)
                    .doOnNext { Storage.notify(Quotation.list) }
                    .observeOn(Schedulers.io())
                    .flatMap { type ->
                        if (source.get() != null && type != null) {
                            val subscribe = Payload.subscribe(type)
                            Log.d(TAG, subscribe)
                            RxMoreObservables.sendMessage(source.get()!!, subscribe)
                                    .toObservable()
                        } else {
                            Observable.empty<Boolean>()
                        }
                    }

    /**
     * Unsubscribe on unchecking tool
     */
    fun toolUnchecked(source: Source<WebSocket?>): Observable<Any> =
            RxPublisher.observe<Quotation.Type>(Quotation.unselect)
                    .doOnNext { Storage.notify(Quotation.list) }
                    .observeOn(Schedulers.io())
                    .flatMap { type ->
                        if (source.get() != null && type != null) {
                            val unsubscribe = Payload.unsubscribe(type)
                            Log.d(TAG, unsubscribe)
                            RxMoreObservables.sendMessage(source.get()!!, unsubscribe)
                                    .onErrorReturn { th -> false }
                                    .toObservable()
                        } else {
                            Observable.just<Boolean>(false)
                        }
                    }

    /**
     * Send saved subs after connection
     */
    fun <T> initialSubscribe(): Observable.Transformer<T, T> = Observable.Transformer { observable ->
        observable.flatMap { rxEvent ->
            if (rxEvent is RxEventConnected) {
                val types = Quotation.selectedTools()
                if (!types.isEmpty()) {
                    val subscribe = Payload.subscribe(types)
                    Log.d(TAG, subscribe)
                    return@flatMap RxMoreObservables.sendMessage(rxEvent.sender(), subscribe)
                            .map { res -> rxEvent }
                            .onErrorReturn { th -> rxEvent }
                            .toObservable()
                }
            }
            return@flatMap Observable.just(rxEvent)
        }
    }
}