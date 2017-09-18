package xyz.truenight.quotations.viewmodel

import android.databinding.ObservableBoolean

import xyz.truenight.quotations.model.Quotation
import xyz.truenight.quotations.util.RxObservableBoolean
import xyz.truenight.quotations.util.RxPublisher
import xyz.truenight.quotations.util.Storage
import xyz.truenight.utils.Utils

/**
 * Created by true
 * date: 18/09/2017
 * time: 00:13
 *
 *
 * Copyright © Mikhail Frolov
 */

class ToolViewModel(private val type: Quotation.Type) {

    val selected: ObservableBoolean
        get() = RxObservableBoolean(Storage.observe(type.key()))

    val resId: Int
        get() = type.resId

    fun click() {
        if (Utils.safe(Storage.get<Boolean>(type.key()))) {
            unselect()
        } else {
            select()
        }
    }

    fun select() {
        Storage.put(type.key(), true)
        RxPublisher.post(Quotation.select, type)
    }

    fun unselect() {
        Storage.put(type.key(), false)
        RxPublisher.post(Quotation.unselect, type)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as ToolViewModel?

        return type === that!!.type
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }

    companion object {

        fun convert(values: Array<Quotation.Type>): List<ToolViewModel> {
            return values.map { ToolViewModel(it) }
        }
    }
}
