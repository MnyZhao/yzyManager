package com.idolmedia.yzymanager.viewmodel.commodity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BasePopViewModel
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.bean.BuyNotesBean
import com.idolmedia.yzymanager.view.commodity.PurchaseNotesListActivity

/**
 *  时间：2020/10/28-14:37
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity ItemPurchaseNotesBean
 *  描述：
 */
class ItemPurchaseNotesBean(val adapter: BaseAdapter,val json:String) : BaseBean() {

    constructor(adapter: BaseAdapter,json:String,notes : BuyNotesBean):this(adapter, json){
        id = notes.aboutType
        strTitle.set(notes.title)
        strContent.set(notes.content)
    }

    override fun getViewType(): Int {
        return R.layout.item_purchase_notes
    }

    val strTitle = ObservableField<String>()
    val strContent = ObservableField<String>()
    var id = ""

    fun onClickDelete(view:View){
        val pop = BasePopWindow(view.context,R.layout.pop_common_content)
        pop.popModel = BasePopViewModel().apply {
            strContent.set("确认删除吗？")
            listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                    pop.dismiss()
                }

                override fun onPopRight(model: BaseViewModel) {
                    adapter.notifyDelete(position)
                    pop.dismiss()
                }

            }
        }
        pop.showAtCenter(view)
    }

    fun onClickAdd(view:View){
        val intent = Intent(view.context, PurchaseNotesListActivity::class.java)
        intent.putExtra("json",json)

        adapter.let {
            var ids = ""
            for (item in it.getDate()){
                if (item is ItemPurchaseNotesBean){
                    ids += "[${item.id}]"
                }
            }
            intent.putExtra("ids",ids)
        }

        (view.context as Activity).startActivityForResult(intent,100)
    }

    fun onClickShowPop(view:View){
        val intent = Intent(view.context, PurchaseNotesListActivity::class.java)
        intent.putExtra("json",json)

        adapter.let {
            var ids = ""
            for (item in it.getDate()){
                if (item is ItemPurchaseNotesBean){
                    ids += "[${item.id}]"
                }
            }
            intent.putExtra("ids",ids)
        }

        (view.context as Activity).startActivityForResult(intent,position)
    }

}