package com.idolmedia.yzymanager.viewmodel.order

import android.view.View
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.entity.OrderDetailsEntity
import com.idolmedia.yzymanager.model.entity.OrderListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.OrderUtils
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.common.ItemLineBean

/**
 *  时间：2020/10/20-18:02
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order OrderDetailsViewModel
 *  描述：
 */
class OrderDetailsViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()

    var orderEntity:OrderDetailsEntity.Datas? = null

    var orderId = ""
    var pageNo = 1

    fun queryOrderCommodity(){

        RetrofitHelper.instance().getOrderCommodity(orderId,pageNo,object : BaseObserver<OrderListEntity>(){
            override fun onSuccess(t: OrderListEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {
                        if (pageNo==1){
                            it.clear()
                        }

                        if (t.datas.isEmpty() && pageNo==1){
                            //空数据
                        }else if(t.datas.isNotEmpty()){
                            val current = it.getMaxPosition()
                            for(item in t.datas){
                                var money = 0f
                                for(p in item.productItems){
                                    p.mergeOrderNum = orderId
                                    it.add(ItemOrderCommodityBean(p,item.idCard))
                                    money+=p.amountMoney.toFloat()
                                }
                                it.add(ItemOrderTotalBean().apply {
                                    strTotalFreightMoney.set("合并邮费金额¥${item.expressMoney}")
                                    strTotalMoney.set("合计¥${money}")
                                    strTotalCount.set("共${item.productCount}件")
                                })
                            }

                            it.notifyInserted(current)
                        }


                    }
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

            override fun onComplete() {
                super.onComplete()
                finishRefresh.set(finishRefresh.get()+1)
            }

        })

    }

    fun queryOrderDetails(){

        RetrofitHelper.instance().getOrderDetails(orderId,object : BaseObserver<OrderDetailsEntity>(){
            override fun onSuccess(t: OrderDetailsEntity) {
                if (t.resultCode==1){
                    orderEntity = t.datas
                    adapter.get()?.let {
                        it.clear()
                        val addressEdit = if(t.datas.orderStatus == "waitingDelivery"){
                            if (t.datas.isMergeFee == "1"){
                                if (SpManager.userIsManage()){
                                    View.VISIBLE
                                }else{
                                    View.GONE
                                }
                            }else{
                                View.VISIBLE
                            }
                        }else{
                            View.GONE
                        }

                        it.add(ItemOrderDetailsTitleBean("收货人信息",addressEdit).apply {
                            orderNum = t.datas.orderNum
                            json = Gson().toJson(t.datas)
                        })

                        if (t.datas.idCard.isNullOrEmpty()){
                            it.add(ItemOrderDetailsBean("身份证信息","暂无"))
                        }else{
                            it.add(ItemOrderDetailsBean("身份证信息",t.datas.idCard,View.VISIBLE))
                        }
                        it.add(ItemOrderDetailsBean("收货人姓名",t.datas.consignee,true))
                        it.add(ItemOrderDetailsBean("收货电话",t.datas.phone,true))
                        it.add(ItemOrderDetailsBean("省市区",t.datas.cityAddress))
                        it.add(ItemOrderDetailsBean("详细地址",t.datas.consigneeAddress,true))

                        it.add(ItemLineBean(13, R.color.bg_fa))
                        it.add(ItemOrderDetailsTitleBean("订单详情"))
                        it.add(ItemOrderDetailsBean("订单状态",OrderUtils.getOrderStatus(t.datas.orderStatus)))
                        if (t.datas.isMergeFee == "1"){
                            it.add(ItemOrderDetailsBean("合并订单编号",t.datas.mergeOrderNum,true))
                        }else{
                            it.add(ItemOrderDetailsBean("订单编号",t.datas.orderNum,true))
                        }
                        it.add(ItemOrderDetailsBean("下单时间",t.datas.creatTime))
                        if (!t.datas.payTime.isNullOrEmpty()){
                            it.add(ItemOrderDetailsBean("付款时间",t.datas.payTime))
                        }
                        it.add(ItemOrderDetailsBean("下单账号",t.datas.primaryAccountNo,true))
                        if (t.datas.isMergeFee=="0"){
                            it.add(ItemOrderDetailsBean("商品ID",t.datas.productId,true))
                        }

                        if (!t.datas.addition.isNullOrEmpty()){
                            it.add(ItemLineBean(13, R.color.bg_fa))
                            it.add(ItemOrderDetailsTitleBean("附加信息",View.VISIBLE).apply {
                                orderNum = t.datas.orderNum
                            })
                            for(a in t.datas.addition){
                                if (a.addition_key.contains("sfz")){
                                    it.add(ItemOrderDetailsBean(a.field,a.addition_value,true))
                                }else{
                                    it.add(ItemOrderDetailsBean(a.field,a.addition_value))
                                }
                            }
                        }

                        if (!t.datas.wuliuItems.isNullOrEmpty()){
                            it.add(ItemLineBean(13, R.color.bg_fa))
                            it.add(ItemOrderDetailsTitleBean("物流信息",View.VISIBLE).apply {
                                orderNum = t.datas.orderNum
                                json = Gson().toJson(t.datas.wuliuItems)
                            })
                            it.add(ItemOrderDetailsFreightTitleBean())
                            for(e in t.datas.wuliuItems){
                                it.add(ItemOrderDetailsFreightBean(e))
                            }
                            it.add(ItemLineBean(13, R.color.white))
                        }

                        it.add(ItemLineBean(13, R.color.bg_fa))
                        it.add(ItemOrderDetailsTitleBean("支付信息",View.GONE))
                        it.add(ItemOrderDetailsBean("商品金额","${t.datas.productMoney}元"))
                        it.add(ItemOrderDetailsBean("邮费金额","${t.datas.expressMoney}元"))
                        it.add(ItemOrderDetailsBean("优惠券金额","${t.datas.couponMoney}元"))
                        it.add(ItemOrderDetailsBean("支付金额","${t.datas.amountMoney}元"))

                        it.add(ItemLineBean(13, R.color.bg_fa))
                        it.add(ItemOrderDetailsTitleBean("订单明细",View.GONE))
                        for(i in t.datas.orderItemByOrderNums){
                            for(o in i.orderItems){
                                it.add(ItemOrderDetailsCommodityBean(o,i))
                            }
                        }

                        it.notifyDataSetChanged()

                    }
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

            override fun onComplete() {
                super.onComplete()
                finishRefresh.set(finishRefresh.get()+1)
            }

        })

    }

}