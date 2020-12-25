package com.idolmedia.yzymanager.viewmodel.pop

import android.content.Intent
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.model.entity.LoginEntity
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.login.LoginActivity

/**
 *  时间：2020/10/19-11:24
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.pop ItemHomeAccountBean
 *  描述：
 */
class ItemHomeAccountBean(val pop: BasePopWindow, val type:Int) : BaseBean() {

    constructor(pop: BasePopWindow,entity: LoginEntity):this(pop,0){
        strHead.set(entity.datas.headImg)
        strName.set(entity.datas.nickName)
    }

    constructor(pop: BasePopWindow,entity: LoginEntity,isSelect:Boolean):this(pop,entity){
        isSelected.set(isSelect)
    }

    override fun getViewType(): Int {
        return when (type){
            0 -> R.layout.item_pop_account
            else -> R.layout.item_pop_account_add
        }
    }

    val strHead = ObservableField<String>()
    val strName = ObservableField<String>()
    val isSelected = ObservableBoolean(false)
    var onItemSelectListener: OnItemSelectListener? = null

    fun onClickAdd(view: View){
        view.context.startActivity(Intent(view.context,LoginActivity::class.java))
        pop.dismiss()
    }

    fun onClickSelect(view: View){
        if (!isSelected.get()){
            onItemSelectListener?.onItemSelect(position,this)
        }
        pop.dismiss()

    }

}