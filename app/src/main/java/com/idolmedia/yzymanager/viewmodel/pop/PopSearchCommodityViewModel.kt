package com.idolmedia.yzymanager.viewmodel.pop

import android.view.ViewGroup
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnPopSelectListener

/**
 *  时间：2020/10/20-16:09
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.pop PopOrderFilterTypeViewModel
 *  描述：
 */
class PopSearchCommodityViewModel(val pop: BasePopWindow) : BaseViewModel() {

    val typeIndex = ObservableInt(0)

    var onSelectListener: OnPopSelectListener? = null

    init {
        pop.width = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    fun onClickType(type:Int){
        typeIndex.set(type)
        onSelectListener?.onSelect(type)
        pop.dismiss()
    }

}