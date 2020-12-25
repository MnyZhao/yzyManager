package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity


class ExpressCompanyEntity : BaseEntity() {

    var datas = ArrayList<Item>()

    class Item{
        var shippername = ""
        var kdcode = ""

        override fun toString(): String {
            return shippername
        }
    }

}