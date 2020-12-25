package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

class LoginEntity : BaseEntity() {

    var datas = Data()

    class Data{
        var headImg = ""
        var token = ""
        var usKey = ""
        var nickName = ""
        var userId = ""
        var username = ""
        var roleCode = ""
        var accountType = ""
        var attestationType = ""
        /**众筹商品：0-不可创建；1-可以创建 */
        var manageLimit = ""
    }

}