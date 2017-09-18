package xyz.truenight.quotations.util

import android.support.v7.widget.RecyclerView
import android.view.View

interface OnItemLongClickListener<T> {
    fun onItemLongClicked(recyclerView: RecyclerView, itemView: View, position: Int, item: T): Boolean
}