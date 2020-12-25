package com.idolmedia.yzymanager.viewmodel.commodity

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnSortListener
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil

/**
 *  时间：2020/10/21-17:37
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityManagerViewModel
 *  描述：
 */
class CommoditySortViewModel : BaseViewModel(){

    val adapter = ObservableField<BaseAdapter>()
    var sortListener: OnSortListener? = null
    var pageNo = 1
    var sortMainType = ""

    fun queryCommodity(){
        RetrofitHelper.instance().queryCommodityList(sortMainType,pageNo,object : BaseObserver<CommodityListEntity>(){
            override fun onSuccess(t: CommodityListEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {

                        if (pageNo==1){
                            it.clear()
                        }

                        if (t.datas.isNullOrEmpty() && pageNo==1){
                            //无数据
                            isLoadMore.set( false )
                        }else if(t.datas.isNotEmpty()){
                            val current = it.getMaxPosition()
                            for(item in t.datas){
                                it.add(ItemCommoditySortBean(item,null,sortMainType).apply {
                                    sortListener = this@CommoditySortViewModel.sortListener
                                })
                            }
                            it.notifyInserted(current)
                            isLoadMore.set( true )
                        }else{
                            isLoadMore.set( false )
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

}