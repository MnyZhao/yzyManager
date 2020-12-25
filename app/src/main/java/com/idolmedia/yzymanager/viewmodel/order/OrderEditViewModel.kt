package com.idolmedia.yzymanager.viewmodel.order

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.entity.OrderAdditionalEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.ItemEmptyBean
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean

/**
 *  时间：2020/11/17-17:42
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order OrderAdditionalEditViewModel
 *  描述：
 */
class OrderEditViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()

    var orderNum = ""

    fun queryOrderAdditional(){
        RetrofitHelper.instance().queryOrderAdditional(orderNum,object : BaseObserver<OrderAdditionalEntity>(){
            override fun onSuccess(t: OrderAdditionalEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {

                        it.clear()

                        if(t.datas.isNullOrEmpty()){
                            it.emptyBean = ItemEmptyBean("此商品未设置附加信息")
                        }else{
                            it.add(ItemCommodityAddBean("附加信息"))

                            for(item in t.datas){
                                it.add(ItemCommodityAddBean(item))
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

        })
    }

}