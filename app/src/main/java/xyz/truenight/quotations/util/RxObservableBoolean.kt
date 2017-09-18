package xyz.truenight.quotations.util

import android.databinding.ObservableBoolean
import com.jakewharton.rxrelay.BehaviorRelay
import rx.Observable
import rx.Subscription
import rx.subjects.BehaviorSubject
import xyz.truenight.utils.Utils
import java.util.concurrent.CopyOnWriteArraySet

class RxObservableBoolean : ObservableBoolean {

    private val count = CopyOnWriteArraySet<android.databinding.Observable.OnPropertyChangedCallback>()

    private var subscription: Subscription? = null

    private var relay: Observable<Boolean>? = null

    constructor(relay: Observable<Boolean>) : super() {
        this.relay = relay
    }

    constructor(value: Boolean, relay: Observable<Boolean>) : super(value) {
        this.relay = relay
    }

    constructor(relay: BehaviorSubject<Boolean>) : super(Utils.safe(relay.value)) {
        this.relay = relay
    }

    constructor(relay: BehaviorRelay<Boolean>) : super(Utils.safe(relay.value)) {
        this.relay = relay
    }

    override fun addOnPropertyChangedCallback(callback: android.databinding.Observable.OnPropertyChangedCallback) {
        super.addOnPropertyChangedCallback(callback)
        if (count.isEmpty()) {
            subscription = relay!!.subscribe { value -> set(Utils.safe(value)) }
        }
        count.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: android.databinding.Observable.OnPropertyChangedCallback) {
        super.removeOnPropertyChangedCallback(callback)
        count.remove(callback)
        if (count.isEmpty()) {
            subscription!!.unsubscribe()
        }
    }
}