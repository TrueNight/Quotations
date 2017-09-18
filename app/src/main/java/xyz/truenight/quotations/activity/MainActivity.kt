package xyz.truenight.quotations.activity

import xyz.truenight.quotations.BR
import xyz.truenight.quotations.R
import xyz.truenight.quotations.binding.ViewModelBinding
import xyz.truenight.quotations.databinding.ActivityMainBinding
import xyz.truenight.quotations.viewmodel.MainViewModel

class MainActivity : BindingLifecycleActivity<ActivityMainBinding>() {

    override val bindingLayoutRes: Int
        get() = R.layout.activity_main
    override val viewModelBindings: List<ViewModelBinding>
        get() = list(ViewModelBinding(this, BR.vm, MainViewModel::class.java))

}
