package com.idolmedia.yzymanager.viewmodel.pop

import android.view.View
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.model.entity.CommodityListEntity

/**
 *  时间：2020/11/23-13:56
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.pop ItemPopCommdityBean
 *  描述：
 */
class ItemPopCommodityBean(val item:CommodityListEntity.Data,val onItemDeleteListener: OnItemDeleteListener) : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_pop_commodity
    }

    val strImg = ObservableField<String>()
    val strName = ObservableField<String>()
    val strId = ObservableField<String>()

    init {
        strImg.set(item.imageUrl)
        strName.set(item.shopName)
        if (item.shopType == "eleBook"){
            strId.set("电子刊ID:${item.shopcommonId}")
        }else{
            strId.set("商品ID:${item.shopcommonId}")
        }
    }

    fun onClickDelete(view:View){
        onItemDeleteListener.onItemDelete(position,this)
    }

}