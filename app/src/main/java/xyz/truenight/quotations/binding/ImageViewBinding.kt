package xyz.truenight.quotations.binding

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView

/**
 * Created by true
 * date: 17/09/2017
 * time: 23:34
 *
 * Copyright © Mikhail Frolov
 */

@BindingAdapter("android:src")
fun src(view: ImageView, image: Drawable?) {
    view.setImageDrawable(image)
}