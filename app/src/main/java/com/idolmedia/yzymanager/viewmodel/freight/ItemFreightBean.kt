package com.idolmedia.yzymanager.viewmodel.freight

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.*
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.model.entity.FreightListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.freight.FreightAddActivity

/**
 *  时间：2020/10/26-18:06
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.freight ItemFreightBean
 *  描述：
 */
class ItemFreightBean(var freight:FreightListEntity.FreightItem) : BaseBean() {

    constructor(onItemSelectedListener: OnItemSelectListener,freight:FreightListEntity.FreightItem):this(freight){
        layoutId = R.layout.item_freight
        this.onItemSelectedListener = onItemSelectedListener
    }

    constructor(onItemDeleteListener: OnItemDeleteListener,freight:FreightListEntity.FreightItem):this(freight){
        layoutId = R.layout.item_freight_admin
        this.onItemDeleteListener = onItemDeleteListener
    }

    var layoutId = R.layout.item_freight_admin
    override fun getViewType(): Int {
        return layoutId
    }

    val strTitle = ObservableField<String>()
    val strName = ObservableField<String>()
    val isSelect = ObservableBoolean()
    private var onItemSelectedListener: OnItemSelectListener? = null
    private var onItemDeleteListener: OnItemDeleteListener? = null

    init {

        strName.set("模板名称："+freight.name)

    }

    fun onClickDelete(view:View){

        if (freight.isUsing == 1){
            ToastUtil.show("有商品正在使用，无法删除")
            return
        }

        val pop = BasePopWindow(view.context,R.layout.pop_common_content)
        pop.popModel = BasePopViewModel().apply {
            content.set("确认删除此条运费模板吗？")
            listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                    pop.dismiss()
                }

                override fun onPopRight(model: BaseViewModel) {
                    pop.dismiss()
                    deleteFreight()
                }
            }
        }
        pop.showAtCenter(view)

    }

    fun onClickEdit(view:View){
        val intent = Intent(view.context,FreightAddActivity::class.java)
        intent.putExtra("json",Gson().toJson(freight))
        (view.context as Activity).startActivityForResult(intent,position)
    }

    fun onClickCheck(view:View){
        onItemSelectedListener?.onItemSelect(position,this)
    }

    private fun deleteFreight(){
        RetrofitHelper.instance().deleteFreightTemplate(freight.freightId,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show(t.msg)
                    onItemDeleteListener?.onItemDelete(position,this@ItemFreightBean)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

}