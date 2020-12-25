package com.idolmedia.yzymanager.viewmodel

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean

/**
 *  时间：2020/12/2-16:09
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel ItemEmptyBean
 *  描述：
 */
class ItemEmptyBean() : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_empty
    }

    constructor(content:String):this(){
        emptyContent.set(content)
    }

    var emptyContent = ObservableField<String>()

}