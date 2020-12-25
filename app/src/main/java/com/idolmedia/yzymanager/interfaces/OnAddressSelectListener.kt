package com.idolmedia.yzymanager.interfaces

import com.idolmedia.yzymanager.base.BaseBean

interface OnAddressSelectListener {

    fun onAddressSelect(position:Int,isSelect:Boolean,bean: BaseBean)

}