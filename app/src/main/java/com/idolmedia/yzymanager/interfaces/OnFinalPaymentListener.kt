package com.idolmedia.yzymanager.interfaces

import com.idolmedia.yzymanager.base.BaseBean

/**
 *  时间：2020/11/12-17:56
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.interfaces OnFinalPaymentListener
 *  描述：
 */
interface OnFinalPaymentListener {

    fun onItemDelete(position:Int,bean:BaseBean)

    fun onItemAdd(position:Int,bean:BaseBean)

}