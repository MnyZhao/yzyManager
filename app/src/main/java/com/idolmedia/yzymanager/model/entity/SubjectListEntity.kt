package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

/**
 *  时间：2020/11/23-17:11
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.entity SubjectListEntity
 *  描述：
 */
class SubjectListEntity : BaseEntity() {

    var datas = ArrayList<Item>()

    class Item{
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
        var shoppingTo = ""
        var subjectDesc = ""
    }

}