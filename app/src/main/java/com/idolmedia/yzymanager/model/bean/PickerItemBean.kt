package com.idolmedia.yzymanager.model.bean

/**
 *  时间：2019/7/14-16:11
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.bean.entity PickerItemBean
 *  描述：
 */
class PickerItemBean(val id:Int,val countryName:String) {

    override fun toString(): String {
        return countryName
    }

}