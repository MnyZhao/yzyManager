package com.idolmedia.yzymanager.viewmodel.main

import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.view.main.MineInformationActivity

/**
 *  时间：2020/11/25-16:06
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.main ItemMineHeadBean
 *  描述：
 */
class ItemMineHeadBean : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_mine_head
    }

    val strHead = ObservableField<String>()
    val strName = ObservableField<String>()
    val strId = ObservableField<String>()

    init {

        refresh()

    }

    fun onClickHead(view:View){
        view.context.startActivity(Intent(view.context,MineInformationActivity::class.java))
    }


    fun refresh(){
        val user = SpManager.getUserEntity()
        user?.let {

            strHead.set(it.datas.headImg)
            strName.set(it.datas.nickName)
            strId.set("商家ID:${it.datas.userId}")

        }

    }


}