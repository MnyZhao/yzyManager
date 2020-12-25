package com.idolmedia.yzymanager.viewmodel.pop

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BasePopViewModel
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.viewmodel.ido.ItemIdoBean

/**
 *  时间：2020/10/29-14:07
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.pop PopIdoAssociatedViewModel
 *  描述：
 */
class PopIdoAssociatedViewModel(private val deleteListener:OnItemDeleteListener,idoList:ArrayList<BaseBean>,adapter: BaseAdapter) : BasePopViewModel(),OnItemDeleteListener {

    val adapter = ObservableField<BaseAdapter>()

    val strCount = ObservableField<String>()
    var tip = ""

    init {


        for(item in idoList){
            if (item is ItemIdoBean){
                tip = "爱豆"
                adapter.add(ItemIdoBean(this,item.ido))
            }
            else if(item is ItemPopCommodityBean){
                if (item.item.shopType == "eleBook"){
                    tip = "电子刊"
                }else{
                    tip = "商品"
                }
                adapter.add(ItemPopCommodityBean(item.item,this))
            }
        }

        this.adapter.set(adapter)
        strCount.set("已选中${idoList.size}件${tip}")
    }

    override fun onItemDelete(position: Int, bean: BaseBean) {
        deleteListener.onItemDelete(position, bean)
        adapter.get()?.notifyDelete(position)

        strCount.set("已选中${adapter.get()?.getDate()?.size}件${tip}")
    }

}