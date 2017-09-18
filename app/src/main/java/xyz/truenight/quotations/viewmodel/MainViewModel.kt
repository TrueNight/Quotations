package xyz.truenight.quotations.viewmodel

import android.databinding.ObservableBoolean
import android.support.design.widget.Snackbar
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.appunite.websocket.rx.RxWebSockets
import com.appunite.websocket.rx.messages.RxEventConn
import me.tatarka.bindingcollectionadapter2.BindingCollectionAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import rx.Subscription
import rx.functions.Action1
import rx.internal.util.SubscriptionList
import xyz.truenight.quotations.BR
import xyz.truenight.quotations.BuildConfig
import xyz.truenight.quotations.R
import xyz.truenight.quotations.dialogfragment.SelectToolsDialogFragment
import xyz.truenight.quotations.model.Quotation
import xyz.truenight.quotations.util.*
import xyz.truenight.utils.Utils
import xyz.truenight.utils.interfaces.Source
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit


/**
 * Created by true
 * date: 16/09/2017
 * time: 23:29
 *
 *
 * Copyright © Mikhail Frolov
 */

class MainViewModel : LifecycleTrackingViewModel() {

    private val TAG = "MainViewModel"

    // quotation list
    val items = AcceptObservableList<QuotationViewModel>(DiffCallback.get())

    // tools list
    val tools = ToolViewModel.convert(Quotation.Type.values())

    val loading = ObservableBoolean()

    val sortAsc = RxObservableBoolean(Utils.safe(Storage.get<Boolean>(Quotation.sortTypeAsc)), Storage.observe(Quotation.sortTypeAsc))

    val sortField = RxObservableInt(Storage.observe(Quotation.sortField, Quotation.SortField.TYPE)
            .map { field -> field.resId })

    val binding: ItemBinding<QuotationViewModel> = ItemBinding.of(BR.item, R.layout.quotation_item)

    val toolsBinding: ItemBinding<ToolViewModel> = ItemBinding.of(BR.item, R.layout.quotation_type_item)

    private var repoSub: Subscription? = null

    private var subscription: Subscription? = null

    private val rxWebSockets = RxWebSockets(OkHttpClient(), Request.Builder()
            .get()
            .url(BuildConfig.HOST)
            .build())

    private var webSocket: WebSocket? = null

    var addButton: WeakReference<View>? = null

    fun addClick(view: View) {
        addButton = WeakReference(view)
        SelectToolsDialogFragment()
                .show(AppUtil.getFragmentActivity(view.context).supportFragmentManager,
                        SelectToolsDialogFragment::class.java.canonicalName)

    }

    fun selectionClosed() {
        val unwrap = Utils.unwrap(addButton)
        if (unwrap != null && Storage.get<Any>("first:time") == null && Quotation.selectedTools().isNotEmpty()) {
            Snackbar.make(unwrap, R.string.remove_items_hint, Snackbar.LENGTH_LONG).show()
            Storage.put("first:time", Object())
        }
    }

    fun sortClick(view: View) {
        val menu = PopupMenu(view.context, view)
        menu.inflate(R.menu.menu_main)
        menu.setOnMenuItemClickListener({ menuItem ->
            Storage.put(Quotation.sortField, when (menuItem.itemId) {
                R.id.action_ask -> Quotation.SortField.ASK
                R.id.action_bid -> Quotation.SortField.BID
                R.id.action_spread -> Quotation.SortField.SPREAD
                else -> Quotation.SortField.TYPE
            })
            true
        })
        menu.show()
    }

    fun sortTypeClick() {
        Storage.put(Quotation.sortTypeAsc, !Utils.safe(Storage.get<Boolean>(Quotation.sortTypeAsc)))
    }

    val swipeListener = object : OnItemSwipedListener {
        override fun onItemSwiped(view: RecyclerView, itemView: View, position: Int) {
            @Suppress("UNCHECKED_CAST")
            val item = (view.adapter as BindingCollectionAdapter<QuotationViewModel>).getAdapterItem(position)

            item.unselect()

            Snackbar.make(itemView, view.context.getString(item.get.type.resId)
                    + view.context.getString(R.string.item_removed_from_list), Snackbar.LENGTH_LONG)
                    .setAction(R.string.cancel, {
                        item.select()
                    }).show()
        }
    }

    override fun onActive() {
        Log.d(TAG, "onActive")

        repoSub = SubscriptionList(
                Repository.toolChecked(Source { webSocket })
                        .subscribe(),
                Repository.toolUnchecked(Source { webSocket })
                        .subscribe(),
                Repository.quotationList()
                        .compose(items.transformer())
                        .subscribe(items.consumer(), Action1 { t -> Log.e(TAG, "", t) })
        )

        val share = rxWebSockets
                .webSocketObservable()
                .compose(RxUtil.loading(loading))
                .doOnNext { rxEvent ->
                    if (rxEvent is RxEventConn) {
                        webSocket = rxEvent.sender()
                    }
                }
                .compose(Repository.initialSubscribe())
                .doOnNext { rxEvent ->
                    Log.d(TAG, rxEvent.toString())
                }
                .compose(RxUtil.retryWithDelay(Int.MAX_VALUE, 10, TimeUnit.SECONDS))
                .share()

        subscription = Repository.cacheQuotations(share)
    }

    override fun onInactive() {
        Log.d(TAG, "onInactive")
        repoSub?.unsubscribe()
        subscription?.unsubscribe()
        webSocket = null
    }
}
