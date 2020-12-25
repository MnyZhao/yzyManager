package com.idolmedia.yzymanager.viewmodel.commodity

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.model.entity.CommodityPreconditionEntity
import com.idolmedia.yzymanager.model.entity.SubjectClassifyEntity

/**
 *  时间：2020/10/26-17:33
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity ItemCommodityAddSelectBean
 *  描述：
 */
class ItemCommodityAddSelectBean() : BaseBean(){

    constructor(title:String,id:String,select:Boolean):this(){
        strTitle.set(title)
        isSelect.set(select)
        this.id = id
    }

    constructor(title:String,select:Boolean):this(){
        strTitle.set(title)
        isSelect.set(select)
    }

    constructor(good:CommodityPreconditionEntity.Good,select:Boolean):this(){
        strTitle.set(good.goodsTypeName)
        isSelect.set(select)
        id = good.id
    }

    constructor(good:SubjectClassifyEntity.Item,select:Boolean):this(){
        strTitle.set(good.title)
        isSelect.set(select)
        id = good.id
    }

    override fun getViewType(): Int {
        return R.layout.item_commodity_add_select
    }

    val strTitle = ObservableField<String>()
    val isSelect = ObservableBoolean()
    var id = ""

}