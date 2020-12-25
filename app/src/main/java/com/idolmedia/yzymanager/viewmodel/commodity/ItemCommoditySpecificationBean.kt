package com.idolmedia.yzymanager.viewmodel.commodity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.model.bean.SpecificationBean
import com.idolmedia.yzymanager.view.commodity.CommoditySpecificationAddActivity

/**
 *  时间：2020/10/27-14:08
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity ItemBuySpecificationBean
 *  描述：
 */
class ItemCommoditySpecificationBean(var bean: SpecificationBean,private val onItemDeleteListener: OnItemDeleteListener,val isVipShop:Int) : BaseBean() {

    override fun getViewType(): Int {
        return R.layout.item_commodity_specification
    }

    val strName = ObservableField<String>()
    val strInventory = ObservableField<String>()
    val strLimit = ObservableField<String>()
    val strPriceOriginal = ObservableField<String>()
    val strPriceCurrent = ObservableField<String>()
    val strImg = ObservableField<String>()

    var isEdit = false

    init {

        initData()

    }

    private fun initData(){
        strImg.set(bean.catalogImg)
        strName.set("规格名称：${bean.catalogTitle}")
        strInventory.set("库存数量：${bean.surplusNo}")
        if (bean.limitBuy.isEmpty() ||bean.limitBuy.toInt()<=0){
            strLimit.set("不限购")
        }else{
            strLimit.set("限购：${bean.limitBuy}")
        }

        if (isVipShop==0){
            strPriceOriginal.set("划线价：${if (bean.originalPrice.isNullOrEmpty()) "无" else bean.originalPrice}")
            strPriceCurrent.set("普通价格：${bean.currentPrice}")
        }
        else if(isVipShop==1){
            strPriceOriginal.set("会员价：${bean.currentPrice}")
            strPriceCurrent.set("划线价：${if (bean.originalPrice.isNullOrEmpty()) "无" else bean.originalPrice}")
        }
        else if (isVipShop==2){
            strPriceOriginal.set("会员价：${bean.vipPrice}")
            strPriceCurrent.set("普通价格：${bean.currentPrice}")
        }
    }

    fun setNewBean(bean: SpecificationBean){
        this.bean = bean
        initData()
    }

    fun onClickEdit(view:View){
        val intent = Intent(view.context, CommoditySpecificationAddActivity::class.java)
        intent.putExtra("json",Gson().toJson(bean))
        intent.putExtra("isVipShop",isVipShop)
        intent.putExtra("isEdit",isEdit)
        (view.context as Activity).startActivityForResult(intent,position)
    }

    fun onClickDelete(view:View){
        onItemDeleteListener.onItemDelete(position,this)
    }

}