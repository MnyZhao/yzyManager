package com.idolmedia.yzymanager.viewmodel.commodity

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.entity.CommoditySpecificationEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil

/**
 *  时间：2020/10/22-15:14
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityAdjustInventoryViewModel
 *  描述：
 */
class CommodityAdjustInventoryViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()

    var id = ""

    fun queryCommodityStore(){
        RetrofitHelper.instance().queryCommoditySpecification(false,id,object : BaseObserver<CommoditySpecificationEntity>(){
            override fun onSuccess(t: CommoditySpecificationEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {

                        it.clear()

                        for(item in t.datas){
                            it.add(ItemAdjustInventoryBean(item).apply {
                                parentViewModel = this@CommodityAdjustInventoryViewModel
                            })
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