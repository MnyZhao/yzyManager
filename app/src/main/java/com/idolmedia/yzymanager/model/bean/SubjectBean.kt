package com.idolmedia.yzymanager.model.bean

/**
 *  时间：2020/11/19-15:57
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.bean SubjectBean
 *  描述：
 */
class SubjectBean {

    var subjectName = ""
    var subjectCatalogId = ""
    var subjectLabels = ""
    var subjectDetail = ""
    var isMergeExpress = "0"
    var commodityOverseasList = ""
    var overseasFreightId = ""
    var commodityList = ""
    var freightId = ""
    var bookList = ""
    var detailsHeadImgUrl = ""
    var previewUrl = ""


    class Commodity{
        var shopcommonId = ""
        var sortNo = 0
        /**香港-true；国内-false*/
        var overseas = false
    }

}