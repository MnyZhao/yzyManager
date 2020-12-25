package com.idolmedia.yzymanager.model.bean

import com.idolmedia.yzymanager.model.entity.AdditionalEntity

/**
 *  时间：2020/11/3-10:31
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.bean CommodityPublishBean
 *  描述：
 */
class CommodityPublishBean {

    var shopName = ""
    //support-应援；ordinary-普通；reserve2-定金模式； crowdfunding-众筹商品
    var shopType = ""
    var reserveStatus = ""
    var shopDetails = ""
    //0-不代管、自管；1-代管
    var isProxies = 0
    //主图
    var imageUrl = ""
    //副图
    var subImageArray = ""
    //0-不是；1-是，仅限会员用户购买；2-皆可购买
    var isVipShop = -1
    var goodsTypeId = ""
    //0-全部用户；1-第三方商家；2-粉丝团商家；3-第三方商家和粉丝团商家
    var buyerGrade = -1
    //0-中国大陆；1-港澳台+国外；2-全球；3-港澳台；4-港澳台+中国大陆；5-国外 ；6-中国大陆+国外
    var isAreaLimit = ""
    //是否定时上架：0-否；1是
    var isTimmingUp = 0
    var upTime = ""
    //是否有活动时间限制 有限制需要设置结束时间
    var isLimitTime = false
    var startTime = ""
    var endTime = ""
    var finalStartTime = ""
    var finalEndTime = ""
    //发行时间
    var issuingDate = ""
    //配送时间
    var deliveryDate = ""
    var shoppingTo = ""
    //1推送，0不推送
    var pushType = ""
    //运费模板id；如果包邮穿0
    var freightId = ""
    //爱豆id，每个逗号隔开
    var aidouIds = ""
    var idoList = ""
    //购买须知
    var buyNotes = ArrayList<BuyNotesBean>()
    //商品规格
    var catalogItems = ArrayList<SpecificationBean>()
    var deleteSpecificationIds = ""
    //卖家公告
    var sellerNotice = ""
    var addition = ""
    var additionType = ""
    var additionList= ArrayList<AdditionalEntity.AdditionalSave>()

    //目标金额
    var supportMoney = ""

}