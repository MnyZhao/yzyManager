package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity


class FreightListEntity : BaseEntity() {

    var datas = ArrayList<FreightItem>()

    class FreightItem{

        var freightId = ""
        var name = ""
        // 1 正在使用 不能删除
        var isUsing = 0

        var firstPart = ""
        var firstPrice = ""
        var continuePart = ""
        var continuePrice = ""

        var freightitems = ArrayList<Item>()

    }

    class Item{
        var areaIds = ""
        var areaNames = ""
        var continuePart = ""
        var continuePrice = ""
        var creatTime = ""
        var firstPart = ""
        var firstPrice = ""
        var freightitemsId = ""
        var freightOrder = ""
        var freightType = ""
        //1 是默认
        var isDefault = ""
        var isMandatory = ""
    }

}