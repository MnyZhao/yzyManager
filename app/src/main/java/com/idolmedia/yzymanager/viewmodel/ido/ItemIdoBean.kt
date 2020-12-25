package com.idolmedia.yzymanager.viewmodel.ido

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.model.entity.IdoAssociatedEntity

/**
 *  时间：2020/10/29-13:40
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.ido ItemIdoBean
 *  描述：
 */
class ItemIdoBean(val ido:IdoAssociatedEntity.Ido) : BaseBean() {

    constructor(selectListener: OnItemSelectListener,ido:IdoAssociatedEntity.Ido):this(ido){
        layoutId = R.layout.item_ido_associate
        this.selectListener = selectListener
    }

    constructor(deleteListener:OnItemDeleteListener,ido:IdoAssociatedEntity.Ido):this(ido){
        layoutId = R.layout.item_ido_associate_pop
        this.deleteListener = deleteListener
    }

    var layoutId = R.layout.item_ido_associate
    override fun getViewType(): Int {
       return layoutId
    }

    val strImg = ObservableField<String>()
    val strName = ObservableField<String>()
    val strFans = ObservableField<String>()
    val isSelect = ObservableBoolean()

    var deleteListener:OnItemDeleteListener? = null
    var selectListener: OnItemSelectListener? = null

    init {

        strImg.set(ido.idoHeadImg)
        strName.set(ido.idoName)
        strFans.set("粉丝：${ido.fansCount}")

    }

    fun onClickSelect(view:View){
        isSelect.set(!isSelect.get())
        selectListener?.onItemSelect(position,this)
    }

    fun onClickDelete(view:View){
        deleteListener?.onItemDelete(position,this)
    }

}