package com.idolmedia.yzymanager.viewmodel.pop

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnPopSelectListener

/**
 *  时间：2020/10/19-14:25
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.pop PopSubjectMergeViewModel
 *  描述：
 */
class PopSubjectMergeViewModel(val onPopSelectListener: OnPopSelectListener) : BaseViewModel() {

    val mergeIndex = ObservableInt(0)

    fun onClickSelect(index:Int){
        onPopSelectListener.onSelect(index)
    }

}