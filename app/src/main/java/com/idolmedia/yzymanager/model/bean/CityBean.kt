package com.idolmedia.yzymanager.model.bean

import com.contrarywind.interfaces.IPickerViewData

/**
 *  时间：2019/7/19-18:19
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.bean CityBean
 *  描述：
 */
class CityBean : IPickerViewData{

    var code = 0
    var name = ""
    var children = ArrayList<CityBean>()

    override fun getPickerViewText(): String {
        return name
    }

}