package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

/**
 *  时间：2020/11/18-20:04
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.entity HomeMessageEntity
 *  描述：
 */
class HomeMessageEntity : BaseEntity() {

    var checkProductNum = 0
    var messageNum = 0
    var orderStatusNum = ArrayList<Item>()

    class Item{
        var orderStatus = ""
        var num = 0
    }

}