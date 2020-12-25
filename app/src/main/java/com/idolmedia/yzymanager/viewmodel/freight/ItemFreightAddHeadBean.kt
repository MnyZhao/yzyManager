package com.idolmedia.yzymanager.viewmodel.freight

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.model.entity.FreightListEntity

/**
 *  时间：2020/10/27-10:10
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.freight ItemFreightAddHeadBean
 *  描述：
 */
class ItemFreightAddHeadBean() : BaseBean() {

    constructor(item:FreightListEntity.FreightItem):this(){
        strName.set(item.name)
        strFirstCount.set(item.firstPart)
        strFirstPrice.set(item.firstPrice)
        strSubCount.set(item.continuePart)
        strSubPrice.set(item.continuePrice)
    }

    override fun getViewType(): Int {
        return R.layout.item_freight_add_head
    }

    val strName = ObservableField<String>()

    val strFirstCount = ObservableField<String>()
    val strFirstPrice = ObservableField<String>()

    val strSubCount = ObservableField<String>()
    val strSubPrice = ObservableField<String>()

}