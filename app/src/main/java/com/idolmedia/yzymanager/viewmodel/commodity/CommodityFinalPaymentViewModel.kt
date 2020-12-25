package com.idolmedia.yzymanager.viewmodel.commodity

import android.content.Intent
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnFinalPaymentListener
import com.idolmedia.yzymanager.model.bean.SpecificationBean
import com.idolmedia.yzymanager.model.entity.CommoditySpecificationEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import java.lang.StringBuilder

/**
 *  时间：2020/10/26-11:26
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityAddFinalPaymentViewModel
 *  描述：
 */
class CommodityFinalPaymentViewModel : BaseViewModel()  {

    val adapter = ObservableField<BaseAdapter>()

    lateinit var listener:OnFinalPaymentListener

    var fromId = ""
    var shopId = ""
    var shopName = ""
    var isAudit = false
    var id = ""
    var isVipShop = 0
    /**定金规格的数量 数量为1可以随意增加尾款*/
    var reserveSize = 0

    var freightId = ""
    /**时间限制【0：有时间限制；1：无时间限制】*/
    var isLimitTime = false
    var startTime = ""
    var endTime = ""
    var pushType = 1
    var addition = ""
    /**商品详情里的尾款信息*/
    var categoryList = ArrayList<SpecificationBean>()
    val deleteIds = StringBuilder()

    fun querySpecification(){
        RetrofitHelper.instance().queryCommoditySpecification(isAudit,id,object : BaseObserver<CommoditySpecificationEntity>(){
            override fun onSuccess(t: CommoditySpecificationEntity) {
                if (t.resultCode==1){

                    adapter.get()?.let {
                        val current = it.getMaxPosition()
                        for(item in t.datas){
                            if (item.cataFlag == "1"){
                                reserveSize+=1
                            }
                            it.add(ItemFinalPaymentClassifyBean(item,isAudit,listener,isVipShop).apply {
                                strShopName.set("商家信息:${shopName}")
                                strShopId.set("商家ID:${shopId}")
                            })
                        }

                        it.notifyInserted(current)

                    }

                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    fun saveFinal():Intent?{
        if (freightId.isEmpty()){
            ToastUtil.show("请设置邮费模板")
            return null
        }

        if (isLimitTime){
            if (startTime.isEmpty()){
                ToastUtil.show("请设置尾款开始时间")
                return null
            }

            if (endTime.isEmpty()){
                ToastUtil.show("请设置尾款结束时间")
                return null
            }
        }else{
            if (startTime.isEmpty()){
                ToastUtil.show("请设置尾款开始时间")
                return null
            }
        }
        val intent = Intent()
        adapter.get()?.let {
            var count = 0
            val list = ArrayList<SpecificationBean?>()
            for(item in it.getDate()){
                if (item is ItemFinalPaymentClassifyBean){
                    count+=1
                    list.add(item.item)
                }
            }
            if (reserveSize==1 && count==1){
                ToastUtil.show("请为定金商品添加尾款")
                return null
            }
            if (reserveSize>1 && reserveSize*2!=count){
                ToastUtil.show("请为所有定金商品添加尾款")
                return null
            }

            intent.putExtra("catalogItems", Gson().toJson(list))
        }
        intent.putExtra("addition",addition)
        intent.putExtra("endTime",endTime)
        intent.putExtra("startTime",startTime)
        intent.putExtra("freightId",freightId)
        intent.putExtra("limitless",if (isLimitTime) 0 else 1)
        intent.putExtra("pushType",pushType)
        return intent
    }

    fun openFinal(observer: BaseObserver<BaseEntity>){
        if (freightId.isEmpty()){
            ToastUtil.show("请设置邮费模板")
            return
        }

        if (isLimitTime){
            if (startTime.isEmpty()){
                ToastUtil.show("请设置尾款开始时间")
                return
            }

            if (endTime.isEmpty()){
                ToastUtil.show("请设置尾款结束时间")
                return
            }
        }else{
            if (startTime.isEmpty()){
                ToastUtil.show("请设置尾款开始时间")
                return
            }
        }

        val map = HashMap<String,Any?>()

        adapter.get()?.let {

            var count = 0
            val list = ArrayList<SpecificationBean?>()
            for(item in it.getDate()){
                if (item is ItemFinalPaymentClassifyBean){
                    count+=1
                    list.add(item.item)
                }
            }

            if (reserveSize==1 && count==1){
                ToastUtil.show("请为定金商品添加尾款")
                return
            }

            if (reserveSize>1 && reserveSize*2!=count){
                ToastUtil.show("请为所有定金商品添加尾款")
                return
            }

            map["catalogItems"] = Gson().toJson(list)
        }

        map["addition"] = addition
        map["endTime"] = endTime
        map["startTime"] = startTime
        map["freightId"] = freightId
        map["limitless"] = if (isLimitTime) 0 else 1
        map["pushType"] = pushType
        map["reserveStatus"] = 2
        map["shopcommonId"] = id
        map["productCategoryDeleteIds"] = deleteIds.toString()

        RetrofitHelper.instance().openFinal(isAudit,map,observer)

    }

    fun sortCategory(category:ArrayList<SpecificationBean>){
        val djList = ArrayList<SpecificationBean>()
        val wkList = ArrayList<SpecificationBean>()
        val list = ArrayList<SpecificationBean>()
        for(item in category){
            if (item.cataFlag == "1"){
                djList.add(item)
            }else if(item.cataFlag == "2"){
                wkList.add(item)
            }
        }
        for(item in djList){
            list.add(item)
            for(wk in wkList){
                if (wk.djSscId == item.sscId){
                    list.add(wk)
                }
            }
        }
        categoryList = list
    }

}