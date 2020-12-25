package com.idolmedia.yzymanager.viewmodel.order

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean

/**
 *  时间：2020/12/7-17:27
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order ItemOrderTotalBean
 *  描述：
 */
class ItemOrderTotalBean : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_order_total
    }

    val strTotalMoney = ObservableField<String>()
    val strTotalFreightMoney = ObservableField<String>()
    val strTotalCount = ObservableField<String>()

}