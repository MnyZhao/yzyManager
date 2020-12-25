package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

class CommodityListEntity : BaseEntity() {

    var currentTime = ""
    var datas = ArrayList<Data>()

    class Data{

        var currentTime = ""

        var addition = ""
        var additionType = ""
        var attestationType = ""
        var betweenPrice = ""
        var createTime = ""
        var deliveryDate = ""
        var endTime = ""
        var expressMoney = ""
        var freightId = ""
        var goodsTypeId = ""
        var imageUrl = ""
        var isTimmingUp = ""
        var isVipShop = ""
        var issuingDate = ""
        var putaway = ""
        var salesSecret = ""
        var shopName = ""
        var shopType = ""
        var shopcommonId = ""
        var startTime = ""
        var supportMoney = ""
        var upTime = ""
        var virtualuserId = ""

        var nickName = ""
        var vipPrice = ""
        /**活动区分类(1:限量;2:普通)*/
        var activityType = ""
        /**0：为不置顶；1：置顶*/
        var isStick = ""
        /**发货地(1：国内；2：国外)*/
        var shoppingTo = ""
        /**尾款销量*/
        var finalSaleNo = ""
        /**商品规格分类中的假销量之和(尾款)*/
        var fFinalSaleNo = ""
        /**假销量对应金额(尾款和非尾款都放在这里)*/
        var fSaleMoney = ""
        /**锁定订单修改地址功能：0-不锁定;1-锁定*/
        var lockOrderAddress = ""
        /**锁定订单地址操作者（0：用户操作；1：官方操作）*/
        var lockOrderOperator = ""
        /**锁定订单附加信息修改功能：0-不锁定;1-锁定*/
        var lockOrderAddition = ""
        /**状态(1：定金；2：尾款；3：全款)*/
        var reserveStatus = ""
        /**尾款开始时间*/
        var finalStartTime = ""
        var finalEndTime = ""
        /**0：为不隐藏；1：隐藏	*/
        var isHide = ""
        /**商品限购数量设置*/
        var limitCount = ""

        var refunds = ""
        var refundsReason = ""
        var refundsTips = ""

        /**
         * 是否有地区限制；
         * 0-中国大陆；
         * 1-港澳台+国外；
         * 2-全球；
         * 3-港澳台；
         * 4-港澳台+中国大陆；
         * 5-国外 ；
         * 6-中国大陆+国外
         */
        var isAreaLimit = ""
        /**是否合并计算运费；0-否；1-是*/
        var isMergeExpress = ""
        var subjectId = ""
        /**收藏数量*/
        var collectionCount = ""
        /**时间限制【0：有时间限制；1：无时间限制】*/
        var limitless = ""
        /**限购次数*/
        var limitTimes = ""
        /**
         * 众筹状态
         * 0-众筹中；
         * 1-自然众筹成功(达到目标金额)；
         * 2-自然众筹失败(未达到目标金额)；
         * 3-取消项目(订单状态为待退款)；
         * 4-众筹生产中(此状态必须为众筹成功，真、假销量都可以；订单状态为待发货)；
         * 5-确认退款
         */
        var isCrowdfundingStatus = ""
        /**销量*/
        var saleNoStr = ""
        var saleNo = ""
        /**审核状态0-待审核；1-审核成功；2-审核失败、审核被拒；3-撤销审核；4-审核中，管理员正在审核页面停留审核商品，此时商品不可编辑*/
        var checkStatus = ""
        var checkReason = ""

        var timesTamp = ""
        var sortNo = ""
        var overseas = ""

        var mixtureId = ""
        /**是否正在开启尾款你的审核中状态*/
        var isEdit = false
        //控制跳转页面的类型，尾款商品分类审核尾款和尾款阶段商品审核，审核尾款跳转到尾款信息页-1；尾款阶段时跳转到定金商品和尾款信息页-2
        var toViewType= ""
        var showBuyhistory = ""

    }

}