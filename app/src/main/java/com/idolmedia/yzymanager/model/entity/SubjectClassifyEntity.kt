package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

/**
 *  时间：2020/11/19-15:53
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.entity SubjectClassifyEntity
 *  描述：
 */
class SubjectClassifyEntity : BaseEntity() {

    var datas = ArrayList<Item>()

    class Item{
        var id = ""
        var title = ""
        var url = ""
    }

}