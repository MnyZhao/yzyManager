package com.idolmedia.yzymanager.viewmodel.common

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean

/**
 *  时间：2020/10/20-18:07
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order ItemOrderDetailsExpressBean
 *  描述：
 */
class ItemTitleBean(title:String) : BaseBean() {

    constructor(title: String,color:Int):this(title){
        bgColor.set(color)
    }


    override fun getViewType(): Int {
        return R.layout.item_common_title
    }

    val strTitle = ObservableField(title)
    val bgColor = ObservableInt()

}