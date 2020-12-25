package com.idolmedia.yzymanager.base

import android.view.View
import androidx.databinding.ObservableField

/**
 *  时间：2020/10/22-18:03
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.base BasePopViewModel
 *  描述：
 */
open class BasePopViewModel:BaseViewModel() {

    val title = ObservableField<String>()
    val content = ObservableField<String>()

    var listener:BasePopWindow.OnPopClickListener? = null

    fun onClickLeft(view:View){
        listener?.onPopLeft(this)
    }

    fun onClickRight(view: View){
        listener?.onPopRight(this)
    }

}