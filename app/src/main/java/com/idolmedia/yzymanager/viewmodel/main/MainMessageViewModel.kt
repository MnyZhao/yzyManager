package com.idolmedia.yzymanager.viewmodel.main

import android.app.Activity
import android.view.View
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.entity.MessageListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.MainActivity
import com.idolmedia.yzymanager.viewmodel.ItemEmptyBean

/**
 *  时间：2020/10/16-14:35
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.main MainHomeViewModel
 *  描述：
 */
class MainMessageViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()

    var pageNo = 1

    fun queryHomeMessage(){
        RetrofitHelper.instance().queryMessage(pageNo,object : BaseObserver<MessageListEntity>(){
            override fun onSuccess(t: MessageListEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {

                        if (pageNo==1){
                            it.clear()
                        }

                        if (t.datas.isNullOrEmpty() && pageNo==1){
                            //无数据
                            it.emptyBean = ItemEmptyBean("您还没有消息哦~")
                        }else if(t.datas.isNotEmpty()){
                            val current = it.getMaxPosition()
                            for(item in t.datas){
                                it.add(ItemMessageBean(item))
                            }

                            it.notifyInserted(current)
                        }

                    }
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

            override fun onComplete() {
                super.onComplete()
                finishRefresh.set(finishRefresh.get()+1)
            }

        })
    }

    fun onClickReadAll(view:View){
        RetrofitHelper.instance().readMessage("",object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("已清除所有未读消息状态")
                    adapter.get()?.let {
                        for(bean in it.getDate()){
                            if (bean is ItemMessageBean){
                                bean.item.haveRead = "1"
                                bean.visibleTip.set(View.GONE)
                            }
                        }
                        (view.context as MainActivity).binding.viewModel?.visibleMessage?.set(View.GONE)
                    }
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

}