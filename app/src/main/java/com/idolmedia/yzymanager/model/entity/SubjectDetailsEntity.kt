package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

/**
 *  时间：2020/11/24-14:40
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.entity SubjectDetailsEntity
 *  描述：
 */
class SubjectDetailsEntity : BaseEntity() {

    var datas = Data()

    class Data{
        var subjectId = ""
        var subjectName = ""
        var sbjectCatalogTitle = ""
        var subjectCatalogId = ""
        var previewUrl = ""
        var detailsHeadImgUrl = ""
        var createTimeStr = ""
        var sortNo = ""
        var isMergeExpress = ""
        var isPutway = ""
        var labelList = ArrayList<Label>()
        var subjectDetails = ""
        var freightId = ""
        var overseasFreightId = ""
        var productList = ArrayList<Commodity>()
        var eleBookList = ArrayList<Book>()

    }

    class Label{
        var id = ""
        var label = ""
        var subjectId = ""
        var createTime = ""
    }

    class Book{
        var shopcommonId = ""
        var shopName = ""
        var imageUrl = ""
        var sortNo = ""
    }

    class Commodity{
        var shopcommonId = ""
        var shopName = ""
        var imageUrl = ""
        var sortNo = ""
        var overseas = false
    }

}