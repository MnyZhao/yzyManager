package com.idolmedia.yzymanager.viewmodel.pop

import android.view.ViewGroup
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.viewmodel.order.OrderManagerViewModel

/**
 *  时间：2020/10/20-16:09
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.pop PopOrderFilterTypeViewModel
 *  描述：
 */
class PopOrderFilterTypeViewModel(val pop: BasePopWindow,val parentModel:OrderManagerViewModel) : BaseViewModel() {

    val typeIndex = ObservableInt(0)

    init {
        pop.width = ViewGroup.LayoutParams.WRAP_CONTENT

        when(parentModel.strFilterType.get()){
            "商品ID" -> typeIndex.set(0)
            "商品名称" -> typeIndex.set(1)
            "用户ID" -> typeIndex.set(2)
            "用户昵称" -> typeIndex.set(3)
            "用户账号" -> typeIndex.set(4)
            "收货人电话" -> typeIndex.set(5)
            "收货人姓名" -> typeIndex.set(6)
            "订单编号" -> typeIndex.set(7)
            "快递单号" -> typeIndex.set(8)
            "合并邮费专题ID" -> typeIndex.set(9)
            else -> typeIndex.set(10)
        }

    }

    fun onClickType(type:Int){
        val str = when(type){
            0 -> "商品ID"
            1 -> "商品名称"
            2 -> "用户ID"
            3 -> "用户昵称"
            4 -> "用户账号"
            5 -> "收货人电话"
            6 -> "收货人姓名"
            7 -> "订单编号"
            8 -> "快递单号"
            9 -> "合并邮费专题ID"
            10 -> "付款订单编号"
            else -> ""
        }

        parentModel.strFilterType.set(str)
        parentModel.filterType = type
        pop.dismiss()
    }

}