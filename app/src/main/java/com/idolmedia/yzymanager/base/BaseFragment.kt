package com.idolmedia.yzymanager.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.databinding.library.baseAdapters.BR

/**
 *  时间：2019/6/26-14:58
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.base BaseFragment
 *  描述：
 */
abstract class BaseFragment<E : ViewDataBinding> : Fragment() {

    var binding : E ?= null
    /**是否是第一次可见*/
    var isFirstVisibleHint = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (binding == null){
            binding = DataBindingUtil.inflate<E>(inflater, getLayoutId(),container,false)
            val viewModel = getViewModel()
            binding?.setVariable(BR.viewModel,viewModel)
            //绑定实时刷新数据
            lifecycle.addObserver(viewModel)
            //感知生命周期
            binding?.lifecycleOwner = this
        }
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewOrData()
    }

    abstract fun getLayoutId():Int

    abstract fun getViewModel():BaseViewModel

    /**初始化view或数据*/
    abstract fun initViewOrData()

    open fun isFirstVisible():Boolean{
        return if (isFirstVisibleHint){
            isFirstVisibleHint = false
            true
        }else{
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in childFragmentManager.fragments){
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**隐藏软键盘*/
    open fun hideKeyboard(view : View){
        view.postDelayed(
            {
                val manager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(view.windowToken, 0)
            } ,
            150
        )
    }


    fun showKeyBoard(et:View?){
        et?.postDelayed({
            et.requestFocus()
            val manager = et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(et, 0)
        },200)
    }

}