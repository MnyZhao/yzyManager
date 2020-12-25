package com.idolmedia.yzymanager.interfaces

import com.idolmedia.yzymanager.base.BaseBean

/**
 *  时间：2020/10/29-14:34
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.interfaces OnItemDeleteListener
 *  描述：
 */
interface OnItemDeleteListener {

    fun onItemDelete(position:Int,bean:BaseBean)

}