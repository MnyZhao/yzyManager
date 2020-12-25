package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

class OrderDetailsEntity : BaseEntity(){

    var datas = Datas()

    class Datas{
        var productMoney = ""
        var creatTime = ""
        var consignee = ""
        var phone = ""
        var cityAddress = ""
        var orderStatus = ""
        var consigneeAddress = ""
        var orderNum = ""
        var mergeOrderNum = ""
        var fromId = ""
        var expressMoney = ""
        var couponMoney = ""
        var amountMoney = ""
        var payTime = ""
        var productId = ""
        var idCard = ""
        var name = ""
        var frontIdcardImg = ""
        var backIdcardImg = ""
        var shopName = ""
        var imageUrl = ""
        var nickName = ""

        /**1合并 0非合并*/
        var isMergeFee = ""

        var province = ""
        var city = ""
        var county = ""
        var areaId = ""

        var primaryAccountNo = ""

        var addition = ArrayList<AdditionalEntity.Additional2>()
        var orderItemByOrderNums = ArrayList<Item>()
        var wuliuItems = ArrayList<Express>()

    }

    class Item{
        var payWkOrderNum = ""
        var payType = ""
        var orderItems = ArrayList<Order>()
    }

    class Order{
        var orderitemsId = ""
        var orderId = ""
        var shopcommonId = ""
        var shopName = ""
        var subName = ""
        var catalogId = ""
        var catalogName = ""
        var payMoney = ""
        var creatTime = ""
        var originalPrice = ""
        var shopCount = ""
        var catalogImg = ""
    }

    class Express{
        var waybillNo = ""
        var shippercode = ""
        var shippername = ""
    }


}