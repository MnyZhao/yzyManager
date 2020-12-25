package com.idolmedia.yzymanager.viewmodel.commodity

import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.ItemEmptyBean

/**
 *  时间：2020/10/21-17:37
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityManagerViewModel
 *  描述：
 */
class CommodityManagerViewModel : BaseViewModel(),OnItemDeleteListener{

    val adapter = ObservableField<BaseAdapter>()

    val strFilterType = ObservableField("全部类型")
    val strFilterCommodity = ObservableField("全部商品")
    val strFilterIdentity = ObservableField("身份限购")
    val strFilterUp = ObservableField("全部")

    var buyerGrade = 0
    var checkStatus = 0
    var putaway = 0
    var shopName = ""
    var shopType = 0
    var commodityType= 0
    var shopcommonId = ""
    var pageNo = 1

    fun queryCommodity(){
        val map = getDataMap()
        map["isBusiness"] = !SpManager.userIsManage()

        RetrofitHelper.instance().commodityList(map,object : BaseObserver<CommodityListEntity>(){
            override fun onSuccess(t: CommodityListEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {

                        if (pageNo==1){
                            it.clear()
                        }

                        if (t.datas.isEmpty() && pageNo==1){
                            isLoadMore.set(false)
                            it.emptyBean = ItemEmptyBean("没找到商品哦")
                        }else if(t.datas.isNotEmpty()){
                            val current = it.getMaxPosition()
                            for(item in t.datas){
                                item.currentTime = t.currentTime
                                it.add(ItemCommodityManageBean(item,this@CommodityManagerViewModel))
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

    fun queryCommodityAudit(){
        if (SpManager.userIsManage()){
            queryCommodityAuditManage()
        }else{
            queryCommodityAuditMerchant()
        }
    }

    /**管理员待审核商品*/
    private fun queryCommodityAuditManage(){
        val map = getDataMap()
        map["isBusiness"] = false
        if (putaway==0){
            map["putaway"] = ""
        }else{
            map["putaway"] = putaway-1
        }

        map["buyerGrade"] = ""
        if (checkStatus >0){
            map["checkStatus"]= 2
        }else{
            map["checkStatus"]= 0
        }

        RetrofitHelper.instance().commodityListAudioAdmin(map,object : BaseObserver<CommodityListEntity>(){
            override fun onSuccess(t: CommodityListEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {

                        if (pageNo==1){
                            it.clear()
                        }

                        if (t.datas.isEmpty() &&pageNo==1){
                            isLoadMore.set(false)
                            it.emptyBean = ItemEmptyBean("没找到商品哦")
                        }else if(t.datas.isNotEmpty()){
                            val current = it.getMaxPosition()
                            for(item in t.datas){
                                item.currentTime = t.currentTime
                                it.add(ItemAuditAdminBean(item,this@CommodityManagerViewModel))
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

    /**商家待审核商品*/
    private fun queryCommodityAuditMerchant(){
        val map = getDataMap()
        when(checkStatus){
            0 -> map["checkStatus"] = ""
            1 -> map["checkStatus"] = "0"
            2 -> map["checkStatus"] = "3"
            3 -> map["checkStatus"] = "2"
        }
        map["isBusiness"] = true
        map["putaway"] = ""
        RetrofitHelper.instance().commodityListAudioWaiting(map,object : BaseObserver<CommodityListEntity>(){
            override fun onSuccess(t: CommodityListEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {

                        if (pageNo==1){
                            it.clear()
                        }

                        if (t.datas.isEmpty() &&pageNo==1){
                            isLoadMore.set(false)
                            it.emptyBean = ItemEmptyBean("没找到商品哦")
                        }else if(t.datas.isNotEmpty()){
                            val current = it.getMaxPosition()
                            for(item in t.datas){
                                item.currentTime = t.currentTime
                                val bean = ItemCommodityManageBean(item,this@CommodityManagerViewModel)
                                bean.layoutId = R.layout.item_audit_waiting
                                bean.isAudit = true
                                it.add(bean)
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

    private fun getDataMap():HashMap<String,Any?>{
        val map = HashMap<String,Any?>()

        when(shopType){
            0 -> map["shopType"] = ""
            1 -> map["shopType"] = "ordinary"
            2 -> map["shopType"] = "discount"
            3 -> map["shopType"] = "reserve2"
            4 -> map["shopType"] = "activity"
            5 -> map["shopType"] = "support"
            6 -> map["shopType"] = "crowdfunding"
            7 -> map["isVipShopToList"] = "0" //全部vip商品
            8 -> map["isVipShop"] = 1
            9 -> map["isVipShop"] = 2
        }

        when(commodityType){
            0 ->{
                map["isMergeExpress"] = ""
            }
            1 ->{
                map["isMergeExpress"] = "1"
            }
            2 ->{
                map["isMergeExpress"] = "0"
            }
        }

        map["buyerGrade"] = buyerGrade
        map["putaway"] = putaway
        map["pageNo"] = pageNo

        return map
    }

    override fun onItemDelete(position: Int, bean: BaseBean) {
        adapter.get()?.notifyDelete(position)
    }

}