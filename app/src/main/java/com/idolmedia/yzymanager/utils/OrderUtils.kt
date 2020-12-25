package com.idolmedia.yzymanager.utils

/**
 *  时间：2020/11/17-16:28
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.utils OrderUtils
 *  描述：
 */
object OrderUtils {

    fun getOrderStatus(status:String):String{
        return when(status){
            "toPay" -> "待支付"
            "waitingDelivery" -> "待发货"
            "waitingGoods" -> "待收货"
            "waitingApprise" -> "待评价"
            "orderOver" -> "已完成"
            "refunded" -> "已退款"
            "cancel" -> "订单取消"
            "invalid" -> "支付超时"
            "waitingFinalPay" -> "待付尾款"
            "waitingRefunded" -> "待退款"
            "stockUp" -> "备货中"
            "orderDisable" -> "订单无效"
            "reserveDisable" -> "定金订单失效"
            "crowdfunding" -> "众筹中"
            else -> status
        }
    }

}