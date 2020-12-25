package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

/**
 *  时间：2020/11/10-14:36
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.entity AccountIdentityEntity
 *  描述：
 */
class AccountIdentityEntity : BaseEntity() {

    val datas = ArrayList<Identity>()

    class Identity{
        var roleCode = ""
    }

}