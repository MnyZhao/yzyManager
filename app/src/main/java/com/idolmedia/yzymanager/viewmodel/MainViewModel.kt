package com.idolmedia.yzymanager.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.entity.HomeMessageEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil

/**
 *  时间：2020/10/16-14:04
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel MineViewModel
 *  描述：
 */
class MainViewModel : BaseViewModel() {

    val currentItem = ObservableInt(0)
    val visiblePopBg = ObservableInt(View.GONE)

    val visibleMessage = ObservableInt(View.GONE)
    val strMessageCount = ObservableField<String>()

    fun queryHomeNote(observer:BaseObserver<HomeMessageEntity>){
        RetrofitHelper.instance().getHomeNote(object : BaseObserver<HomeMessageEntity>(){
            override fun onSuccess(t: HomeMessageEntity) {
                if (t.resultCode==1){
                    queryHomeMessage()
                    //显示订单与审核数量
                    observer.onSuccess(t)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    fun queryHomeMessage(){
        RetrofitHelper.instance().getHomeNoteOverseas(object : BaseObserver<HomeMessageEntity>(){
            override fun onSuccess(t: HomeMessageEntity) {
                if (t.resultCode==1){
                    if (t.messageNum>0){
                        visibleMessage.set(View.VISIBLE)
                        if (t.messageNum<=99){
                            strMessageCount.set("${t.messageNum}")
                        }else{
                            strMessageCount.set("99+")
                        }
                    }else{
                        visibleMessage.set(View.GONE)
                    }
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }


}