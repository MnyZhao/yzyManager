package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

/**
 *  时间：2020/11/2-16:02
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.entity CommodityPrecondition
 *  描述：
 */
class CommodityPreconditionEntity : BaseEntity() {

    var datas = Data()

    class Data{

        var goodsTypeArray = ArrayList<Good>()
        var buyNotesArray = ArrayList<Note>()
        var defaultAdditionArray = ArrayList<AdditionalEntity.AdditionalCommon>()

    }


    class Good{
        var id = ""
        var goodsTypeName = ""
    }

    class Note{
        var id = ""
        var title = ""
    }

}