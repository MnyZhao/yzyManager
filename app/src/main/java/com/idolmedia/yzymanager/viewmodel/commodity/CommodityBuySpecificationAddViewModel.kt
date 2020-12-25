package com.idolmedia.yzymanager.viewmodel.commodity

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.bean.SpecificationBean
import com.idolmedia.yzymanager.utils.ToastUtil

/**
 *  时间：2020/10/26-14:58
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityAddViewModel
 *  描述：
 */
class CommodityBuySpecificationAddViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()

    var isVipShop = 0
    var isEdit = false
    var cataFlag = 0
    var bean: SpecificationBean? = null
    var djSscId = ""

    fun getSpecificationBean():SpecificationBean?{

        adapter.get()?.let {

            if (bean==null){
                bean = SpecificationBean()
                bean?.cataFlag = "$cataFlag"
                bean?.saleNo = "0"
                bean?.djSscId = djSscId
            }

            for (item in it.getDate()){
                item as ItemCommodityAddBean
                if (item.strTitle.get() == "规格名称"){
                    if (item.strContent.get().isNullOrEmpty()){
                        ToastUtil.show("请设置规格名称")
                        return null
                    }else{
                        bean?.catalogTitle = item.strContent.get() ?: ""
                    }
                }
                else if (item.strTitle.get() == "库存数量"){
                    if (item.strContent.get().isNullOrEmpty()){
                        ToastUtil.show("请设置库存数量")
                        return null
                    }else{
                        bean?.surplusNo = item.strContent.get()?: "0"
                    }
                }
                else if (item.strTitle.get() == "是否限购"){
                    if (!item.isOpen.get()){
                        bean?.limitBuy = "0"
                    }
                }
                else if (item.strTitle.get() == "限购数量"){
                    if (item.strContent.get().isNullOrEmpty()){
                        ToastUtil.show("请设置限购数量")
                        return null
                    }else{
                        bean?.limitBuy = item.strContent.get()?: "0"
                    }

                }
                else if (item.strTitle.get() == "划线价格"){
                    bean?.originalPrice = item.strContent.get()?: "0"
                }
                else if (item.strTitle.get() == "普通价格"){
                    if (item.strContent.get().isNullOrEmpty()){
                        ToastUtil.show("请设置普通价格")
                        return null
                    }else{
                        bean?.currentPrice = item.strContent.get()?: "0"
                    }
                }
                else if (item.strTitle.get() == "会员价格"){
                    if (item.strContent.get().isNullOrEmpty()){
                        ToastUtil.show("请设置会员价格")
                        return null
                    }else{
                        if (isVipShop == 2){
                            bean?.vipPrice = item.strContent.get()?: "0"
                        }else{
                            bean?.currentPrice = item.strContent.get()?: "0"
                        }
                    }
                }
                else if (item.strTitle.get() == "分类图片" ){
                    if (item.imgRealPath.isNullOrEmpty()){
                        ToastUtil.show("请设置分类图片")
                        return null
                    }else{
                        bean?.catalogImg = item.imgRealPath
                    }
                }
            }

            if (bean?.sscId.isNullOrEmpty()){
                bean?.catalogEditType = "2"
            }else{
                bean?.catalogEditType = "1"
            }

            return bean

        }
        return null
    }

}