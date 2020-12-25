package com.idolmedia.yzymanager.viewmodel.freight

import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.bean.FreightBean
import com.idolmedia.yzymanager.model.entity.FreightListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil

/**
 *  时间：2020/10/26-18:24
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.freight FreightAddViewModel
 *  描述：
 */
class FreightAddViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()

    var isOverseas = false
    var isMergeExpress = "0"
    var bean : FreightListEntity.FreightItem? = null

    fun canSave():Boolean{
        adapter.get()?.let {
            var areaIndex = 0
            for(item in it.getDate()){
                if (item is ItemFreightAddHeadBean){

                    if (item.strName.get().isNullOrEmpty()){
                        ToastUtil.show("请设置模板名称")
                        return false
                    }
                    if (item.strFirstCount.get().isNullOrEmpty()){
                        ToastUtil.show("请设置首件数量")
                        return false
                    }
                    if (item.strFirstPrice.get().isNullOrEmpty()){
                        ToastUtil.show("请设置首件价格")
                        return false
                    }
                    if (item.strSubCount.get().isNullOrEmpty()){
                        ToastUtil.show("请设置续件数量")
                        return false
                    }
                    if (item.strSubPrice.get().isNullOrEmpty()){
                        ToastUtil.show("请设置续件价格")
                        return false
                    }
                }
                else if(item is ItemFreightAddAreaBean){
                    if (item.layoutId == R.layout.item_freight_add_area){
                        areaIndex+=1
                        if (item.bean.firstPart.isEmpty()){
                            ToastUtil.show("请为第${areaIndex}条指定区域设置计价方式")
                            return false
                        }
                    }
                }
            }
            return true
        }
        return false
    }

    fun saveFreight(observer: BaseObserver<BaseEntity>){

        adapter.get()?.let {
            var name = ""
            val list = ArrayList<FreightBean>()
            for(item in it.getDate()){
                if (item is ItemFreightAddHeadBean){
                    name = item.strName.get() ?: ""
                    val freightBean = FreightBean()
                    freightBean.firstPart = item.strFirstCount.get() ?: "0"
                    freightBean.firstPrice = item.strFirstPrice.get() ?: "0"
                    freightBean.continuePart = item.strSubCount.get() ?: "0"
                    freightBean.continuePrice = item.strSubPrice.get() ?: "0"
                    list.add(freightBean)
                }
                else if(item is ItemFreightAddAreaBean){
                    if (item.layoutId == R.layout.item_freight_add_area){
                        item.bean.isDefault = 0
                        list.add(item.bean)
                    }
                }
            }

            val map = HashMap<String,Any?>()
            map["freightId"] = bean?.freightId?: ""
            map["freightItems"] = Gson().toJson(list)
            map["name"] = name
            map["isMergeExpress"] = isMergeExpress
            map["modityType"] = bean==null
            map["isOverseas"] = isOverseas
            map["fromId"] = SpManager.getUserEntity()?.datas?.userId ?: ""
            RetrofitHelper.instance().saveFreightTemplate(map, observer)
        }

    }

}