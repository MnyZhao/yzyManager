package com.idolmedia.yzymanager.viewmodel.commodity

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseViewModel

/**
 *  时间：2020/10/26-14:58
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityAddViewModel
 *  描述：
 */
class CommodityBuySpecificationViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()

    var isVipShop = 0
    var isEdit = false

}