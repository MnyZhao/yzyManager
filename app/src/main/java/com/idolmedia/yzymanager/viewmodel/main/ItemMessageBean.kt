package com.idolmedia.yzymanager.viewmodel.main

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.model.entity.MessageListEntity
import com.idolmedia.yzymanager.model.entity.MessageStatusEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.TimeUtils
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.commodity.CommodityManageSingleActivity

/**
 *  时间：2020/11/19-9:49
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.main ItemMessageBean
 *  描述：
 */
class ItemMessageBean(val item:MessageListEntity.Message) : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_message
    }

    val strTime = ObservableField<String>()
    val strTitle = ObservableField<String>()
    val strContent = ObservableField<String>()
    val visibleTip = ObservableInt(View.GONE)

    init {

        strTitle.set(item.title)
        strContent.set(item.content)
        if (item.haveRead == "0"){
            visibleTip.set(View.VISIBLE)
        }else{
            visibleTip.set(View.GONE)
        }

        strTime.set(TimeUtils.getTimeFormatText(item.createTimeStr))

    }

    fun onItemClick(view: View){

        if ((item.shoppingTo == "1" && SpManager.isOverseasSystem()) ||
            item.shoppingTo == "2" && !SpManager.isOverseasSystem()){
            ToastUtil.show("当前商品需切换系统操作")
            return
        }

        if (item.haveRead == "0"){
            RetrofitHelper.instance().readMessage(item.id,object : BaseObserver<BaseEntity>(){
                override fun onSuccess(t: BaseEntity) {
                    if (t.resultCode==1){
                        item.haveRead = "1"
                        visibleTip.set(View.GONE)
                    }
                }

                override fun onError(msg: String) {
                }

            })
        }

        RetrofitHelper.instance().queryMessageStatus(item.id,"",object : BaseObserver<MessageStatusEntity>(){
            override fun onSuccess(t: MessageStatusEntity) {
                if (t.resultCode==1){
                    if (t.datas.clickType == "3"){
                        ToastUtil.show("消息失效")
                    }else{
                        val intent = Intent(view.context,CommodityManageSingleActivity::class.java)
                        intent.putExtra("commodityId",item.shopcommonId)
                        intent.putExtra("isPassed",item.clickType=="1")
                        (view.context as Activity).startActivityForResult(intent,position)
                    }
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })

    }

}