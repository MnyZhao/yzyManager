package com.idolmedia.yzymanager.viewmodel.order

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean

/**
 *  时间：2020/10/20-18:07
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order ItemOrderDetailsExpressBean
 *  描述：
 */
class ItemOrderAddressBean(content:String) : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_order_address
    }

    val strContent = ObservableField(content)

}