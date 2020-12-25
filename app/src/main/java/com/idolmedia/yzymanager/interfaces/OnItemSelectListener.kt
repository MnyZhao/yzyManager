package com.idolmedia.yzymanager.interfaces

import com.idolmedia.yzymanager.base.BaseBean

/**
 *  时间：2020/11/2-10:36
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.interfaces OnItemSelectListener
 *  描述：
 */
interface OnItemSelectListener {

    fun onItemSelect(position:Int,bean:BaseBean)

}