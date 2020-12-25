package com.idolmedia.yzymanager.viewmodel.common

import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.utils.Dp2PxUtils

/**
 *  时间：2020/10/29-13:48
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel ItemLineBean
 *  描述：
 */
class ItemLineBean(height:Int) : BaseBean() {

    constructor(height:Int,color:Int):this(height){
        this.color.set(color)
    }

    override fun getViewType(): Int {
        return R.layout.item_common_line
    }

    val color = ObservableInt(R.color.gray_e)

    init {
        this.height.set(Dp2PxUtils.dip2px(height).toInt())
    }

}