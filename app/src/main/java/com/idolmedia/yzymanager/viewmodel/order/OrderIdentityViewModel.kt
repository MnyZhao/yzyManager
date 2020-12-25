package com.idolmedia.yzymanager.viewmodel.order

import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.view.order.PictureActivity

/**
 *  时间：2020/11/17-19:58
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order OrderIdentityViewModel
 *  描述：
 */
class OrderIdentityViewModel : BaseViewModel() {

    val strImgOne = ObservableField<String>()
    val strImgTwo = ObservableField<String>()
    val strName = ObservableField<String>()
    val strNumber = ObservableField<String>()

    fun onClickImg(view:View){
        val intent = Intent(view.context,PictureActivity::class.java)
        val list = ArrayList<String>()
        list.add(strImgOne.get() ?: "")
        list.add(strImgTwo.get() ?: "")
        intent.putExtra("json",Gson().toJson(list))
        view.context.startActivity(intent)
    }

}