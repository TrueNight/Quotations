package xyz.truenight.quotations.binding

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * Created by true
 * date: 16/09/2017
 * time: 23:36
 *
 * Copyright © Mikhail Frolov
 */

class ViewModelBinding {

    val activity: FragmentActivity?
    val fragment: Fragment?
    val vmClass: Class<out ViewModel>
    val variableId: Int
    var factory: ViewModelProvider.Factory? = null
        private set

    constructor(activity: FragmentActivity, variableId: Int, vmClass: Class<out ViewModel>) {
        this.activity = activity
        fragment = null
        this.vmClass = vmClass
        this.variableId = variableId
    }

    constructor(activity: FragmentActivity, variableId: Int, vmClass: Class<out ViewModel>, factory: ViewModelProvider.Factory) {
        this.activity = activity
        fragment = null
        this.vmClass = vmClass
        this.variableId = variableId
        this.factory = factory
    }

    constructor(fragment: Fragment, variableId: Int, vmClass: Class<out ViewModel>) {
        activity = null
        this.fragment = fragment
        this.vmClass = vmClass
        this.variableId = variableId
    }

    constructor(fragment: Fragment, variableId: Int, vmClass: Class<out ViewModel>, factory: ViewModelProvider.Factory) {
        activity = null
        this.fragment = fragment
        this.vmClass = vmClass
        this.variableId = variableId
        this.factory = factory
    }
}