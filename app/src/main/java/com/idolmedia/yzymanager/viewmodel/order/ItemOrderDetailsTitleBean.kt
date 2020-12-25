package com.idolmedia.yzymanager.viewmodel.order

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.view.order.OrderAdditionalEditActivity
import com.idolmedia.yzymanager.view.order.OrderConsigneeEditActivity
import com.idolmedia.yzymanager.view.order.OrderLogisticsEditActivity

/**
 *  时间：2020/10/20-18:07
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order ItemOrderDetailsExpressBean
 *  描述：
 */
class ItemOrderDetailsTitleBean() : BaseBean() {

    constructor(title:String):this(){
        strTitle.set(title)
    }

    constructor(title:String,edit:Int):this(title){
        visibleEdit.set(edit)
    }

    override fun getViewType(): Int {
        return R.layout.item_order_details_title
    }

    val strTitle = ObservableField<String>()
    val visibleEdit = ObservableInt(View.GONE)

    var orderNum = ""
    var json = ""

    fun onClickEdit(view:View){
        if (strTitle.get() == "附加信息"){
            val intent = Intent(view.context,OrderAdditionalEditActivity::class.java)
            intent.putExtra("orderNum",orderNum)
            (view.context as Activity).startActivityForResult(intent,0)
        }
        else if(strTitle.get() == "物流信息"){
            val intent = Intent(view.context,OrderLogisticsEditActivity::class.java)
            intent.putExtra("orderNum",orderNum)
            intent.putExtra("json",json)
            (view.context as Activity).startActivityForResult(intent,0)
        }
        else if(strTitle.get() == "收货人信息"){
            val intent = Intent(view.context, OrderConsigneeEditActivity::class.java)
            intent.putExtra("orderNum",orderNum)
            intent.putExtra("json",json)
            (view.context as Activity).startActivityForResult(intent,0)
        }
    }

}