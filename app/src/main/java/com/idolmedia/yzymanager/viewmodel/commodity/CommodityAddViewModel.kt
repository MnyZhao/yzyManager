package com.idolmedia.yzymanager.viewmodel.commodity

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.text.InputType
import android.util.Log
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnItemSwitchListener
import com.idolmedia.yzymanager.model.bean.BuyNotesBean
import com.idolmedia.yzymanager.model.bean.BuyNotesNoIdBean
import com.idolmedia.yzymanager.model.bean.CommodityPublishBean
import com.idolmedia.yzymanager.model.bean.SpecificationBean
import com.idolmedia.yzymanager.model.entity.CommodityDetailsEntity
import com.idolmedia.yzymanager.model.entity.CommodityPreconditionEntity
import com.idolmedia.yzymanager.model.entity.UploadImageEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.Dp2PxUtils
import com.idolmedia.yzymanager.utils.PhotoUtils
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.common.ItemLineBean
import com.idolmedia.yzymanager.widget.CustomLoading
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import java.lang.StringBuilder

/**
 *  时间：2020/10/26-14:58
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityAddViewModel
 *  描述：
 */
class CommodityAddViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()
    val commodityPublishBean = CommodityPublishBean()
    var cpEntity:CommodityPreconditionEntity? = null

    var commodityId = ""
    /**编辑商品时的时间 由商品详情接口获得*/
    var timesTamp = ""
    var fromId = ""
    var isAudit = false

    /**发布前前置数据*/
    fun queryCommodityPrecondition(){

        RetrofitHelper.instance().queryCommodityPrecondition(object : BaseObserver<CommodityPreconditionEntity>(){
            override fun onSuccess(t: CommodityPreconditionEntity) {
                if (t.resultCode==1){
                    cpEntity = t
                }
            }

            override fun onError(msg: String) {
            }

        })
    }

    fun queryCommodityDetails(observer:BaseObserver<CommodityDetailsEntity>){

        RetrofitHelper.instance().queryCommodityPrecondition(object : BaseObserver<CommodityPreconditionEntity>(){
            override fun onSuccess(t: CommodityPreconditionEntity) {
                if (t.resultCode==1){
                    cpEntity = t

                    RetrofitHelper.instance().commodityDetails(commodityId,isAudit,observer)

                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
                finishRefresh.set(finishRefresh.get()+1)
            }

        })

    }

    /**发布商品*/
    fun saveCommodity(context: Context,observer: BaseObserver<BaseEntity>){
        if (commodityPublishBean.shopType.isEmpty()){
            ToastUtil.show("请选择商品分类")
            return
        }

        if (commodityPublishBean.goodsTypeId.isEmpty()){
            ToastUtil.show("请选择一级分类")
            return
        }
        if (commodityPublishBean.buyerGrade<0){
            ToastUtil.show("请选择身份限购")
            return
        }
        if (commodityPublishBean.isTimmingUp==1 && commodityPublishBean.upTime.isEmpty()){
            ToastUtil.show("请设置定时上架时间")
            return
        }
        if (commodityPublishBean.isAreaLimit.isEmpty()){
            ToastUtil.show("请设置地区限制")
            return
        }
        if (commodityPublishBean.startTime.isEmpty()){
            ToastUtil.show("请设置活动开始时间")
            return
        }
        if (commodityPublishBean.isLimitTime && commodityPublishBean.endTime.isEmpty()){
            ToastUtil.show("请设置活动结束时间")
            return
        }
        if ((commodityPublishBean.shopType != "support"||commodityPublishBean.shopType != "reserve2") && commodityPublishBean.freightId.isEmpty()){
            ToastUtil.show("请设置邮费模板")
            return
        }

        adapter.get()?.let {

            for(item in it.getDate()){
                if (item is ItemCommodityAddBean){
                    if (item.strTitle.get() == "商品名称"){
                        if (item.strContent.get().isNullOrEmpty()){
                            ToastUtil.show("请设置商品名称")
                            return
                        }else{
                            commodityPublishBean.shopName = item.strContent.get() ?: ""
                        }
                    }
                    else if (item.strTitle.get() == "会员属性"){
                        if (item.strContent.get().isNullOrEmpty()){
                            ToastUtil.show("请设置会员属性")
                            return
                        }
                    }
                    else if (item.strTitle.get() == "目标金额"){
                        if (item.strContent.get().isNullOrEmpty()){
                            ToastUtil.show("请设置目标金额")
                            return
                        } else {
                            commodityPublishBean.supportMoney = item.strContent.get()?: ""
                        }
                    }
                    else if (item.strTitle.get() == "商品图片"){
                        if (item.imgRealPath.isEmpty()){
                            ToastUtil.show("请设置商品主图")
                            return
                        }else{
                            val sb = StringBuilder()
                            if (!item.viceFirst.get()?.imgRealPath.isNullOrEmpty()){
                                if (sb.toString().isEmpty()){
                                    sb.append(item.viceFirst.get()?.imgRealPath)
                                }else{
                                    sb.append(","+item.viceFirst.get()?.imgRealPath)
                                }
                            }
                            if (!item.viceSecond.get()?.imgRealPath.isNullOrEmpty()){
                                if (sb.toString().isEmpty()){
                                    sb.append(item.viceSecond.get()?.imgRealPath)
                                }else{
                                    sb.append(","+item.viceSecond.get()?.imgRealPath)
                                }
                            }
                            if (!item.viceThird.get()?.imgRealPath.isNullOrEmpty()){
                                if (sb.toString().isEmpty()){
                                    sb.append(item.viceThird.get()?.imgRealPath)
                                }else{
                                    sb.append(","+item.viceThird.get()?.imgRealPath)
                                }
                            }
                            if (!item.viceFourth.get()?.imgRealPath.isNullOrEmpty()){
                                if (sb.toString().isEmpty()){
                                    sb.append(item.viceFourth.get()?.imgRealPath)
                                }else{
                                    sb.append(","+item.viceFourth.get()?.imgRealPath)
                                }
                            }
                            commodityPublishBean.subImageArray = sb.toString()
                            commodityPublishBean.imageUrl = item.imgRealPath
                        }
                    }
                    else if(item.strTitle.get()?.contains("发行时间") == true){
                        commodityPublishBean.issuingDate = item.strContent.get() ?:""
                    }
                    else if(item.strTitle.get()?.contains("配送时间") == true){
                        commodityPublishBean.deliveryDate = item.strContent.get() ?:""
                    }
                    else if(item.strTitle.get() == "海外直邮"){
                        commodityPublishBean.shoppingTo = if (item.isOpen.get()) "2" else "1"
                    }
                    else if(item.strTitle.get() == "是否推送"){
                        commodityPublishBean.pushType = if (item.isOpen.get()) "1" else "0"
                    }
                }
            }

            if (commodityPublishBean.catalogItems.isNullOrEmpty()){
                ToastUtil.show("请添加至少一个规格")
                return
            }

            CustomLoading.getInstance().showLoad(context)

            val map = HashMap<String,Any?>()
            map["shopType"] = commodityPublishBean.shopType
            map["reserveStatus"] = if (commodityPublishBean.shopType == "reserve2") 1 else 3
            map["shopDetails"] = commodityPublishBean.shopDetails
            map["isVipShop"] = commodityPublishBean.isVipShop
            map["goodsTypeId"] = commodityPublishBean.goodsTypeId
            map["buyerGrade"] = commodityPublishBean.buyerGrade
            map["isProxies"] = commodityPublishBean.isProxies
            map["shopName"] = commodityPublishBean.shopName
            map["imageUrl"] = commodityPublishBean.imageUrl
            map["subImageArray"] = commodityPublishBean.subImageArray
            map["catalogItems"] = Gson().toJson(commodityPublishBean.catalogItems)
            map["isTimmingUp"] = commodityPublishBean.isTimmingUp
            map["upTime"] = commodityPublishBean.upTime
            map["isAreaLimit"] = commodityPublishBean.isAreaLimit
            map["limitless"] = if (commodityPublishBean.isLimitTime) "0" else "1"
            map["startTime"] = commodityPublishBean.startTime
            map["endTime"] = commodityPublishBean.endTime
            map["issuingDate"] = commodityPublishBean.issuingDate
            map["deliveryDate"] = commodityPublishBean.deliveryDate
            map["aidouIds"] = commodityPublishBean.aidouIds
            map["shoppingTo"] = commodityPublishBean.shoppingTo
            map["pushType"] = commodityPublishBean.pushType
            map["freightId"] = commodityPublishBean.freightId
            map["sellerNotice"] = commodityPublishBean.sellerNotice
            map["addition"] = commodityPublishBean.addition
            map["additionType"] = commodityPublishBean.additionType
            map["supportMoney"] = commodityPublishBean.supportMoney

            val list = ArrayList<BuyNotesNoIdBean>()
            for(item in commodityPublishBean.buyNotes){
                list.add(BuyNotesNoIdBean().apply {
                    aboutType = item.id
                    title = item.title
                    content = item.content
                })
            }
            if (list.isNotEmpty()){
                map["aboutArray"] = Gson().toJson(list)
            }

            map["modityType"] = true

            RetrofitHelper.instance().publishCommodity(map ,observer)

        }

    }

    /**编辑后的商品*/
    fun saveEditCommodity(context: Context,observer: BaseObserver<BaseEntity>){

        if (adapter.get()?.getDate().isNullOrEmpty()){
            return
        }

        if (commodityPublishBean.shopType.isEmpty()){
            ToastUtil.show("请选择商品分类")
            return
        }

        if (commodityPublishBean.goodsTypeId.isEmpty()){
            ToastUtil.show("请选择一级分类")
            return
        }
        if (commodityPublishBean.buyerGrade<0){
            ToastUtil.show("请选择身份限购")
            return
        }
        if (commodityPublishBean.isTimmingUp==1 && commodityPublishBean.upTime.isEmpty()){
            ToastUtil.show("请设置定时上架时间")
            return
        }
        if (commodityPublishBean.isAreaLimit.isEmpty()){
            ToastUtil.show("请设置地区限制")
            return
        }
        if (commodityPublishBean.startTime.isEmpty()){
            ToastUtil.show("请设置活动开始时间")
            return
        }
        if (commodityPublishBean.isLimitTime && commodityPublishBean.endTime.isEmpty()){
            ToastUtil.show("请设置活动结束时间")
            return
        }
        if (commodityPublishBean.shopType != "reserve2"){
            if (commodityPublishBean.freightId.isEmpty()){
                ToastUtil.show("请设置邮费模板")
                return
            }
        }else if(commodityPublishBean.shopType == "reserve2" && commodityPublishBean.reserveStatus == "2"){
            if (commodityPublishBean.freightId.isEmpty()){
                ToastUtil.show("请设置邮费模板")
                return
            }
        }

        adapter.get()?.let {

            for(item in it.getDate()){
                if (item is ItemCommodityAddBean){
                    if (item.strTitle.get() == "商品名称"){
                        if (item.strContent.get().isNullOrEmpty()){
                            ToastUtil.show("请设置商品名称")
                            return
                        }else{
                            commodityPublishBean.shopName = item.strContent.get() ?: ""
                        }
                    }
                    else if (item.strTitle.get() == "会员属性"){
                        if (item.strContent.get().isNullOrEmpty()){
                            ToastUtil.show("请设置会员属性")
                            return
                        }
                    }

                    else if (item.strTitle.get() == "目标金额"){
                        if (item.strContent.get().isNullOrEmpty()){
                            ToastUtil.show("请设置目标金额")
                            return
                        } else {
                            commodityPublishBean.supportMoney = item.strContent.get()?: ""
                        }
                    }

                    else if (item.strTitle.get() == "商品图片"){
                        if (item.imgRealPath.isEmpty()){
                            ToastUtil.show("请设置商品主图")
                            return
                        }else{
                            val sb = StringBuilder()
                            if (!item.viceFirst.get()?.imgRealPath.isNullOrEmpty()){
                                if (sb.toString().isEmpty()){
                                    sb.append(item.viceFirst.get()?.imgRealPath)
                                }else{
                                    sb.append(","+item.viceFirst.get()?.imgRealPath)
                                }
                            }
                            if (!item.viceSecond.get()?.imgRealPath.isNullOrEmpty()){
                                if (sb.toString().isEmpty()){
                                    sb.append(item.viceSecond.get()?.imgRealPath)
                                }else{
                                    sb.append(","+item.viceSecond.get()?.imgRealPath)
                                }
                            }
                            if (!item.viceThird.get()?.imgRealPath.isNullOrEmpty()){
                                if (sb.toString().isEmpty()){
                                    sb.append(item.viceThird.get()?.imgRealPath)
                                }else{
                                    sb.append(","+item.viceThird.get()?.imgRealPath)
                                }
                            }
                            if (!item.viceFourth.get()?.imgRealPath.isNullOrEmpty()){
                                if (sb.toString().isEmpty()){
                                    sb.append(item.viceFourth.get()?.imgRealPath)
                                }else{
                                    sb.append(","+item.viceFourth.get()?.imgRealPath)
                                }
                            }
                            commodityPublishBean.subImageArray = sb.toString()
                            commodityPublishBean.imageUrl = item.imgRealPath
                        }
                    }
                    else if(item.strTitle.get()?.contains("发行时间") == true){
                        commodityPublishBean.issuingDate = item.strContent.get() ?:""
                    }
                    else if(item.strTitle.get()?.contains("配送时间") == true){
                        commodityPublishBean.deliveryDate = item.strContent.get() ?:""
                    }
                    else if(item.strTitle.get() == "海外直邮"){
                        commodityPublishBean.shoppingTo = if (item.isOpen.get()) "2" else "1"
                    }
                    else if(item.strTitle.get() == "是否推送"){
                        commodityPublishBean.pushType = if (item.isOpen.get()) "1" else "0"
                    }

                }
            }

            CustomLoading.getInstance().showLoad(context)

            val map = HashMap<String,Any?>()
            map["shopType"] = commodityPublishBean.shopType
            map["shopDetails"] = commodityPublishBean.shopDetails
            map["isVipShop"] = commodityPublishBean.isVipShop

            if (commodityPublishBean.goodsTypeId.isEmpty() || commodityPublishBean.goodsTypeId == "-1"){

            }else{
                map["goodsTypeId"] = commodityPublishBean.goodsTypeId
            }
            map["buyerGrade"] = commodityPublishBean.buyerGrade
            map["isProxies"] = commodityPublishBean.isProxies
            map["shopName"] = commodityPublishBean.shopName
            map["imageUrl"] = commodityPublishBean.imageUrl
            map["subImageArray"] = commodityPublishBean.subImageArray
            map["catalogItems"] = Gson().toJson(commodityPublishBean.catalogItems)
            map["isTimmingUp"] = commodityPublishBean.isTimmingUp
            map["upTime"] = commodityPublishBean.upTime
            map["isAreaLimit"] = commodityPublishBean.isAreaLimit
            map["limitless"] = if (commodityPublishBean.isLimitTime) "0" else "1"
            map["startTime"] = commodityPublishBean.startTime
            map["endTime"] = commodityPublishBean.endTime
            map["issuingDate"] = commodityPublishBean.issuingDate
            map["deliveryDate"] = commodityPublishBean.deliveryDate
            map["aidouIds"] = commodityPublishBean.aidouIds
            map["shoppingTo"] = commodityPublishBean.shoppingTo
            map["pushType"] = commodityPublishBean.pushType
            map["freightId"] = if (commodityPublishBean.freightId.isEmpty()) "0" else commodityPublishBean.freightId
            map["sellerNotice"] = commodityPublishBean.sellerNotice
            map["addition"] = commodityPublishBean.addition
            map["additionType"] = commodityPublishBean.additionType
            map["productCategoryDeleteIds"] = commodityPublishBean.deleteSpecificationIds
            map["shopcommonId"] = commodityId

            val list = ArrayList<BuyNotesNoIdBean>()
            for(item in commodityPublishBean.buyNotes){
                val bean = BuyNotesNoIdBean()
                bean.aboutType = if (item.aboutType.isNullOrEmpty()) item.id else item.aboutType
                bean.title  = item.title
                bean.content = item.content
                list.add(bean)
            }
            if (list.isNotEmpty()){
                map["aboutArray"] = Gson().toJson(list)
            }

            map["modityType"] = false
            map["timesTamp"] = timesTamp

            RetrofitHelper.instance().saveCommodity(map,isAudit,observer)
        }

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if (resultCode == RESULT_OK && PhotoUtils.verifyImgRequestIsCode(requestCode)){
            val list = PictureSelector.obtainMultipleResult(data) as ArrayList<LocalMedia>
            adapter.get()?.let {
                for(item in it.getDate()){
                    if (item is ItemCommodityAddBean){
                        if (item.isImg && requestCode == PhotoUtils.CHOOSE_REQUEST){
                            item.upLoadImg(list[0].cutPath,this,object : BaseObserver<UploadImageEntity>(){
                                override fun onSuccess(t: UploadImageEntity) {
                                    if (t.resultCode==1){
                                        item.imgRealPath = t.imageUrl
                                        item.canDelete.set(true)
                                        item.strImg.set(item.imgRealPath)
                                        if (commodityPublishBean.shopType!="support"){
                                            item.viceFirst.get()?.path?.set(item.imgRealPath)
                                            item.viceFirst.get()?.imgRealPath = item.imgRealPath
                                            item.viceFirst.get()?.canDelete?.set(true)
                                        }
                                        commodityPublishBean.imageUrl = t.imageUrl
                                    }
                                }

                                override fun onError(msg: String) {
                                    ToastUtil.show(msg)
                                }

                            })
                            break
                        }
                        else if(item.isImg && requestCode == PhotoUtils.CHOOSE_REQUEST_FIRST){
                            item.viceFirst.get()?.setImgPath(list[0].cutPath)
                            break
                        }
                        else if(item.isImg && requestCode == PhotoUtils.CHOOSE_REQUEST_SECOND){
                            item.viceSecond.get()?.setImgPath(list[0].cutPath)
                            break
                        }
                        else if(item.isImg && requestCode == PhotoUtils.CHOOSE_REQUEST_THIRD){
                            item.viceThird.get()?.setImgPath(list[0].cutPath)
                            break
                        }
                        else if(item.isImg && requestCode == PhotoUtils.CHOOSE_REQUEST_FOURTH){
                            item.viceFourth.get()?.setImgPath(list[0].cutPath)
                            break
                        }
                    }
                }
            }
        }

        else if(resultCode == 1){
            adapter.get()?.let {
                for(item in it.getDate()){
                    if (item is ItemCommodityAddBean){
                        if ((requestCode == 1 && item.strTitle.get() == "商品分类")||
                            (requestCode == 2 && item.strTitle.get() == "会员属性")||
                            (requestCode == 3 && item.strTitle.get() == "一级分类")||
                            (requestCode == 4 && item.strTitle.get() == "身份限购")||
                            (requestCode == 5 && item.strTitle.get() == "售后客服")||
                            (requestCode == 6 && item.strTitle.get() == "地区限制")){
                            val status = data?.getStringExtra("status") ?: ""
                            item.strContent.set(status)

                            if (item.strTitle.get() == "商品分类"){
                                val shopType = data?.getStringExtra("shopType") ?: ""
                                commodityPublishBean.shopType = data?.getStringExtra("shopType") ?: ""

                                //发布定金商品没有邮费与附加信息选项
                                var current = -1
                                for((index,bean) in it.getDate().withIndex()){
                                    if (bean is ItemCommodityAddBean){
                                        if (bean.strTitle.get() == "关联爱豆"){
                                            current = index
                                            break
                                        }
                                    }
                                }
                                if (current>0){
                                    it.removeAllDateFromIndex(current+1)
                                }

                                commodityPublishBean.shoppingTo = "1"
                                if (shopType != "support"){
                                    if (SpManager.isOverseasSystem()){
                                        commodityPublishBean.shoppingTo = "2"
                                        it.add(ItemCommodityAddBean("海外直邮",true).apply {
                                            canEdit = false
                                        })
                                    }else{
                                        it.add(ItemCommodityAddBean("海外直邮",false))
                                    }
                                }
                                it.add(ItemCommodityAddBean("是否推送",false))

                                for(b in it.getDate()){
                                    if (b is ItemCommodityAddBean){
                                        if (b.strTitle.get() == "商品图片"){
                                            if (shopType == "support"){
                                                b.height.set(Dp2PxUtils.dip2px(180).toInt())
                                                b.strImgTip.set("(建议比例2:1)")
                                                if (b.cropW==1){
                                                    b.onClickDelete(b.imgView!!)
                                                }
                                                b.cropW = 2
                                                b.mainImgBg.set(R.mipmap.ic_img_add_admin_width)
                                            }else{
                                                b.height.set(Dp2PxUtils.dip2px(90).toInt())
                                                b.strImgTip.set("(建议比例1:1)")
                                                if (b.cropW==2){
                                                    b.onClickDelete(b.imgView!!)
                                                }
                                                b.cropW = 1
                                                b.mainImgBg.set(R.mipmap.ic_img_add_admin)
                                            }
                                            break
                                        }
                                    }
                                }

                                if (shopType == "reserve2"){
                                    if (SpManager.getUserIdentity() == "VIP_PRODUCT_MERCHANT"){
                                        val bean = adapter.get()?.getItem(2) as ItemCommodityAddBean
                                        bean.strContent.set("")
                                        commodityPublishBean.isVipShop = -1
                                    }
                                    it.add(ItemLineBean(12, R.color.bg_fa))
                                    it.add(ItemCommodityAddBean("卖家公告",if (commodityPublishBean.sellerNotice.isNullOrEmpty()) "" else "已编辑"))
                                    it.add(ItemCommodityAddBean("购买须知",if (commodityPublishBean.buyNotes.isNullOrEmpty()) "" else "已编辑"))
                                    commodityPublishBean.freightId = "0"
                                }
                                else if(shopType == "support"){
                                    it.add(ItemLineBean(12, R.color.bg_fa))
                                    it.add(ItemCommodityAddBean("目标金额","","请输入目标金额","元",
                                        InputType.TYPE_CLASS_NUMBER  or InputType.TYPE_NUMBER_FLAG_DECIMAL))
                                    it.add(ItemCommodityAddBean("附加信息",""))
                                    it.add(ItemCommodityAddBean("卖家公告",if (commodityPublishBean.sellerNotice.isNullOrEmpty()) "" else "已编辑"))
                                    it.add(ItemCommodityAddBean("购买须知",if (commodityPublishBean.buyNotes.isNullOrEmpty()) "" else "已编辑"))
                                    commodityPublishBean.freightId = "0"
                                }
                                else{
                                    it.add(ItemLineBean(12, R.color.bg_fa))
                                    it.add(ItemCommodityAddBean("是否包邮",false,object :
                                        OnItemSwitchListener {
                                        override fun onItemSwitch(position: Int, bean: BaseBean, switch: Boolean) {
                                            if (switch){
                                                it.notifyDelete(position+1)
                                                commodityPublishBean.freightId = "0"
                                            }else{
                                                it.add(position+1,ItemCommodityAddBean("邮费模板",""))
                                                it.notifyInserted(position+1)
                                                commodityPublishBean.freightId = ""
                                            }
                                        }
                                    }))
                                    it.add(ItemCommodityAddBean("邮费模板",""))

                                    it.add(ItemLineBean(12, R.color.bg_fa))

                                    if(shopType == "support" || shopType == "crowdfunding"){
                                        it.add(ItemCommodityAddBean("目标金额","","请输入目标金额","元",
                                            InputType.TYPE_CLASS_NUMBER  or InputType.TYPE_NUMBER_FLAG_DECIMAL))
                                    }

                                    it.add(ItemCommodityAddBean("附加信息",""))
                                    it.add(ItemCommodityAddBean("卖家公告",if (commodityPublishBean.sellerNotice.isNullOrEmpty()) "" else "已编辑"))
                                    it.add(ItemCommodityAddBean("购买须知",if (commodityPublishBean.buyNotes.isNullOrEmpty()) "" else "已编辑"))

                                    commodityPublishBean.freightId = ""
                                }

                                it.notifyInserted(current)
                            }
                            else if (item.strTitle.get() == "会员属性"){
                                val vipShop = data?.getIntExtra("isVipShop",-1) ?: -1
                                if (commodityPublishBean.isVipShop != vipShop){
                                    commodityPublishBean.isVipShop = vipShop
                                    commodityPublishBean.catalogItems.clear()
                                    for(bean in it.getDate()){
                                        if (bean is ItemCommodityAddBean){
                                            if (bean.strTitle.get() == "商品规格"){
                                                bean.strContent.set("")
                                                break
                                            }
                                        }
                                    }
                                }
                            }
                            else if (item.strTitle.get() == "一级分类"){
                                commodityPublishBean.goodsTypeId = data?.getStringExtra("goodsTypeId") ?: ""
                            }
                            else if (item.strTitle.get() == "身份限购"){
                                commodityPublishBean.buyerGrade = data?.getIntExtra("buyerGrade",-1) ?: -1
                            }
                            else if (item.strTitle.get() == "售后客服"){
                                commodityPublishBean.isProxies = data?.getIntExtra("isProxies",0) ?: 0
                            }
                            else if (item.strTitle.get() == "地区限制"){
                                commodityPublishBean.isAreaLimit = data?.getStringExtra("isAreaLimit") ?: ""
                            }
                            break
                        }
                        else if (requestCode == 7 && item.strTitle.get() == "活动时间"){
                            val isLimit = data?.getBooleanExtra("isLimit",false) ?: false
                            val startTime = data?.getStringExtra("startTime") ?: ""
                            val endTime = data?.getStringExtra("endTime") ?: ""
                            commodityPublishBean.isLimitTime = isLimit
                            commodityPublishBean.startTime = startTime
                            commodityPublishBean.endTime = endTime
                            item.strContent.set("已编辑")
                            break
                        }
                        else if (requestCode == 8 && item.strTitle.get() == "关联爱豆"){
                            val idoIds = data?.getStringExtra("idoIds") ?: ""
                            val idoNames = data?.getStringExtra("idoNames") ?: ""
                            val json = data?.getStringExtra("idoList") ?: ""
                            item.strContent.set(idoNames)
                            commodityPublishBean.aidouIds = idoIds
                            commodityPublishBean.idoList = json
                        }
                        else if (requestCode == 9 && item.strTitle.get() == "邮费模板"){
                            val freightId = data?.getStringExtra("freightId") ?: ""
                            val freightName = data?.getStringExtra("freightName") ?: ""
                            item.strContent.set(freightName)
                            commodityPublishBean.freightId = freightId
                        }
                        else if (requestCode == 10 && item.strTitle.get() == "附加信息"){
                            val json = data?.getStringExtra("json") ?: ""
                            val additionType = data?.getStringExtra("additionType") ?: ""
                            item.strContent.set("已编辑")
                            commodityPublishBean.addition = json
                            commodityPublishBean.additionType = additionType
                            Log.d("====additional",json)
                            Log.d("====additional",additionType)
                        }
                        else if (requestCode == 11 && item.strTitle.get() == "卖家公告"){
                            val sellerNotice = data?.getStringExtra("sellerNotice") ?: ""
                            commodityPublishBean.sellerNotice = sellerNotice
                            item.strContent.set("已编辑")
                        }
                        else if (requestCode == 12 && item.strTitle.get() == "购买须知"){
                            val json = data?.getStringExtra("json") ?: ""
                            val type = object : TypeToken<ArrayList<BuyNotesBean>>(){}.type
                            val list = Gson().fromJson<ArrayList<BuyNotesBean>>(json,type)
                            list?.let {
                                commodityPublishBean.buyNotes = it
                            }
                            item.strContent.set("已编辑")
                        }
                        else if (requestCode == 13 && item.strTitle.get() == "商品规格"){
                            val json = data?.getStringExtra("json") ?: ""
                            val type = object : TypeToken<ArrayList<SpecificationBean>>(){}.type
                            commodityPublishBean.catalogItems.clear()
                            commodityPublishBean.catalogItems.addAll(Gson().fromJson(json,type))
                            commodityPublishBean.deleteSpecificationIds = data?.getStringExtra("deleteIds") ?: ""
                            item.strContent.set("已编辑")
                        }
                        else if (requestCode == 14 && item.strTitle.get() == "商品详情"){
                            val info = data?.getStringExtra("info") ?: ""
                            commodityPublishBean.shopDetails = info
                            item.strContent.set("已编辑")
                        }
                        else if (requestCode == 15 && item.strTitle.get() == "尾款信息"){
                            val json = data?.getStringExtra("catalogItems")
                            val addition = data?.getStringExtra("addition") ?: ""
                            val finalEndTime = data?.getStringExtra("endTime") ?: ""
                            val finalStartTime = data?.getStringExtra("startTime") ?: ""
                            val limitless = data?.getIntExtra("limitless",0) ?: 0
                            val freightId = data?.getStringExtra("freightId") ?: ""
                            val pushType = data?.getStringExtra("pushType") ?: 1

                            val type = object:TypeToken<ArrayList<SpecificationBean>>(){}.type
                            val catalogItems = Gson().fromJson<ArrayList<SpecificationBean>>(json,type)
                            commodityPublishBean.catalogItems = catalogItems
                            commodityPublishBean.isLimitTime = limitless == 0
                            commodityPublishBean.finalStartTime = finalStartTime
                            commodityPublishBean.finalEndTime = finalEndTime
                            commodityPublishBean.freightId = freightId
                            commodityPublishBean.pushType = pushType.toString()
                            commodityPublishBean.addition = addition

                            item.strContent.set("已编辑")
                        }
                    }
                }
            }
        }
    }

}