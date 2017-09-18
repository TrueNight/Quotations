package xyz.truenight.quotations.util

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.Resources
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import xyz.truenight.quotations.binding.ViewModelBinding
import xyz.truenight.quotations.viewmodel.LifecycleTrackingViewModel
import xyz.truenight.utils.Utils
import java.util.*

object BindingUtil {

    fun bind(bindings: List<ViewModelBinding>, dataBinding: ViewDataBinding, owner: LifecycleOwner, layoutRes: Int): HashMap<String, ViewModel> {
        val map = HashMap<String, ViewModel>()
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
                (viewModel as? LifecycleTrackingViewModel)?.registerLifecycle(owner)

                if (!dataBinding.setVariable(holder.variableId, viewModel)) {
                    BindingUtil.throwMissingVariable(dataBinding, holder.variableId, layoutRes)
                }
            }
        }
        return map
    }

    fun throwMissingVariable(binding: ViewDataBinding, bindingVariable: Int, @LayoutRes layoutRes: Int) {
        val context = binding.root.context
        val resources = context.resources
        val layoutName = resources.getResourceName(layoutRes)
        // Yeah reflection is slow, but this only happens when there is a programmer error.
        var bindingVariableName: String
        try {
            bindingVariableName = getBindingVariableName(context, bindingVariable)
        } catch (e: Resources.NotFoundException) {
            // Fall back to int
            bindingVariableName = "" + bindingVariable
        }

        throw IllegalStateException("Could not bind variable '$bindingVariableName' in layout '$layoutName'")
    }


    @Throws(Resources.NotFoundException::class)
    private fun getBindingVariableName(context: Context, bindingVariable: Int): String {
        try {
            return getBindingVariableByDataBinderMapper(bindingVariable)
        } catch (e1: Exception) {
            try {
                return getBindingVariableByBR(context, bindingVariable)
            } catch (e2: Exception) {
                throw Resources.NotFoundException("" + bindingVariable)
            }

        }

    }

    @Throws(Exception::class)
    private fun getBindingVariableByDataBinderMapper(bindingVariable: Int): String {
        val dataBinderMapper = Class.forName("android.databinding.DataBinderMapper")
        val convertIdMethod = dataBinderMapper.getDeclaredMethod("convertBrIdToString", Int::class.javaPrimitiveType)
        convertIdMethod.isAccessible = true
        val constructor = dataBinderMapper.getDeclaredConstructor()
        constructor.isAccessible = true
        val instance = constructor.newInstance()
        val result = convertIdMethod.invoke(instance, bindingVariable)
        return result as String
    }

    /**
     * Attempt to getInternal the name by using reflection on the generated BR class. Unfortunately, we
     * don't know BR's package name so this may fail if it's not the same as the apps package name.
     */
    @Throws(Exception::class)
    private fun getBindingVariableByBR(context: Context, bindingVariable: Int): String {
        val packageName = context.packageName
        val BRClass = Class.forName(packageName + ".BR")
        val fields = BRClass.fields
        for (field in fields) {
            val value = field.getInt(null)
            if (value == bindingVariable) {
                return field.name
            }
        }
        throw Exception("not found")
    }
}