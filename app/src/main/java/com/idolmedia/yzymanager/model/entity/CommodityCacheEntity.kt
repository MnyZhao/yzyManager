package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

class CommodityCacheEntity : BaseEntity() {

    var addition = ArrayList<Addition>()
    var aidou = ArrayList<Aidou>()

    class Addition{
        var field = ""
        var key = ""
        var placehold = ""
    }

    class Aidou{
        var id = ""
        var liasName = ""
        var name = ""
        var sortLetters = ""
        var idoHeadImg = ""
    }

}