package xyz.truenight.quotations.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import xyz.truenight.quotations.BR

import xyz.truenight.quotations.model.Quotation
import xyz.truenight.quotations.util.RxPublisher
import xyz.truenight.quotations.util.Storage
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by true
 * date: 18/09/2017
 * time: 16:41
 *
 *
 * Copyright © Mikhail Frolov
 */

class QuotationViewModel(private var _get: Quotation) : BaseObservable() {

    var get: Quotation
        @Bindable get() = _get
        set(value) {
            _get = value
            notifyPropertyChanged(BR.get)
        }

    fun select() {
        Storage.put(get.type.key(), true)
        RxPublisher.post(Quotation.select, get.type)
    }

    fun unselect() {
        Storage.put(get.type.key(), false)
        RxPublisher.post(Quotation.unselect, get.type)
    }

    companion object {

        fun convert(values: List<Quotation>): List<QuotationViewModel> {
            return values.map { QuotationViewModel.get(it) }
        }

        private val CACHE = ConcurrentHashMap<Quotation.Type, QuotationViewModel>()

        /**
         * Flyweight for recycler updates
         */
        private fun get(it: Quotation): QuotationViewModel {
            var cached = CACHE[it.type]
            if (cached == null) {
                cached = QuotationViewModel(it)
                CACHE[it.type] = cached
            } else {
                cached.get = it
            }

            return cached
        }
    }
}
