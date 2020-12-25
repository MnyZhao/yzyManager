package com.idolmedia.yzymanager.interfaces

import com.idolmedia.yzymanager.base.BaseBean

/**
 *  时间：2020/12/2-14:35
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.interfaces OnSubjectListener
 *  描述：
 */
interface OnSubjectListener {

    fun onDelete(position:Int)

    fun onPutAway(position: Int)

    fun onRefresh(bean : BaseBean)

}