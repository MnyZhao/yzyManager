package com.idolmedia.yzymanager.viewmodel.commodity

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.interfaces.OnResultListener
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.ItemEmptyBean

/**
 *  时间：2020/11/19-11:06
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityManageSingleViewModel
 *  描述：
 */
class CommodityManageSingleViewModel : BaseViewModel()  {

    val adapter = ObservableField<BaseAdapter>()

    lateinit var onPassedListener:OnResultListener
    lateinit var onRefuseListener:OnResultListener
    lateinit var onDeleteListener:OnItemDeleteListener

    var isPassed = false
    var commodityId = ""

    fun queryCommodity(){

        val map = HashMap<String,Any?>()
        map["isBusiness"] = !SpManager.userIsManage()
        map["shopcommonId"] = commodityId
        map["pageNo"] = 1

        val observer = object : BaseObserver<CommodityListEntity>(){
            override fun onSuccess(t: CommodityListEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {

                        it.clear()

                        if (t.datas.isNullOrEmpty()){
                            it.emptyBean = ItemEmptyBean("此消息已失效")
                        }else{
                            for(item in t.datas){
                                item.currentTime = t.currentTime
                                if (isPassed){
                                    it.add(ItemCommodityManageBean(item,onDeleteListener))
                                }else{
                                    if (SpManager.userIsManage()){
                                        it.add(ItemAuditAdminBean(item,null).apply {
                                            onAuditPassedResultListener = onPassedListener
                                            onAuditRefuseResultListener = onRefuseListener
                                        })
                                    }else{
                                        val bean = ItemCommodityManageBean(item,onDeleteListener)
                                        bean.onAuditPassedResultListener = onPassedListener
                                        bean.onAuditRefuseResultListener = onRefuseListener
                                        bean.layoutId = R.layout.item_audit_waiting
                                        bean.isAudit = true
                                        it.add(bean)
                                    }

                                }
                            }
                        }

                        it.notifyDataSetChanged()
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

        }

        if (isPassed){
            RetrofitHelper.instance().commodityList(map,observer)
        }else{
            if (SpManager.userIsManage()){
                RetrofitHelper.instance().commodityListAudioAdmin(map, observer)
            }else{
                RetrofitHelper.instance().commodityListAudioWaiting(map, observer)
            }
        }

    }



}