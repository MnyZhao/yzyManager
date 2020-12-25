package com.idolmedia.yzymanager.viewmodel.order

import android.view.View
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.model.entity.OrderDetailsEntity
import com.idolmedia.yzymanager.utils.CopyUtil

/**
 *  时间：2020/10/20-18:07
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order ItemOrderDetailsExpressBean
 *  描述：
 */
class ItemOrderDetailsFreightBean(val item:OrderDetailsEntity.Express) : BaseBean() {

    override fun getViewType(): Int {
        return getLayoutId()
    }

    fun getLayoutId():Int{
        return if (isEdit) R.layout.item_order_details_freight_edit else R.layout.item_order_details_freight
    }

    val strName = ObservableField<String>()
    val strNameShort = ObservableField<String>()
    val strNumber = ObservableField<String>()
    var isEdit = false

    init {

        strName.set(item.shippername)
        strNameShort.set(item.shippercode)
        strNumber.set(item.waybillNo)

    }


    fun onClickCopy(view:View){
        CopyUtil.copy(view.context,item.waybillNo)
    }

}