package xyz.truenight.quotations.util

import android.databinding.ObservableBoolean
import com.appunite.websocket.rx.messages.RxEvent
import com.appunite.websocket.rx.messages.RxEventConnected
import rx.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by true
 * date: 17/09/2017
 * time: 19:15
 *
 * Copyright © Mikhail Frolov
 */
object RxUtil {
    fun loading(loading: ObservableBoolean): Observable.Transformer<RxEvent, RxEvent> {
        return Observable.Transformer { observable ->
            observable
                    .doOnSubscribe { loading.set(true) }
                    .doOnNext { event ->
                        if (event is RxEventConnected) {
                            loading.set(false)
                        }
                    }
                    .doOnError { loading.set(false) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> filterAndMap(cls: Class<T>): Observable.Transformer<Any, T> {
        return Observable.Transformer { observable ->
            observable
                    .filter { item -> cls.isInstance(item) }
                    .map { item -> item as T }
        }
    }

    fun <T> retryWithDelay(retryCount: Int, delay: Long, timeUnit: TimeUnit): Observable.Transformer<T, T> {
        return Observable.Transformer { observable ->
            observable
                    .retryWhen({ errors -> retryWithDelay(errors, retryCount, delay, timeUnit) })
        }
    }

    private fun retryWithDelay(errors: Observable<out Throwable>, count: Int, delay: Long, timeUnit: TimeUnit): Observable<Long> {
        return errors
                .zipWith(Observable.range(1, count)) { _, i -> i }
                .flatMap { _ -> Observable.timer(delay, timeUnit) }
    }
}