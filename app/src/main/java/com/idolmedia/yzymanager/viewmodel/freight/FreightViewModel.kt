package com.idolmedia.yzymanager.viewmodel.freight

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.model.entity.FreightListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.ItemEmptyBean

/**
 *  时间：2020/10/26-18:02
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.freight FreightViewModel
 *  描述：
 */
class FreightViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()
    var freightId = "" //上级页面带过来的已选择的运费模板id
    //是否是管理页过来的，管理页可以编辑，其余只能选择
    var isAdmin = false
    //管理员操作商户的商品时要选择商户的运费模板
    var fromId = ""
    var pageNo = 1
    var onItemDeleteListener:OnItemDeleteListener = object : OnItemDeleteListener{
        override fun onItemDelete(position: Int, bean: BaseBean) {
            adapter.get()?.notifyDelete(position)
        }
    }
    lateinit var onItemSelectListener:OnItemSelectListener

    fun queryFreight(){
        RetrofitHelper.instance().queryFreightList(pageNo,fromId,object : BaseObserver<FreightListEntity>(){
            override fun onSuccess(t: FreightListEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {
                        if (pageNo==1){
                            it.clear()
                        }

                        if (t.datas.isEmpty() && pageNo == 1){
                            it.emptyBean = ItemEmptyBean("您还未添加运费模板")
                            isLoadMore.set(false)
                        }else if(t.datas.isNotEmpty()){
                            val current = it.getMaxPosition()
                            for(item in t.datas){
                                if (isAdmin){
                                    it.add(ItemFreightBean(onItemDeleteListener ,item))
                                }else{
                                    val bean = ItemFreightBean(onItemSelectListener ,item)
                                    if (item.freightId == freightId){
                                        bean.isSelect.set(true)
                                    }
                                    it.add(bean)
                                }
                            }

                            it.notifyInserted(current)
                            isLoadMore.set(true)
                        }else{
                            isLoadMore.set(false)
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

    fun querySubjectFreight(isOverseas:Boolean){
        RetrofitHelper.instance().querySubjectFreightList(pageNo,isOverseas,object : BaseObserver<FreightListEntity>(){
            override fun onSuccess(t: FreightListEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {
                        if (pageNo==1){
                            it.clear()
                        }

                        val current = it.getMaxPosition()
                        for(item in t.datas){
                            if (isAdmin){
                                it.add(ItemFreightBean(onItemDeleteListener ,item))
                            }else{
                                val bean = ItemFreightBean(onItemSelectListener ,item)
                                if (item.freightId == freightId){
                                    bean.isSelect.set(true)
                                }
                                it.add(bean)
                            }
                        }

                        it.notifyInserted(current)

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