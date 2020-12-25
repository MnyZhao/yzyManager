package com.idolmedia.yzymanager.viewmodel.pop

import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnPopItemSelectListener

/**
 *  时间：2020/10/20-16:09
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.pop PopOrderFilterTypeViewModel
 *  描述：
 */
class PopCommodityViewModel(val pop: BasePopWindow,val listener: OnPopItemSelectListener?) : BaseViewModel() {

    val typeIndex = ObservableInt(0)

    init {
        pop.width = ViewGroup.LayoutParams.MATCH_PARENT

        pop.isOutsideTouchable = false
        pop.isFocusable = false

    }

    fun onClickType(type:Int){
        listener?.onPopItemSelect(type,"")
    }

}