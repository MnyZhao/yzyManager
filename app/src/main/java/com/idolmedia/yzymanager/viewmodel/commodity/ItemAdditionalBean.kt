package com.idolmedia.yzymanager.viewmodel.commodity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.*
import com.idolmedia.yzymanager.databinding.ItemAdditionalCommonBinding
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.model.entity.AdditionalEntity
import com.idolmedia.yzymanager.view.additional.AdditionalAddActivity

/**
 *  时间：2020/10/27-11:02
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity ItemAdditionalBean
 *  描述：
 */
class ItemAdditionalBean(val listener:OnItemDeleteListener) : BaseBean() {

    var layoutId = R.layout.item_additional
    override fun getViewType(): Int {
        return layoutId
    }

    constructor(item : AdditionalEntity.AdditionalCommon, listener:OnItemDeleteListener):this(listener){
        layoutId = R.layout.item_additional_common

        this.item.addition_key = item.key
        this.item.field = item.field
        this.item.placehold = item.field
        this.item.addition_isalter = item.additionIsalter
        this.item.addition_is = item.additionIs

        strName.set(item.field)
        strHint.set(item.placehold)
        isEdit.set(item.additionIsalter == "0")
        isMandatory.set(item.additionIs == "0")
    }

    constructor(item : AdditionalEntity.AdditionalSave, listener:OnItemDeleteListener):this(listener){
        this.item = item
        strName.set(item.field)
        strHint.set(item.placehold)
        isEdit.set(item.addition_isalter == "0")
        isMandatory.set(item.addition_is == "0")
    }

    val strName = ObservableField<String>()
    val strHint = ObservableField<String>()
    /**是否可编辑*/
    var isEdit = ObservableBoolean(true)
    /**是否必选*/
    var isMandatory = ObservableBoolean()
    val isCheck = ObservableBoolean()
    /**是否可以选择*/
    var isCanCheck = true
    var item = AdditionalEntity.AdditionalSave()

    fun getAdditional(): AdditionalEntity.AdditionalSave {
        item.field = strName.get()?: ""
        item.placehold = strHint.get()?: ""
        item.addition_is = if (isMandatory.get()) "0" else "1"
        item.addition_isalter = if (isEdit.get()) "0" else "1"
        return item
    }

    fun refreshAdditional(additionalBean:AdditionalEntity.AdditionalSave ){
        strName.set(additionalBean.field)
        strHint.set(additionalBean.placehold)
        isEdit.set(additionalBean.addition_isalter == "0")
        isMandatory.set(additionalBean.addition_is == "0")
    }

    fun onClickSelect(view:View){
        if (isCanCheck){
            view as ImageView
            isCheck.set(!isCheck.get())
            if (isCheck.get()){
                view.setImageDrawable(ContextCompat.getDrawable(view.context,R.drawable.ic_car_check))
            }else{
                view.setImageDrawable(ContextCompat.getDrawable(view.context,R.drawable.ic_car_uncheck))
            }
        }
    }

    fun onClickEdit(view:View){
        val intent = Intent(view.context, AdditionalAddActivity::class.java)
        intent.putExtra("json",Gson().toJson(item))
        intent.putExtra("position",position)
        (view.context as Activity).startActivityForResult(intent,position)
    }

    fun onClickDelete(view:View){
        val pop = BasePopWindow(view.context,R.layout.pop_common_content)
        pop.popModel = BasePopViewModel().apply {
            content.set("确认删除此条附加信息吗？")
            listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                    pop.dismiss()
                }

                override fun onPopRight(model: BaseViewModel) {
                    pop.dismiss()
                    this@ItemAdditionalBean.listener.onItemDelete(position,this@ItemAdditionalBean)
                }

            }
        }
        pop.showAtCenter(view)
    }

    override fun onBindViewModel(context: Context, binding: ViewDataBinding?, position: Int) {
        super.onBindViewModel(context, binding, position)
        if (binding is ItemAdditionalCommonBinding){
            if (isCanCheck){
                if (isCheck.get()){
                    binding.ivSelect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_car_check))
                }else{
                    binding.ivSelect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_car_uncheck))
                }
            }else{
                binding.ivSelect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_car_check_no))
            }
        }
    }

}