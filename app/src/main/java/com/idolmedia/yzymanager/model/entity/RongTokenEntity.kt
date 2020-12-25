package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity


/**
 *  时间：2019/8/20-16:19
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.bean.entity RongTokenEntity
 *  描述：
 */
class RongTokenEntity : BaseEntity() {

    var datas = Data()

    class Data{
        var userId = ""
        var token = ""
    }

}