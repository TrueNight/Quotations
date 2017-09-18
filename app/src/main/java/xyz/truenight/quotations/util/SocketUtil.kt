package xyz.truenight.quotations.util

import android.util.Log
import com.appunite.websocket.rx.messages.RxEvent
import com.appunite.websocket.rx.messages.RxEventStringMessage
import com.google.gson.Gson
import rx.Observable

/**
 * Created by true
 * date: 17/09/2017
 * time: 19:12
 *
 * Copyright © Mikhail Frolov
 */
object SocketUtil {

    /**
     * Convert RxEventStringMessage to model with specified type
     */
    fun <T> toModel(cls: Class<T>): Observable.Transformer<RxEvent, T> {
        return Observable.Transformer { observable ->
            observable
                    .compose(RxUtil.filterAndMap(RxEventStringMessage::class.java))
                    .map({ event ->
                        try {
                            Gson().fromJson(event.message(), cls) as T
                        } catch (e: Exception) {
                            Log.e("SocketUtil", event.message(), e)
                            null
                        }
                    })
        }
    }
}