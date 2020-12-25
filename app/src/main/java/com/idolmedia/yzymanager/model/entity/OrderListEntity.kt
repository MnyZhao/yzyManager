package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

class OrderListEntity: BaseEntity() {

    var datas = ArrayList<OrderItem>()
    var totalCount = ""

    class OrderItem{
        var virtualuserId = ""
        var amountMoney = ""
        var productMoney = ""
        var mergeOrderStatus = ""
        var idCard = ""
        var isMergeFee = ""
        var mergeOrderNum = ""
        var expressMoney = ""
        var productCount = ""
        var productItems = ArrayList<Product>()
    }

    class Product{
        var mergeOrderNum = ""
        var orderNum = ""
        var isMergeFee = ""
        var productId = ""
        var shopName = ""
        var imageUrl = ""
        var productCount = ""
        var productMoney = ""
        var orderStatus = ""
        var amountMoney = ""
    }

}