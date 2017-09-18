package xyz.truenight.quotations.model

import com.google.gson.annotations.SerializedName
import xyz.truenight.quotations.R
import xyz.truenight.quotations.util.Storage
import xyz.truenight.utils.Utils

/**
 * Created by true
 * date: 17/09/2017
 * time: 16:10
 *
 * Copyright © Mikhail Frolov
 */
data class Quotation(@SerializedName("s") val type: Type,
                     @SerializedName("b") val bid: Double,
                     @SerializedName("a") val ask: Double,
                     @SerializedName("spr") val spread: Double) : Diffable {


    enum class Type(val resId: Int) {
        EURUSD(R.string.eur_usd),
        EURGBP(R.string.eur_gbp),
        USDJPY(R.string.usd_jpy),
        GBPUSD(R.string.gbp_usd),
        USDCHF(R.string.usd_chf),
        USDCAD(R.string.usd_cad),
        AUDUSD(R.string.aud_usd),
        EURJPY(R.string.eur_jpy),
        EURCHF(R.string.eur_chf);

        fun key(): String {
            return "selected:$this"
        }
    }

    enum class SortField(val resId: Int) {
        TYPE(R.string.name),
        ASK(R.string.ask),
        BID(R.string.bid),
        SPREAD(R.string.spread)
    }

    override fun same(item: Any?): Boolean {
        if (this === item) return true
        if (item == null || javaClass != item.javaClass) return false

        val that = item as Quotation?

        return type == that!!.type
    }

    companion object {
        val select: String = "Selected"
        val unselect: String = "Unelected"
        val list: String = "List"
        val sortField: String = "SortField"
        val sortTypeAsc: String = "SortType"

        fun selectedTools(): List<Type> {
            return Type.values().filter { Utils.safe(Storage.get<Boolean>(it.key())) }
        }

        fun sortComparator(): java.util.Comparator<Quotation> {
            val type = Utils.safe(Storage.get<Boolean>(Quotation.sortTypeAsc))
            return when (Storage.get(Quotation.sortField, Quotation.SortField.TYPE)) {
                Quotation.SortField.ASK -> Comparator { l, r -> compareTo(l.ask, r.ask, type) }
                Quotation.SortField.BID -> Comparator { l, r -> compareTo(l.bid, r.bid, type) }
                Quotation.SortField.SPREAD -> Comparator { l, r -> compareTo(l.spread, r.spread, type) }
                else -> Comparator { l, r -> compareTo(l.type.name, r.type.name, type) }
            }
        }

        private fun <T : Comparable<T>> compareTo(left: T, right: T, asc: Boolean): Int {
            return if (asc) left.compareTo(right) else right.compareTo(left)
        }
    }
}

//    {"s":"EURGBP","b":"0.79575","bf":2,"a":"0.79593","af":0,"spr":"1.8"}