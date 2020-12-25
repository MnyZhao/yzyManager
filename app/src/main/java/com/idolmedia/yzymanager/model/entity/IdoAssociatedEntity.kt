package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

/**
 *  时间：2020/11/2-17:20
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.entity IdoAssociatedEntity
 *  描述：
 */
class IdoAssociatedEntity : BaseEntity() {

    var attestationIdol = ArrayList<Ido>()
    var datas = ArrayList<Ido>()

    class Ido{
        var idolId = ""
        var idoName = ""
        var idoHeadImg = ""
        var fansCount = ""
    }

}