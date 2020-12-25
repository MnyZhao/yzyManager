package com.idolmedia.yzymanager.viewmodel.main

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.view.order.OrderManagerActivity

/**
 *  时间：2020/10/16-16:43
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.main ItemHomeNoticeBean
 *  描述：
 */
class ItemHomeOrderBean : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_home_order_manager
    }

    val strCountPay = ObservableField("0")
    val strCountSend = ObservableField("0")
    val strCountWK = ObservableField("0")
    val strCountRefund = ObservableField("0")

    init {

        strCountPay.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sender?.let {
                    if (it is ObservableField<*>){
                        Log.d("====订单数量变化","${it.get()}")
                    }
                }
            }
        })

    }

    fun onClickDetails(view:View){

    }

    fun onClickOrder(view:View,type:Int){
        val intent = Intent(view.context,OrderManagerActivity::class.java)
        intent.putExtra("current",type)
        view.context.startActivity(intent)
    }


}