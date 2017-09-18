package xyz.truenight.quotations.util

import android.support.v7.widget.RecyclerView
import android.view.View

interface OnItemClickListener<T> {
    fun onItemClicked(recyclerView: RecyclerView, itemView: View, position: Int, item: T)
}