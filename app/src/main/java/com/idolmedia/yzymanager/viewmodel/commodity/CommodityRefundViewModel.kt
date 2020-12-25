package com.idolmedia.yzymanager.viewmodel.commodity

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.base.BaseViewModel

/**
 *  时间：2020/11/11-17:14
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityRefundViewModel
 *  描述：
 */
class CommodityRefundViewModel : BaseViewModel() {

    val strTitle = ObservableField<String>()
    val strContent = ObservableField<String>()

    val switchBean = ObservableField<ItemCommodityAddBean>()
    val visibleContent = ObservableInt()

}