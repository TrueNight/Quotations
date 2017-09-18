package xyz.truenight.quotations.util

import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList.Callback
import xyz.truenight.latte.Latte
import xyz.truenight.quotations.model.Diffable
import xyz.truenight.utils.Utils

object DiffCallback {

    private val CALLBACK = object : Callback<Any> {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return isIdEquals(oldItem, newItem) || same(oldItem, newItem) || Utils.equal(oldItem, newItem) || Latte.equal(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return Utils.equal(oldItem, newItem) || Latte.equal(oldItem, newItem)
        }
    }

    private fun same(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is Diffable && newItem is Diffable) {
            oldItem.same(newItem)
        } else false
    }

    private fun isIdEquals(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is Identifieble && newItem is Identifieble) {
            Utils.equal(oldItem.id, newItem.id)
        } else false
    }

    fun <T> get(): Callback<T> {
        @Suppress("UNCHECKED_CAST")
        return CALLBACK as Callback<T>
    }

}