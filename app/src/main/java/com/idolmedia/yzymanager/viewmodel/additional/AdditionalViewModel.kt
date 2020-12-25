package com.idolmedia.yzymanager.viewmodel.additional

import android.content.Intent
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.model.entity.AdditionalEntity
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.commodity.ItemAdditionalBean
import kotlin.text.StringBuilder

/**
 *  时间：2020/10/27-10:56
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityAdditionalViewModel
 *  描述：
 */
class AdditionalViewModel : BaseViewModel(),OnItemDeleteListener {

    val adapter = ObservableField<BaseAdapter>()

    var isEdit = false

    fun toSave():Boolean{

        adapter.get()?.let {

            for(item in it.getDate()){
                if (item is ItemAdditionalBean){
                    if (item.isCheck.get()){
                        return true
                    }
                }
            }

        }
        ToastUtil.show("请选择需要添加的附加信息")
        return false
    }

    fun getResultIntent():Intent{
        val intent = Intent()
        val additionType = StringBuilder()
        val list = ArrayList<AdditionalEntity.AdditionalSave>()
        adapter.get()?.let {
            for(bean in it.getDate()){
                if (bean is ItemAdditionalBean){
                    list.add(bean.item)
                }
            }
        }
        intent.putExtra("json",Gson().toJson(list))
        intent.putExtra("additionType",additionType.toString())
        return intent

    }

    override fun onItemDelete(position: Int, bean: BaseBean) {
        adapter.get()?.notifyDelete(position)
    }

}