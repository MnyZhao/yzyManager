package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

/**
 *  时间：2020/4/14-11:06
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmanage.module_common.net.entity LogisticsNumberEntity
 *  描述：
 */
class LogisticsNumberEntity : BaseEntity() {

    var datas = Data()

    class Data{

        var wuliuList = ArrayList<LogisticsItem>()
        var expressInfo = ""
        var count = ""
        var topInfo = ""

    }

    class LogisticsItem{
        var express_time = ""
        var kdCode = ""
        var order_num = ""
        var shipperName = ""
        var shippercode = ""
        var waybill_no = ""
    }

}