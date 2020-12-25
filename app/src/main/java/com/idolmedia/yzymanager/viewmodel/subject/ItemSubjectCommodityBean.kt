package com.idolmedia.yzymanager.viewmodel.subject

import android.content.Context
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.databinding.ItemSubjectCommodityBinding
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.model.entity.CommodityListEntity

/**
 *  时间：2020/10/21-14:39
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.subject ItemSubjectCommodityBean
 *  描述：
 */
class ItemSubjectCommodityBean(val item:CommodityListEntity.Data) : BaseBean() {

    constructor(item:CommodityListEntity.Data,onItemSelectListener: OnItemSelectListener):this(item){
        this.isSelect = true
        this.onItemSelectListener = onItemSelectListener
    }

    override fun getViewType(): Int {
        return getLayoutId()
    }

    val strImg = ObservableField<String>()
    val strName = ObservableField<String>()
    val strId = ObservableField<String>()
    val isCheck = ObservableBoolean()
    var isSelect = false


    var onItemSelectListener: OnItemSelectListener? = null

    init {

        strImg.set(item.imageUrl)
        strId.set("商品ID:${item.shopcommonId}")
        strName.set(item.shopName)

    }

    fun onClickSelect(view:View){
        isCheck.set(!isCheck.get())
        onItemSelectListener?.onItemSelect(position,this)
    }

    private fun getLayoutId():Int{
        if (isSelect) return R.layout.item_subject_commodity_select
        return R.layout.item_subject_commodity
    }

    override fun onBindViewModel(context: Context, binding: ViewDataBinding?, position: Int) {
        super.onBindViewModel(context, binding, position)
        if (binding is ItemSubjectCommodityBinding){
        }
    }

}