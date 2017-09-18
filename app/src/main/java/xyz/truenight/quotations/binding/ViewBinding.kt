package xyz.truenight.quotations.binding

import android.databinding.BindingAdapter
import android.support.v4.view.ViewCompat
import android.view.View

/**
 * Created by true
 * date: 06/09/2017
 * time: 21:44
 *
 *
 * Copyright © Mikhail Frolov
 */

@BindingAdapter(value = *arrayOf("visible", "hideType"), requireAll = false)
fun visible(view: View, visible: Boolean, hideType: Int) {
    var type = hideType
    if (type == 0) {
        type = View.GONE
    }
    view.visibility = if (visible) View.VISIBLE else type
}

fun visible(view: View, visible: Boolean) {
    visible(view, visible, 0)
}

@BindingAdapter("elevation")
fun elevation(view: View, elevation: Float) {
    ViewCompat.setElevation(view, elevation)
}

@BindingAdapter("onClick")
fun onClick(view: View, listener: View.OnClickListener) {
    view.setOnClickListener(listener)
}

@BindingAdapter("android:onClick")
fun androidOnClick(view: View, listener: View.OnClickListener) {
    view.setOnClickListener(listener)
}
