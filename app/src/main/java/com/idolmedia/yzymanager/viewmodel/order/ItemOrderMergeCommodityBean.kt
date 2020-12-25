package com.idolmedia.yzymanager.viewmodel.order

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.model.entity.OrderListEntity

/**
 *  时间：2020/11/17-16:13
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order ItemOrderMergeCommodityBean
 *  描述：
 */
class ItemOrderMergeCommodityBean(item:OrderListEntity.Product) : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_order_merge_commodity
    }

    val strImg = ObservableField(item.imageUrl)

}