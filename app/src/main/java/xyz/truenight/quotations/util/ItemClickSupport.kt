package xyz.truenight.quotations.util

import android.support.v7.widget.RecyclerView
import android.view.View

import xyz.truenight.quotations.R

class ItemClickSupport private constructor(private val mRecyclerView: RecyclerView) {
    private var mOnlyClickable: Boolean = false
    private var mOnItemClickListener: OnItemClick? = null
    private var mOnItemLongClickListener: OnItemLongClick? = null
    private val mOnClickListener = View.OnClickListener { v ->
        if (mOnItemClickListener != null) {
            val holder = mRecyclerView.getChildViewHolder(v)
            mOnItemClickListener!!.onItemClicked(mRecyclerView, v, holder.adapterPosition)
        }
    }
    private val mOnLongClickListener = View.OnLongClickListener { v ->
        if (mOnItemLongClickListener != null) {
            val holder = mRecyclerView.getChildViewHolder(v)
            return@OnLongClickListener mOnItemLongClickListener!!.onItemLongClicked(mRecyclerView, v, holder.adapterPosition)
        }
        false
    }
    private val mAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            if (mOnItemClickListener != null && (!mOnlyClickable || view.isClickable)) {
                view.setOnClickListener(mOnClickListener)
            } else {
                view.setOnClickListener(null)
            }
            if (mOnItemLongClickListener != null && (!mOnlyClickable || view.isClickable)) {
                view.setOnLongClickListener(mOnLongClickListener)
            } else {
                view.setOnLongClickListener(null)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {

        }
    }

    init {
        mRecyclerView.setTag(R.id.item_click_support, this)
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener)
    }

    fun setOnlyClickable(onlyClickable: Boolean): ItemClickSupport {
        if (mOnlyClickable != onlyClickable) {
            this.mOnlyClickable = onlyClickable
            refreshAllChildren()
        }
        return this
    }

    fun setOnItemClickListener(listener: OnItemClick?): ItemClickSupport {
        if (mOnItemClickListener == null && listener != null) {
            mOnItemClickListener = listener
            refreshAllChildren()
        } else if (mOnItemClickListener != null && listener == null) {
            mOnItemClickListener = null
            refreshAllChildren()
        } else {
            mOnItemClickListener = listener
        }
        return this
    }

    fun setOnItemLongClickListener(listener: OnItemLongClick?): ItemClickSupport {
        if (mOnItemLongClickListener == null && listener != null) {
            mOnItemLongClickListener = listener
            refreshAllChildren()
        } else if (mOnItemLongClickListener != null && listener == null) {
            mOnItemLongClickListener = null
            refreshAllChildren()
        } else {
            mOnItemLongClickListener = listener
        }
        return this
    }

    private fun refreshAllChildren() {
        for (i in 0..mRecyclerView.childCount - 1) {
            val view = mRecyclerView.getChildAt(i)
            mAttachListener.onChildViewAttachedToWindow(view)
        }
    }

    private fun detach(view: RecyclerView) {
        mOnItemClickListener = null
        mOnItemLongClickListener = null
        refreshAllChildren()
        view.removeOnChildAttachStateChangeListener(mAttachListener)
        view.setTag(R.id.item_click_support, null)
    }

    interface OnItemClick {
        fun onItemClicked(recyclerView: RecyclerView, itemView: View, position: Int)
    }

    interface OnItemLongClick {
        fun onItemLongClicked(recyclerView: RecyclerView, itemView: View, position: Int): Boolean
    }

    companion object {

        fun addTo(view: RecyclerView): ItemClickSupport {
            var support: ItemClickSupport? = view.getTag(R.id.item_click_support) as ItemClickSupport
            if (support == null) {
                support = ItemClickSupport(view)
            }
            return support
        }

        fun removeFrom(view: RecyclerView): ItemClickSupport? {
            val support = view.getTag(R.id.item_click_support) as ItemClickSupport
            support?.detach(view)
            return support
        }
    }
}