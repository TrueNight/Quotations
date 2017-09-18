package xyz.truenight.quotations.dialogfragment

import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import xyz.truenight.quotations.BR
import xyz.truenight.quotations.R
import xyz.truenight.quotations.binding.ViewModelBinding
import xyz.truenight.quotations.databinding.FragmentToolsBinding
import xyz.truenight.quotations.viewmodel.MainViewModel

/**
 * Created by true
 * date: 17/09/2017
 * time: 22:14
 *
 *
 * Copyright © Mikhail Frolov
 */

class SelectToolsDialogFragment : BindingLifecycleDialogFragment<FragmentToolsBinding>() {

    override val bindingLayoutRes: Int
        get() = R.layout.fragment_tools

    override val viewModelBindings: List<ViewModelBinding>
        get() = list(ViewModelBinding(activity, BR.vm, MainViewModel::class.java))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_NoPadding_AnimatedFade)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        getViewModel(MainViewModel::class.java)?.selectionClosed()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding()?.backClick = View.OnClickListener { dismiss() }
        return view
    }
}
