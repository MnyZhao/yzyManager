package com.idolmedia.yzymanager.viewmodel.main

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.PopHomeAccountBinding
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.view.MainActivity
import com.idolmedia.yzymanager.viewmodel.pop.PopHomeAccountModel

/**
 *  时间：2020/10/16-14:35
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.main MainHomeViewModel
 *  描述：
 */
class MainHomeViewModel : BaseViewModel(){

    val adapter = ObservableField<BaseAdapter>()
    val systemType = ObservableInt()
    val strName = ObservableField<String>()
    val strHead = ObservableField<String>()
    lateinit var onItemSelectListener:OnItemSelectListener

    private var currentUserId = ""

    init {

        SpManager.getUserEntity()?.let {
            currentUserId = it.datas.userId
            strName.set(it.datas.nickName)
            strHead.set(it.datas.headImg)
        }

        if (SpManager.isOverseasSystem()){
            systemType.set(1)
        }else{
            systemType.set(0)
        }

    }

    fun onClickLogin(view:View){

        (view.context as MainActivity).binding.viewModel?.visiblePopBg?.set(View.VISIBLE)

        val pop = BasePopWindow(view.context, R.layout.pop_home_account)
        (pop.binding as PopHomeAccountBinding).recyclerView.layoutManager = LinearLayoutManager(view.context)
        pop.popModel = PopHomeAccountModel(onItemSelectListener).also {
            it.adapter.set(BaseAdapter(view.context))
            it.showAccount(pop)
        }
        pop.showAtViewBottom(view)
        pop.setOnDismissListener {
            (view.context as MainActivity).binding.viewModel?.visiblePopBg?.set(View.GONE)
        }
    }

    fun onClickSystemSwitch(view:View){
        if (systemType.get()==0){
            systemType.set(1)
            SpManager.saveSystem(true)
        }else{
            systemType.set(0)
            SpManager.saveSystem(false)
        }
        if (SpManager.userIsManage()){
            val bean = adapter.get()?.getItem(2)
            if (bean is ItemHomeBean){
                if (systemType.get()==0){
                    bean.layoutId = R.layout.item_home_other_manager
                }else{
                    bean.layoutId = R.layout.item_home_other_manager_overseas
                }
                adapter.get()?.notifyItemChanged(2)
            }
        }
    }

    fun showData(){

        if (SpManager.getUserEntity()?.datas?.userId == currentUserId){
            return
        }

        currentUserId = SpManager.getUserEntity()?.datas?.userId ?: ""

        SpManager.getUserEntity()?.let {
            strName.set(it.datas.nickName)
            strHead.set(it.datas.headImg)
        }

        adapter.get()?.let {
            it.clear()
            it.add(ItemHomeOrderBean())
            if (SpManager.getUserIdentity() == "COMMON_PRODUCT_MERCHANT"){
                it.add(ItemHomeBean(R.layout.item_home_commodity_manager).apply {
                    strCommodityAdd.set("添加商品")
                    strAudit.set("审核中")
                })
                it.add(ItemHomeBean(R.layout.item_home_freight))
            }
            else if(SpManager.getUserIdentity() == "VIP_PRODUCT_MERCHANT"){
                it.add(ItemHomeBean(R.layout.item_home_commodity_manager).apply {
                    strCommodityAdd.set("添加VIP商品")
                    strAudit.set("审核中")
                })
                it.add(ItemHomeBean(R.layout.item_home_freight))
            }
            else if(SpManager.getUserIdentity() == "OPERATIONAL_MANAGER"){
                it.add(ItemHomeBean(R.layout.item_home_commodity_manager).apply {
                    strCommodityAdd.set("")
                    strAudit.set("待审核")
                    visibleCommodityAdd.set(View.INVISIBLE)
                })
                if (SpManager.isOverseasSystem()){
                    it.add(ItemHomeBean(R.layout.item_home_other_manager_overseas))
                }else{
                    it.add(ItemHomeBean(R.layout.item_home_other_manager))
                }
                it.add(ItemHomeBean(R.layout.item_home_about_commodity_sort))
            }
            it.notifyDataSetChanged()
        }
    }

}