package com.idolmedia.yzymanager.viewmodel.pop

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.utils.SpManager

/**
 *  时间：2020/10/16-18:02
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.main PopHomeAccountModel
 *  描述：
 */
class PopHomeAccountModel(val onItemSelectListener: OnItemSelectListener) : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()

    fun showAccount(pop: BasePopWindow){
        SpManager.getUserList()?.let {

            val entity = SpManager.getUserEntity()!!

            for(item in it){
                if (entity.datas.userId == item.datas.userId){
                    adapter.get()?.add(ItemHomeAccountBean(pop,item,true).apply {
                        this.onItemSelectListener = this@PopHomeAccountModel.onItemSelectListener
                    })
                }else{
                    adapter.get()?.add(ItemHomeAccountBean(pop,item).apply {
                        this.onItemSelectListener = this@PopHomeAccountModel.onItemSelectListener
                    })
                }
            }
        }
        adapter.get()?.add(ItemHomeAccountBean(pop,1))
    }

}