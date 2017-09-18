package xyz.truenight.quotations.util

import android.support.v7.util.DiffUtil

import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList
import rx.Emitter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by true
 * date: 17/09/2017
 * time: 18:09
 *
 *
 * Copyright © Mikhail Frolov
 */

class AcceptObservableList<T> : DiffObservableList<T>, Action1<List<T>> {

    constructor(callback: DiffObservableList.Callback<T>) : super(callback)

    constructor(callback: DiffObservableList.Callback<T>, detectMoves: Boolean) : super(callback, detectMoves)

    override fun call(ts: List<T>) {
        update(ts)
    }

    override fun update(newItems: List<T>?) {
        super.update(newItems ?: Collections.emptyList())
    }

    /**
     * Returns transformer to calculate diff in computation scheduler
     * Use [.consumer] to apply computation result
     */
    fun transformer(): Observable.Transformer<in List<T>?, Calculated<T>> {
        return Observable.Transformer { observable ->
            observable
                    .map { ts -> ts ?: Collections.emptyList() }
                    .flatMap { ts ->
                        Observable.create(Action1<Emitter<Calculated<T>>> { emitter ->
                            emitter.onNext(Calculated(ts, calculateDiff(ts)))
                            emitter.onCompleted()
                        }, Emitter.BackpressureMode.NONE)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                    }
        }
    }

    /**
     * Apply calculated diff obtained by using [.transformer]
     */
    fun consumer(): Action1<Calculated<T>> {
        return Action1 { calculated -> update(calculated.list, calculated.diffResult) }
    }

    class Calculated<out T> constructor(val list: List<T>, val diffResult: DiffUtil.DiffResult)
}
