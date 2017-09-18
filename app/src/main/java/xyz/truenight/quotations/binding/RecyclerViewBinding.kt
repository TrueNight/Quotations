package xyz.truenight.quotations.binding

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import xyz.truenight.quotations.util.*


/**
 * Created by true
 * date: 17/09/2017
 * time: 23:34
 *
 * Copyright © Mikhail Frolov
 */

@BindingAdapter("itemClick")
fun <T> itemClick(view: RecyclerView, listener: OnItemClickListener<T>?) {
    @Suppress("UNCHECKED_CAST")
    ItemClickSupport.addTo(view).setOnItemClickListener(if (listener == null) null else object : ItemClickSupport.OnItemClick {
        override fun onItemClicked(recyclerView: RecyclerView, itemView: View, position: Int) {
            listener.onItemClicked(recyclerView, itemView, position, (recyclerView.adapter as BindingRecyclerViewAdapter<T>).getAdapterItem(position))
        }
    })
}

@BindingAdapter("itemLongClick")
fun <T> itemLongClick(view: RecyclerView, listener: OnItemLongClickListener<T>?) {
    @Suppress("UNCHECKED_CAST")
    ItemClickSupport.addTo(view).setOnItemClickListener(if (listener == null) null else object : ItemClickSupport.OnItemClick {
        override fun onItemClicked(recyclerView: RecyclerView, itemView: View, position: Int) {
            listener.onItemLongClicked(recyclerView, itemView, position, (recyclerView.adapter as BindingRecyclerViewAdapter<T>).getAdapterItem(position))
        }
    })
}

@BindingAdapter("itemTouchHelper")
fun <T> itemTouchHelper(view: RecyclerView, callback: ItemTouchHelper.Callback) {
    val itemTouchHelper = ItemTouchHelper(callback)
    itemTouchHelper.attachToRecyclerView(view)
}

@BindingAdapter("itemTouchHelper")
fun <T> swipeItem(view: RecyclerView, listener: OnItemSwipedListener) {
    val itemTouchHelper = ItemTouchHelper(OnSwipeItemTouchHelperCallback(view, listener))
    itemTouchHelper.attachToRecyclerView(view)
}