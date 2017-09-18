package xyz.truenight.quotations.util

import android.databinding.Observable
import android.databinding.ObservableInt
import com.jakewharton.rxrelay.BehaviorRelay
import rx.Subscription
import rx.subjects.BehaviorSubject
import xyz.truenight.utils.Utils
import java.util.*

class RxObservableInt : ObservableInt {

    private val count = HashSet<Observable.OnPropertyChangedCallback>()

    private var subscription: Subscription? = null

    private var relay: rx.Observable<Int>? = null

    constructor(relay: rx.Observable<Int>) : super() {
        this.relay = relay
    }

    constructor(value: Int, relay: rx.Observable<Int>) : super(value) {
        this.relay = relay
    }

    constructor(relay: BehaviorSubject<Int>) : super(Utils.safe(relay.value)) {
        this.relay = relay
    }

    constructor(relay: BehaviorRelay<Int>) : super(Utils.safe(relay.value)) {
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