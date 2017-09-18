package xyz.truenight.quotations.activity

import android.arch.lifecycle.*
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import xyz.truenight.quotations.binding.ViewModelBinding
import xyz.truenight.quotations.util.BindingUtil
import xyz.truenight.quotations.viewmodel.LifecycleTrackingViewModel
import xyz.truenight.utils.Utils
import java.util.*

/**
 * Created by true
 * date: 16/09/2017
 * time: 23:46
 *
 * Copyright © Mikhail Frolov
 */

abstract class BindingLifecycleActivity<out B : ViewDataBinding> : AppCompatActivity(), LifecycleOwner {

    private val mRegistry = LifecycleRegistry(this)

    private val map = HashMap<String, ViewModel>()

    private var mBinding: B? = null

    override fun getLifecycle(): LifecycleRegistry {
        return this.mRegistry
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mBinding == null) {
            mBinding = DataBindingUtil.setContentView(this, bindingLayoutRes)
        }

        val bindings = viewModelBindings

        if (Utils.isNotEmpty(bindings)) {
            for (holder in bindings) {
                val provider: ViewModelProvider = if (holder.factory == null) {
                    if (holder.activity == null) {
                        ViewModelProviders.of(holder.fragment!!)
                    } else {
                        ViewModelProviders.of(holder.activity)
                    }
                } else {
                    if (holder.activity == null) {
                        ViewModelProviders.of(holder.fragment!!, holder.factory!!)
                    } else {
                        ViewModelProviders.of(holder.activity, holder.factory!!)
                    }
                }
                val viewModel = provider.get(holder.vmClass)
                map.put(viewModel.javaClass.canonicalName, viewModel)
                (viewModel as? LifecycleTrackingViewModel)?.registerLifecycle(this)

                if (!mBinding!!.setVariable(holder.variableId, viewModel)) {
                    BindingUtil.throwMissingVariable(mBinding!!, holder.variableId, bindingLayoutRes)
                }
            }
        }
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