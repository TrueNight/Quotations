package xyz.truenight.quotations.binding

import android.databinding.BindingAdapter
import android.support.v7.widget.Toolbar
import android.view.View
import xyz.truenight.quotations.util.AppUtil

/**
 * Created by true
 * date: 17/09/2017
 * time: 14:26
 *
 * Copyright © Mikhail Frolov
 */

@BindingAdapter("supportActionBar")
fun supportActionBar(toolbar: Toolbar, enabled: Boolean) {
    if (enabled) {
        AppUtil.getAppCompatActivity(toolbar.context).setSupportActionBar(toolbar)
    }
}

@BindingAdapter("navClick")
fun navClick(toolbar: Toolbar, listener: View.OnClickListener) {
    toolbar.setNavigationOnClickListener(listener)
}