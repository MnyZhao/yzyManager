package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

/**
 *  时间：2020/4/14-14:12
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmanage.module_common.net.entity LogisticsDetailsEntity
 *  描述：
 */
class LogisticsDetailsEntity : BaseEntity() {

    var datas = ArrayList<Item>()

    class Item{
        var wlStatus = ""
        var wlTime = ""
        var wlContext = ""
    }

}