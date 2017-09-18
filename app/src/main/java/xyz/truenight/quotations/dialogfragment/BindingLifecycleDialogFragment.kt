package xyz.truenight.quotations.dialogfragment

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.truenight.quotations.binding.ViewModelBinding
import xyz.truenight.quotations.util.BindingUtil
import xyz.truenight.utils.Utils
import java.util.*

/**
 * Created by true
 * date: 17/09/2017
 * time: 17:34
 *
 *
 * Copyright © Mikhail Frolov
 */

abstract class BindingLifecycleDialogFragment<out B : ViewDataBinding> : DialogFragment(), LifecycleOwner {

    private val mRegistry = LifecycleRegistry(this)

    private var map = HashMap<String, ViewModel>()

    private var mBinding: B? = null

    override fun getLifecycle(): LifecycleRegistry {
        return this.mRegistry
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, bindingLayoutRes, container, false)
        }

        map = BindingUtil.bind(viewModelBindings, mBinding!!, this, bindingLayoutRes)

        return mBinding!!.root
    }

    @get:LayoutRes
    abstract val bindingLayoutRes: Int

    abstract val viewModelBindings: List<ViewModelBinding>

    fun <VM : ViewModel> getViewModel(viewModelClass: Class<VM>): VM? {
        try {
            @Suppress("UNCHECKED_CAST")
            return map[viewModelClass.canonicalName] as VM
        } catch (e: ClassCastException) {
            throw IllegalStateException("Wrong type of VM")
        }
    }

    fun binding(): B? {
        return mBinding
    }

    protected fun list(vararg binding: ViewModelBinding): List<ViewModelBinding> {
        return Utils.add(*binding)
    }
}
