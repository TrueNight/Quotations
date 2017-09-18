package xyz.truenight.quotations.util

import android.support.v7.widget.RecyclerView
import android.view.View

interface OnItemSwipedListener {
    fun onItemSwiped(view: RecyclerView, itemView: View, position: Int)
}