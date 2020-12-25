package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity


/**
 *  时间：2020/4/18-10:38
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmanage.module_common.net.entity AccountListEntity
 *  描述：
 */
class AccountListEntity : BaseEntity() {

    var datas = ArrayList<AccountItem>()

    class AccountItem{

        var bindAccountId = ""
        var bindUserId = ""
        var createTime = ""
        var headImg = ""
        var nickName = ""
        var password = ""
        var userId = ""
        var userName = ""

    }

}