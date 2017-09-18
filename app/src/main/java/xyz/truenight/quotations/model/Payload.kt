package xyz.truenight.quotations.model

import xyz.truenight.utils.Utils

/**
 * Created by true
 * date: 17/09/2017
 * time: 22:43
 *
 * Copyright © Mikhail Frolov
 */
object Payload {

    fun subscribe(vararg types: Quotation.Type): String {
        return "SUBSCRIBE: ${Utils.join(",", types.asList())}"
    }

    fun subscribe(types: List<Quotation.Type>): String {
        return "SUBSCRIBE: ${Utils.join(",", types)}"
    }

    fun unsubscribe(vararg types: Quotation.Type): String {
        return "UNSUBSCRIBE: ${Utils.join(",", types.asList())}"
    }

    fun unsubscribe(types: List<Quotation.Type>): String {
        return "UNSUBSCRIBE: ${Utils.join(",", types)}"
    }
}