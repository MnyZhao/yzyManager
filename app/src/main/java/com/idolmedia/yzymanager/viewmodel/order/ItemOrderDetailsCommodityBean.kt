package com.idolmedia.yzymanager.viewmodel.order

import android.view.View
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.model.entity.OrderDetailsEntity
import com.idolmedia.yzymanager.utils.CopyUtil

/**
 *  时间：2020/10/20-18:07
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order ItemOrderDetailsExpressBean
 *  描述：
 */
class ItemOrderDetailsCommodityBean(val item:OrderDetailsEntity.Order,val parent:OrderDetailsEntity.Item) : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_order_details_commodity
    }

    val strOrderNumber = ObservableField<String>()
    val strPayStatus = ObservableField<String>()
    val strImg = ObservableField<String>()

    val strCommodityName = ObservableField<String>()
    val strCommodityClassify  = ObservableField<String>()
    val strCommodityPrice = ObservableField<String>()
    val strCommodityCount = ObservableField<String>()

    init {

        strImg.set(item.catalogImg)
        strOrderNumber.set("付款订单编号：${parent.payWkOrderNum}")
        when(parent.payType){
            "wxPay" ->strPayStatus.set("微信支付")
            "AliPay" ->strPayStatus.set("支付宝支付")
            "aliGlobal" ->strPayStatus.set("国际支付宝支付")
            "" ->strPayStatus.set("未支付")
            else ->strPayStatus.set(parent.payType)
        }

        strCommodityName.set(item.shopName)
        strCommodityClassify.set("分类：${item.catalogName}")
        strCommodityPrice.set("商品价格：¥${item.payMoney}")
        strCommodityCount.set("下单数量：${item.shopCount}")

    }

    fun onClickCopy(view: View){
        CopyUtil.copy(view.context,parent.payWkOrderNum)
    }

}