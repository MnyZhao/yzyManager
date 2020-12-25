package com.idolmedia.yzymanager.viewmodel.freight

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.model.bean.FreightBean
import com.idolmedia.yzymanager.model.entity.FreightListEntity
import com.idolmedia.yzymanager.view.freight.FreightAddSpecialActivity

/**
 *  时间：2020/10/27-10:10
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.freight ItemFreightAddHeadBean
 *  描述：
 */
class ItemFreightAddAreaBean(val onItemSelectListener: OnItemSelectListener) : BaseBean() {

    constructor(add:Boolean,onItemSelectListener: OnItemSelectListener):this(onItemSelectListener){
        layoutId = R.layout.item_freight_add_area_add
    }

    constructor(areaIds:String,area:String,onItemSelectListener: OnItemSelectListener):this(onItemSelectListener){
        layoutId = R.layout.item_freight_add_area
        strArea.set(area)
        bean.areaIds = areaIds
        bean.areaNames = area
    }

    constructor(item:FreightListEntity.Item,onItemSelectListener: OnItemSelectListener):this(onItemSelectListener){
        layoutId = R.layout.item_freight_add_area
        strArea.set(item.areaNames)
        bean.areaIds = item.areaIds
        bean.areaNames = item.areaNames
        bean.firstPart = item.firstPart
        bean.firstPrice = item.firstPrice
        bean.continuePart = item.continuePart
        bean.continuePrice = item.continuePrice
    }

    var layoutId = R.layout.item_freight_add_area
    override fun getViewType() = layoutId

    val strArea = ObservableField<String>()
    val strTitle = ObservableField<String>()
    val bean = FreightBean()

    fun onClickAdd(view:View){
        onItemSelectListener.onItemSelect(position,this)
    }

    fun onClickEdit(view:View){
        onItemSelectListener.onItemSelect(position,this)
    }

    fun onClickPrice(view:View){
        val intent = Intent(view.context, FreightAddSpecialActivity::class.java)
        intent.putExtra("freight",Gson().toJson(bean))
        (view.context as Activity).startActivityForResult(intent,position)
    }

}