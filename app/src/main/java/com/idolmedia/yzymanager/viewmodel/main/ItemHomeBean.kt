package com.idolmedia.yzymanager.viewmodel.main

import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.view.commodity.*
import com.idolmedia.yzymanager.view.freight.FreightAddActivity
import com.idolmedia.yzymanager.view.freight.FreightListActivity
import com.idolmedia.yzymanager.view.freight.FreightSubjectListActivity
import com.idolmedia.yzymanager.view.order.OrderManagerActivity
import com.idolmedia.yzymanager.view.subject.SubjectAddActivity
import com.idolmedia.yzymanager.view.subject.SubjectManagerActivity

/**
 *  时间：2020/10/16-16:43
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.main ItemHomeNoticeBean
 *  描述：
 */
class ItemHomeBean(var layoutId:Int) : BaseBean() {
    override fun getViewType(): Int {
        return layoutId
    }

    val strContent = ObservableField<String>()
    val strCountPay = ObservableField("0")
    val strCountSend = ObservableField("0")
    val strCountWK = ObservableField("0")
    val strCountRefund = ObservableField("0")

    val strAudit = ObservableField<String>()
    val strCommodityAdd = ObservableField<String>()
    val visibleCommodityAdd = ObservableInt(View.VISIBLE)

    val visibleAudit = ObservableInt(View.GONE)
    val strAuditCount = ObservableField<String>()

    fun onClickDetails(view:View){

    }

    fun onClickSubjectManager(view:View){
        view.context.startActivity(Intent(view.context,SubjectManagerActivity::class.java))
    }

    fun onClickSubjectEdit(view:View){
        view.context.startActivity(Intent(view.context, SubjectAddActivity::class.java))
    }

    fun onClickOrder(view:View,type:Int){
        val intent = Intent(view.context,OrderManagerActivity::class.java)
        intent.putExtra("current",type)
        view.context.startActivity(intent)
    }

    fun onClickCommodityManager(view: View,putaway:Int){
        val intent = Intent(view.context,CommodityManagerActivity::class.java)
        intent.putExtra("putaway",putaway)
        view.context.startActivity(intent)
    }

    fun onClickAudit(view: View){
        if (SpManager.getUserIdentity() == "OPERATIONAL_MANAGER"){
            view.context.startActivity(Intent(view.context,CommodityManagerAuditActivity::class.java))
        }else{
            view.context.startActivity(Intent(view.context,CommodityManagerAuditMerchantActivity::class.java))
        }
    }

    fun onClickCommodity(view:View){
        view.context.startActivity(Intent(view.context, CommodityAddActivity::class.java))
    }

    fun onClickFreightManage(view:View){
        val intent = Intent(view.context, FreightListActivity::class.java)
        intent.putExtra("isAdmin",true)
        view.context.startActivity(intent)
    }

    fun onClickSubjectFreightManage(view:View){
        val intent = Intent(view.context, FreightSubjectListActivity::class.java)
        intent.putExtra("isOverseas",SpManager.isOverseasSystem())
        intent.putExtra("isAdmin",true)
        view.context.startActivity(intent)
    }

    fun onClickFreightAdd(view:View){
        view.context.startActivity(Intent(view.context, FreightAddActivity::class.java))
    }

    fun onClickSubjectFreightAdd(view:View){
        val intent = Intent(view.context, FreightAddActivity::class.java)
        intent.putExtra("isOverseas",SpManager.isOverseasSystem())
        intent.putExtra("isMergeExpress","1")
        view.context.startActivity(intent)
    }

    fun onClickSort(view:View,type: Int){
        val intent = Intent(view.context, CommoditySortActivity::class.java)
        intent.putExtra("sortMainType","$type")
        view.context.startActivity(intent)
    }

}