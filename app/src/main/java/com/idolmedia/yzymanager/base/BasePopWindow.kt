package com.idolmedia.yzymanager.base

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import com.idolmedia.yzymanager.R

/**
 *  时间：2019/7/10-11:22
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.base BasePopWindow
 *  描述：
 */
open class BasePopWindow(val context: Context?,val layoutId:Int): PopupWindow() {

    val binding: ViewDataBinding
    var popModel:BaseViewModel ?= null
    var activity: Activity? = null

    constructor(context: Context,layoutId:Int,viewModel:BaseViewModel):this(context, layoutId){
        popModel = viewModel
    }

    init {

        initType()
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),layoutId,null,false)
        contentView = binding.root

    }

    fun setWidthAndHeight(width:Int,height:Int){
        this.width = width
        this.height = height
    }

    /**
     * 居中弹出
     * */
    fun showAtCenter(view : View?){
        popModel?.let {
            binding.setVariable(BR.popModel,popModel)
            view?.let {
                showAtLocation(view,Gravity.CENTER,0,0)
                bgDarken(view.context as Activity)
            }
        }

    }

    /**从下面弹出*/
    open fun showAtBottom(view : View){
        popModel?.let {
            binding.setVariable(BR.popModel,popModel)
        }
        animationStyle = R.style.PopWindowUpStyle
        showAtLocation(view,Gravity.BOTTOM,0,0)
        bgDarken(view.context as Activity)
    }

    /**从某个view下面弹出*/
    open fun showAtViewBottom(view : View){
        popModel?.let {
            binding.setVariable(BR.popModel,popModel)
        }
        showAsDropDown(view)
    }

    /**从某个view下面弹出*/
    open fun showAtViewBottomDarken(view : View){
        popModel?.let {
            binding.setVariable(BR.popModel,popModel)
        }
        showAsDropDown(view)
        bgDarken(view.context as Activity)
    }

    @TargetApi(23)
    private fun initType() {
        windowLayoutType = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL
    }

    private fun bgDarken(activity:Activity?){
        this.activity = activity
        val lp = activity?.window?.attributes
        lp?.alpha = 0.5f
        activity?.window?.attributes = lp
    }

    override fun dismiss() {
        val lp = activity?.window?.attributes
        lp?.alpha = 1f
        activity?.window?.attributes = lp
        super.dismiss()
    }

    interface OnPopClickListener{
        fun onPopLeft(model: BaseViewModel)
        fun onPopRight(model: BaseViewModel)
    }

}