package com.idolmedia.yzymanager.model.bean

/**
 *  时间：2020/11/26-15:37
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.bean PushBean
 *  描述：
 */
class PushBean {

    var param = Param()
    var type = ""

    class Param{
        var id = ""
        var timeBatch = ""
        var shop_type = ""
        var shopcommon_id = ""
        var shoppingTo = ""
        var clickType = ""
    }


}