package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

/**
 *  时间：2020/11/19-10:16
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.entity MessageListEntity
 *  描述：
 */
class MessageListEntity : BaseEntity() {

    var datas = ArrayList<Message>()


    class Message{
        var id = ""
        var title = ""
        var content = ""
        var createTimeStr = ""
        /**0未读  1已读*/
        var haveRead = ""
        var virtualuserId = ""
        var hasPush = ""
        var pushType = ""
        var shopcommonId = ""
        var shopType = ""
        var clickType = ""
        var shoppingTo = ""
        var timeBatch = ""
    }

}