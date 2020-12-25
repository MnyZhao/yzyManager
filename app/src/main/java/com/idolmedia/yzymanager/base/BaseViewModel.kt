package com.idolmedia.yzymanager.base

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel

/**
 *  时间：2019/6/24-10:41
 *  @author yuLook
 *  描述：
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {

    val titleBar = ObservableField<TitleBarViewModel>()
    val finishRefresh = ObservableInt(0)
    val isLoadMore = ObservableBoolean(true)

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