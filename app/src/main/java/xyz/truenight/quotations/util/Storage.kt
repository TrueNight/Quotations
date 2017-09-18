package xyz.truenight.quotations.util

import android.content.Context
import com.orhanobut.hawk.HawkBuilder
import rx.Observable

/**
 * Created by true
 * date: 18/09/2017
 * time: 00:20
 *
 * Copyright © Mikhail Frolov
 */
object Storage {

    var hawk: RxHawkFacade? = null

    fun init(context: Context) {
        hawk = RxHawkFacade(HawkBuilder(context))
    }


    fun <T> put(key: String, value: T): Boolean {
        return hawk!!.put(key, value)
    }

    fun <T> get(key: String): T? {
        return hawk!!.get<T>(key)
    }

    fun <T> observe(key: String): Observable<T> {
        return hawk!!.observe(key)
    }

    fun <T> observeChange(key: String): Observable<T> {
        return hawk!!.observeChange(key)
    }

    fun <T> get(key: String, def: T): T {
        return hawk!!.get(key, def)
    }

    fun <T> observe(key: String, defVal: T): Observable<T> {
        return hawk!!.observe(key, defVal)
    }

    fun delete(key: String): Boolean {
        return hawk!!.delete(key)
    }

    fun notify(key: String) {
        hawk!!.notify(key)
    }

}