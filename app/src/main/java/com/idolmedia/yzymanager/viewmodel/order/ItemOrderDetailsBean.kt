package com.idolmedia.yzymanager.viewmodel.order

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.utils.CopyUtil

/**
 *  时间：2020/10/20-18:07
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order ItemOrderDetailsExpressBean
 *  描述：
 */
class ItemOrderDetailsBean() : BaseBean() {

    var layoutId = R.layout.item_order_details
    override fun getViewType(): Int {
        return layoutId
    }

    constructor(title:String,content:String):this(){
        strTitle.set(title)
        strContent.set(content)
    }

    constructor(title:String,content:String?,isCopy:Boolean):this(){
        strTitle.set(title)
        strContent.set(content)
        visibleCopy.set(if (isCopy) View.VISIBLE else View.GONE)
        if (isCopy){
            visibleMore.set(View.GONE)
        }
    }

    constructor(title:String,content:String,hint:String):this(){
        layoutId = R.layout.item_order_details_edit
        strTitle.set(title)
        strContent.set(content)
        strHint.set(hint)
    }

    constructor(title:String,content:String,visibleRight:Int):this(){
        strTitle.set(title)
        strContent.set(content)
        visibleMore.set(visibleRight)
    }

    val strTitle = ObservableField<String>()
    val strContent = ObservableField<String>()
    val strHint = ObservableField<String>()
    val visibleMore = ObservableInt(View.INVISIBLE)
    val visibleCopy = ObservableInt(View.GONE)

    fun onClickCopy(view:View){
        CopyUtil.copy(view.context,strContent.get())
    }

}