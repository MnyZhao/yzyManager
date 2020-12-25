package com.idolmedia.yzymanager.base

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ktx.immersionBar
import com.zhy.autolayout.AutoLayoutActivity
import androidx.databinding.library.baseAdapters.BR
import com.idolmedia.yzymanager.R

/**
 *  时间：2019/6/25-17:43
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.base BaseActivity
 *  描述：
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseStateActivity<E: ViewDataBinding> : AutoLayoutActivity() {

    lateinit var binding : E

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ViewDataBinding>(this,getLayoutId()) as E
        val viewModel = getViewModel()
        viewModel?.let {
            binding.setVariable(BR.viewModel,viewModel)
            //感知生命周期
            lifecycle.addObserver(viewModel)
            //绑定生命周期
            binding.lifecycleOwner = this
        }

        //设置沉浸式状态栏
        immersionBar{
            fitsSystemWindows(true)
            statusBarColor(R.color.white)
            navigationBarColor(R.color.white)
            statusBarDarkFont(true)
        }
        //初始化Title
        initTitleBar(savedInstanceState)
        //初始化view
        initView(savedInstanceState)

    }

    abstract fun getLayoutId():Int

    abstract fun getViewModel():BaseViewModel?

    abstract fun initTitleBar(savedInstanceState: Bundle?)

    abstract fun initView(savedInstanceState: Bundle?)

    override fun getResources(): Resources {
        val res = super.getResources()
        if (res.configuration.fontScale != 1f){
            val newConfig = Configuration()
            newConfig.setToDefaults()
            //createConfigurationContext(newConfig)
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }

    /**隐藏软键盘*/
    fun hideKeyboard(view : View){
        //延时执行可解决方法无效的问题
        view.postDelayed(
            {
                val manager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(view.windowToken, 0)
            } ,
            150
        )

    }

    fun showKeyBoard(et:View){
        et.requestFocus()
        et.postDelayed({
            val manager = et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(et, 0)
        },200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for(fragment in supportFragmentManager.fragments){
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

}