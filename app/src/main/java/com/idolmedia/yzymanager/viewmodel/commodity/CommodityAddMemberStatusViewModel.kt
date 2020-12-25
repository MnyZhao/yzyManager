package com.idolmedia.yzymanager.viewmodel.commodity

import android.content.Intent
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseViewModel

/**
 *  时间：2020/10/26-14:58
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityAddViewModel
 *  描述：
 */
class CommodityAddMemberStatusViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()

    fun getIntent():Intent{
        val intent = Intent()

        adapter.get()?.let {
            val isOpen = (it.getDate()[0] as ItemCommodityAddBean).isOpen.get()
            intent.putExtra("isLimit",isOpen)

            val startTime = (it.getDate()[1] as ItemCommodityAddBean).strContent.get() ?: ""
            intent.putExtra("startTime",startTime)
            if (isOpen){
                val endTime = (it.getDate()[2] as ItemCommodityAddBean).strContent.get() ?: ""
                intent.putExtra("endTime",endTime)
            }
        }

        return intent
    }

}