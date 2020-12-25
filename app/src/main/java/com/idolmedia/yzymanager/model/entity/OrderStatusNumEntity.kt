package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity


class OrderStatusNumEntity : BaseEntity(){

    var datas = ArrayList<Item>()

    class Item{
        var count = ""
        var orderStatus = ""
    }

}