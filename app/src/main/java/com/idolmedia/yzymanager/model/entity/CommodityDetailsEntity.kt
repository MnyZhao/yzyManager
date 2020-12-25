package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.model.bean.BuyNotesBean
import com.idolmedia.yzymanager.model.bean.SpecificationBean

/**
 *  时间：2020/4/6-15:40
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmanage.module_common.net.entity CommodityDetailsEntity
 *  描述：
 */
class CommodityDetailsEntity : BaseEntity() {

    var datas = Data()


    class Data{
        var activityType = ""
        var addition = ""
        var additionType = ""
        var androidVersion = ""
        var attestationType = ""
        var betweenPrice = ""
        var bookingPercentage = ""
        var bookingType = ""
        var businessModel = ""

        var createTime = ""
        var deliverAddress = ""
        var deliverType = ""
        var deliveryDate = ""
        var endTime = ""
        var expressMoney = ""
        var finalEndTime = ""
        var finalSaleNo = ""
        var finalStartTime = ""
        var fixedPriceType = ""
        var hasPaypal = ""
        var salesSecret = ""

        var imageUrl = ""
        var imageUrlList = ArrayList<String>()
        var isGift = ""
        var isHide = ""
        var isRecommend = ""
        var isStick = ""
        var isTimmingUp = 0
        var isVipShop = ""
        var issuingDate = ""
        var likeCount = ""
        var limitCount = ""
        var shopName = ""
        var shopDetails = ""
        var shopPritureList = ArrayList<String>()
        var shopType = ""
        var reserveStatus = ""
        var shopcommonId = ""
        var shoppingTo = ""
        var specificationList = ArrayList<Specification>()
        var startTime = ""
        var subName = ""
        var supportMoney = ""
        var upTime = ""
        var isAreaLimit = ""
        var updateTime = ""
        var virtualuserId = ""
        var tFreightVo = Freight()
        var catalogList = ArrayList<Catalog>()
        var idolList = ArrayList<IdoItem>()

        var goodsTypeId = ""
        var buyerGrade = 0
        var isProxies = 0
        var limitless = "0"
        var pushType = ""
        var freightId = ""
        var sellerNotice = ""

        var categoryList = ArrayList<SpecificationBean>()
        var aidouList = ArrayList<IdoAssociatedEntity.Ido>()
        var aboutArray = ArrayList<BuyNotesBean>()

        var timesTamp = ""

    }

    class Catalog{
        var barcode = ""
        var cataFlag = ""
        var catalogImg = ""
        var catalogTitle = ""
        var createTime = ""
        var currentPrice = ""
        var djSscId = ""
        var finalPrice = ""
        var isLockDelete = ""
        var limitBuy = ""
        var lockTime = ""
        var originalPrice = ""
        var productCount = ""
        var saleNo = ""
        var salesSecret = ""
        var shopcommonId = ""
        var sscId = ""
        var store = ""
        var surplusNo = ""
        var updateTime = ""
    }

    class IdoItem{
        var aliasName = ""
        var idoName = ""
        var idolId = ""
    }

    class Specification{
        var id = ""
        var name = ""
        var itemsList = ArrayList<SpecificationItem>()
    }

    class SpecificationItem{
        var id = ""
        var name = ""
        var itemsList = ArrayList<SpecificationItem>()
    }

    class Freight{
        var freightId = ""
        var name = ""
        var freightType = ""
    }

}