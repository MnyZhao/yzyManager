package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity


class CurrentTimeEntity : BaseEntity() {

    var datas = Data()

    class Data{
        var currentTime = ""
    }

}