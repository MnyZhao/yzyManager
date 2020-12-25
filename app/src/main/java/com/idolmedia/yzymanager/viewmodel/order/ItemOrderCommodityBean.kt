package com.idolmedia.yzymanager.viewmodel.order

import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.model.entity.OrderListEntity
import com.idolmedia.yzymanager.utils.CopyUtil
import com.idolmedia.yzymanager.utils.OrderUtils
import com.idolmedia.yzymanager.view.order.OrderAdditionalEditActivity
import com.idolmedia.yzymanager.view.order.OrderDetailsActivity

/**
 *  时间：2020/10/16-14:19
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order ItemOrderBean
 *  描述：
 */
class ItemOrderCommodityBean(val item : OrderListEntity.Product,val idCard:String?) : BaseBean() {

    override fun getViewType(): Int {
        return R.layout.item_order_commodity
    }

    val strOrderNumber = ObservableField<String>()
    val strOrderType = ObservableField<String>()
    val strCommodityName = ObservableField<String>()
    val strCommodityPrice = ObservableField<String>()
    val strCommodityId = ObservableField<String>()
    val strIdNumber = ObservableField<String>()
    val strImg = ObservableField<String>()

    val list = ArrayList<BaseBean>()

    init {

        strOrderNumber.set(item.orderNum)
        strImg.set(item.imageUrl)
        strCommodityName.set(item.shopName)
        strCommodityPrice.set("商品金额：¥${item.productMoney}")
        strOrderType.set(OrderUtils.getOrderStatus(item.orderStatus))
        if (idCard.isNullOrEmpty()){
            strIdNumber.set("用户身份：暂无")
        }else{
            strIdNumber.set("用户身份：${idCard}")
        }
        strCommodityId.set("商品ID:${item.productId}")
    }

    fun onClickEditAdditional(view:View){
        val intent = Intent(view.context,OrderAdditionalEditActivity::class.java)
        intent.putExtra("orderNum",item.orderNum)
        view.context.startActivity(intent)
    }

    /**
     * @param type 0订单号  1身份证号
     * */
    fun onClickCopy(type:Int,view:View){
        if (type==0){
            CopyUtil.copy(view.context,item.orderNum)
        }else if (type==1){
            if (!idCard.isNullOrEmpty()){
                CopyUtil.copy(view.context,idCard)
            }
        }else if (type==2){
            CopyUtil.copy(view.context,item.productId)
        }
    }

    fun onClickDetails(view:View){
         val intent = Intent(view.context,OrderDetailsActivity::class.java)
        intent.putExtra("orderId",item.mergeOrderNum)
        view.context.startActivity(intent)
    }

}