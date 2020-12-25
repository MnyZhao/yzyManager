package com.idolmedia.yzymanager.viewmodel.order

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.databinding.ItemOrderMergeBinding
import com.idolmedia.yzymanager.model.entity.OrderListEntity
import com.idolmedia.yzymanager.utils.CopyUtil
import com.idolmedia.yzymanager.utils.OrderUtils
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.view.order.OrderDetailsActivity
import com.idolmedia.yzymanager.view.order.OrderDetailsListActivity

/**
 *  时间：2020/10/16-14:19
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order ItemOrderBean
 *  描述：
 */
class ItemOrderBean(val item : OrderListEntity.OrderItem) : BaseBean() {

    var layoutId = R.layout.item_order
    override fun getViewType(): Int {
        return layoutId
    }

    val strOrderNumber = ObservableField<String>()
    val strOrderType = ObservableField<String>()
    val strCommodityName = ObservableField<String>()
    val strCommodityPrice = ObservableField<String>()
    val strTotalMoney = ObservableField<String>()
    val strCommodityId = ObservableField<String>()
    val strCount = ObservableField<String>()
    val strIdNumber = ObservableField<String>()
    val strImg = ObservableField<String>()

    val list = ArrayList<BaseBean>()

    init {

        if (item.productItems.size>1){
            layoutId = R.layout.item_order_merge
            for(shop in item.productItems){
                list.add(ItemOrderMergeCommodityBean(shop))
            }
        }else{
            layoutId = R.layout.item_order

            val order = item.productItems[0]
            strImg.set(order.imageUrl)
            strCommodityName.set(order.shopName)
            strCommodityPrice.set("商品金额：${order.productMoney}")
            strCommodityId.set("商品ID:${order.productId}")

        }

        strOrderNumber.set(item.mergeOrderNum)

        if (item.idCard.isNullOrEmpty()){
            strIdNumber.set("用户身份：暂无")
        }else{
            strIdNumber.set("用户身份：${item.idCard}")
        }

        strOrderType.set(OrderUtils.getOrderStatus(item.mergeOrderStatus))

        //订单列表合计价格显示
        if (SpManager.userIsManage()){
            //管理员
            strTotalMoney.set("实付¥${item.amountMoney}")
        }else{
            //商户
            if (item.isMergeFee == "0"){
                //不合并
                strTotalMoney.set("实付¥${item.amountMoney}")
            }else{
                //合并
                strTotalMoney.set("实付¥${item.productMoney}")
            }
        }

        var count = 0
        for(shop in item.productItems){
            count+=shop.productCount.toInt()
        }

        strCount.set("共${count}件")
    }

    /**
     * @param type 0订单号  1身份证号 2商品id
     * */
    fun onClickCopy(type:Int,view:View){
        if (type==0){
            CopyUtil.copy(view.context,item.mergeOrderNum)
        }else if (type==1){
            if (!item.idCard.isNullOrEmpty()){
                CopyUtil.copy(view.context,item.idCard)
            }
        }else if (type==2){
            CopyUtil.copy(view.context,item.productItems[0].productId)
        }
    }

    fun onClickDetails(view:View){
        val intent = if(item.productItems.size>1){
            Intent(view.context,OrderDetailsListActivity::class.java)
        }else{
            Intent(view.context,OrderDetailsActivity::class.java)
        }
        intent.putExtra("orderId",item.mergeOrderNum)
        view.context.startActivity(intent)
    }

    override fun onBindViewModel(context: Context, binding: ViewDataBinding?, position: Int) {
        super.onBindViewModel(context, binding, position)
        if (binding is ItemOrderMergeBinding){
            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            if (binding.recyclerView.itemDecorationCount <=0){
                ItemDecorationCommon.addItemDecoration(ItemDecorationCommon.OrientationType.RIGHT,12,binding.recyclerView)
            }

            binding.recyclerView.layoutManager = manager
            binding.recyclerView.adapter = BaseAdapter(context,list)

        }
    }

}