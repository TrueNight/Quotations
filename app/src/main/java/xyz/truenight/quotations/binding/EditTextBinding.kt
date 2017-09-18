package xyz.truenight.quotations.binding

import android.databinding.BindingAdapter
import android.view.View
import android.widget.EditText
import android.widget.TextView
import xyz.truenight.quotations.util.VoidSelectionActionModeCallback


/**
 * Created by true
 * date: 17/09/2017
 * time: 23:34
 *
 * Copyright © Mikhail Frolov
 */

@BindingAdapter("onClick")
fun onClick(view: EditText, listener: View.OnClickListener?) {
    view.setOnClickListener(listener)
    textOperationsEnabled(view, listener == null)
}

@BindingAdapter("android:onClick")
fun androidOnClick(view: EditText, listener: View.OnClickListener?) {
    view.setOnClickListener(listener)
    textOperationsEnabled(view, listener == null)
}

@BindingAdapter("textOperationsEnabled")
fun textOperationsEnabled(view: TextView, enabled: Boolean) {
    if (enabled) {
        view.isFocusable = true
        view.isLongClickable = true
        view.customSelectionActionModeCallback = null
    } else {
        view.isFocusable = false
        view.isLongClickable = false
        view.customSelectionActionModeCallback = VoidSelectionActionModeCallback()
    }
}