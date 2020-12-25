package com.idolmedia.yzymanager.viewmodel

import android.app.Activity
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseViewModel

/**
 *  时间：2019/6/28-17:54
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.bean.viewmodel TitleBarViewModel
 *  描述：TitleBar
 */
class TitleBarViewModel(title:String) : BaseViewModel(){

    val barTitle =  ObservableField<String>()
    val rightDrawable = ObservableInt()
    val rightStr = ObservableField<String>()
    val rightImgVisible = ObservableInt(View.GONE)
    val rightStrVisible = ObservableInt(View.GONE)

    val backgroundColor = ObservableInt(R.color.white)

    private var leftListener:OnLeftClickListener ?= null
    private var twoClickListener:OnClickListener ?= null


    /**自定义back按钮的点击事件*/
    constructor(title:String,leftListener:OnLeftClickListener):this(title){
        this.leftListener = leftListener
    }

    /**自定义左右俩个按钮的点击事件*/
    constructor(title:String,drawable: Int,twoClickListener:OnClickListener):this(title){
        this.twoClickListener = twoClickListener
        rightDrawable.set(drawable)
        rightImgVisible.set(View.VISIBLE)
    }

    /**自定义左右俩个按钮的点击事件*/
    constructor(title:String,right: String,twoClickListener:OnClickListener):this(title){
        this.twoClickListener = twoClickListener
        rightStr.set(right)
        rightStrVisible.set(View.VISIBLE)
    }

    init {
        barTitle.set(title)
    }

    fun onBackClick(view:View){
        leftListener?.let {
            leftListener!!.onBackClick(view)
            return
        }
        twoClickListener?.let {
            twoClickListener!!.onBackClick(view)
            return
        }
        val activity = view.context as Activity
        activity.onBackPressed()
    }

    fun onRightClick(view:View){
        twoClickListener?.let {
            it.onRightClick(view)
        }
    }


    interface OnLeftClickListener{
        fun onBackClick(view :View)
    }

    interface OnClickListener{
        fun onBackClick(view :View)
        fun onRightClick(view :View)
    }

}