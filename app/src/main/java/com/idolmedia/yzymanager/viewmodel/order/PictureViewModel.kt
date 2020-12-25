package com.idolmedia.yzymanager.viewmodel.order

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseViewModel

/**
 *  时间：2020/12/2-11:16
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order PictureViewModel
 *  描述：
 */
class PictureViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()

    val strTitleCount = ObservableField<String>("×  0/0")

}