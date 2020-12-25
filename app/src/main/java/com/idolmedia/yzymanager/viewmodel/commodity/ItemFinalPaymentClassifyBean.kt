package com.idolmedia.yzymanager.viewmodel.commodity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.interfaces.OnFinalPaymentListener
import com.idolmedia.yzymanager.model.bean.SpecificationBean
import com.idolmedia.yzymanager.view.commodity.CommoditySpecificationAddActivity

/**
 *  时间：2020/10/26-11:37
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity ItemAddFinalPaymentDetailsBean
 *  描述：
 */
class ItemFinalPaymentClassifyBean(val listener:OnFinalPaymentListener,val isAudit:Boolean) : BaseBean(){
    override fun getViewType(): Int {
        return R.layout.item_commodity_final_payment_classify
    }

    constructor(item:SpecificationBean,isAudit:Boolean,listener:OnFinalPaymentListener,isVipShop:Int):this(listener,isAudit){
        this.item = item
        this.isVipShop = isVipShop
        showData()
    }

    val strCommodityStatus = ObservableField<String>()
    val strCommodityName = ObservableField<String>()
    val strCommodityLimit = ObservableField<String>()
    val strCommodityPrice = ObservableField<String>()
    val strCommoditySales = ObservableField<String>()
    val strCommodityImg = ObservableField<String>()
    val strShopName = ObservableField<String>()
    val strShopId = ObservableField<String>()
    val isAdd = ObservableBoolean(false)

    var item:SpecificationBean? = null
    var isVipShop = 0

    fun showData(){
        if (item?.cataFlag == "0"){
            strCommodityStatus.set("普通商品")
        }else if(item?.cataFlag == "1"){
            strCommodityStatus.set("定金商品")
            isAdd.set(true)
        }else if(item?.cataFlag == "2"){
            strCommodityStatus.set("尾款商品")
            isAdd.set(false)
        }
        strCommodityImg.set(item?.catalogImg)
        strCommodityName.set(item?.catalogTitle)
        strCommodityLimit.set("限购：${if (item?.limitBuy.isNullOrEmpty() || item?.limitBuy!!.toInt()<=0) "不限购" else item?.limitBuy}")
        strCommodityPrice.set("商品价格：¥${item?.currentPrice}")
        strCommoditySales.set("销量：${item?.saleNo}")
    }

    fun onClickCopyShop(view: View){

    }

    fun onClickAdd(view:View){
        listener.onItemAdd(position,this)
    }

    fun onClickDelete(view:View){
        listener.onItemDelete(position,this)
    }

    fun onClickEdit(view:View){
        val intent = Intent(view.context, CommoditySpecificationAddActivity::class.java)
        intent.putExtra("json", Gson().toJson(item))
        intent.putExtra("isVipShop",isVipShop)
        if (!isAudit){
            //审核通过的商品编辑已有商品分类时不可以修改库存
            intent.putExtra("isEdit",!item?.sscId.isNullOrEmpty())
        }
        (view.context as Activity).startActivityForResult(intent,position)
    }

}