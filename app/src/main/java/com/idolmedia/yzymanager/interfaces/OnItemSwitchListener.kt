package com.idolmedia.yzymanager.interfaces

import com.idolmedia.yzymanager.base.BaseBean

/**
 *  时间：2020/10/29-10:43
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.interfaces OnItemSwitchListener
 *  描述：
 */
interface OnItemSwitchListener {

    fun onItemSwitch(position:Int,bean:BaseBean,switch:Boolean)

}