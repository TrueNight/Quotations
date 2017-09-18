package xyz.truenight.quotations.util

import com.jakewharton.rxrelay.PublishRelay
import rx.Observable
import xyz.truenight.utils.Utils

/**
 * Created by true
 * date: 18/09/2017
 * time: 12:59
 *
 * Copyright © Mikhail Frolov
 */
object RxPublisher {

    private val relay = PublishRelay.create<Change<Any>>().toSerialized()

    @Suppress("UNCHECKED_CAST")
    fun <T> observe(key: String): Observable<T?> {
        return relay.filter { change -> change.same(key) }.map { change -> change.item as T? }
    }


    class Change<T> constructor(private val key: String, val item: T?) {
        fun same(key: String): Boolean {
            return Utils.equal(this.key, key)
        }
    }

    fun post(key: String, t: Any?) {
        relay.call(Change(key, t))
    }
}