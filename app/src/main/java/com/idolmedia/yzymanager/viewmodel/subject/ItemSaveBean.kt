package com.idolmedia.yzymanager.viewmodel.subject

import android.view.View
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean

/**
 *  时间：2020/11/23-16:11
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.subject ItemSaveBean
 *  描述：
 */
class ItemSaveBean(val onClickListener: View.OnClickListener) : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_save
    }

    fun onClickSave(view:View){
        onClickListener.onClick(view)
    }

}