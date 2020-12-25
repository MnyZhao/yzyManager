package com.idolmedia.yzymanager.viewmodel.additional

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.entity.AdditionalEntity

/**
 *  时间：2020/10/27-10:56
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityAdditionalViewModel
 *  描述：
 */
class AdditionalAddViewModel : BaseViewModel() {

    val strName = ObservableField<String>()
    val strHint = ObservableField<String>()
    val isMandatory = ObservableBoolean()
    val isEdit = ObservableBoolean(true)

    var bean: AdditionalEntity.AdditionalSave? = null

    fun onClickMandatory(view:View){
        isMandatory.set(!isMandatory.get())
    }

    fun onClickEdit(view:View){
        isEdit.set(!isEdit.get())
    }

}