package com.idolmedia.yzymanager.viewmodel.commodity

import android.view.View
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BasePopViewModel
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.interfaces.OnSortListener
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.model.entity.SubjectSortEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil

/**
 *  时间：2020/10/22-11:24
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity ItemCommodityBean
 *  描述：
 */
class ItemCommoditySortBean(commodityItem : CommodityListEntity.Data, onItemDeleteListener: OnItemDeleteListener?,var sortMainType:String) : ItemBaseManageBean(commodityItem,onItemDeleteListener) {

    init {
        layoutId = R.layout.item_commodity_sort
    }

    val strCommoditySystem = ObservableField<String>()
    val strTop = ObservableField<String>()
    var sortListener: OnSortListener? = null

    init {

        if (commodityItem.overseas == "true"){
            strCommoditySystem.set("海外商品")
        }else{
            strCommoditySystem.set("国内商品")
        }

        if (commodityItem.shopType == "reserve2"){
            //定金商品
            if (commodityItem.reserveStatus == "2"){
                //尾款状态
                strCommoditySales.set("定金销量:${commodityItem.saleNo} 尾款销量:${if (commodityItem.finalSaleNo.isNullOrEmpty()) "0" else commodityItem.finalSaleNo}")
            }else{
                strCommoditySales.set("定金销量:${commodityItem.saleNo}")
            }
        }else{
            strCommoditySales.set("销量:${commodityItem.saleNo}")
        }

        if (commodityItem.isStick == "1"){
            strTop.set("取消置顶")
        }else{
            strTop.set("置顶")
        }

    }

    fun onClickTop(view:View){
        RetrofitHelper.instance().commodityStick(if (commodityItem.isStick == "1") "0" else "1",commodityItem.overseas,commodityItem.shopcommonId,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    commodityItem.isStick = if (commodityItem.isStick == "1") "0" else "1"
                    if (commodityItem.isStick == "1"){
                        strTop.set("取消置顶")
                    }else{
                        strTop.set("置顶")
                    }
                    sortListener?.onStick()
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })

    }

    fun onClickSort(view:View){
        RetrofitHelper.instance().queryCommoditySortNo(
            commodityItem.isStick,
            commodityItem.shopcommonId,
            sortMainType,
            commodityItem.sortNo,
            commodityItem.overseas,
            object : BaseObserver<SubjectSortEntity>(){
                override fun onSuccess(t: SubjectSortEntity) {
                    if (t.resultCode==1){
                        if (t.resultCode==1){
                            val pop = BasePopWindow(view.context,R.layout.pop_commodity_sort)
                            val popModel = BasePopViewModel()
                            popModel.content.set("当前商品位置为第${t.currPlace}位")
                            popModel.listener = object : BasePopWindow.OnPopClickListener{
                                override fun onPopLeft(model: BaseViewModel) {
                                    pop.dismiss()
                                }

                                override fun onPopRight(model: BaseViewModel) {
                                    if (popModel.title.get().isNullOrEmpty()){
                                        ToastUtil.show("请填写排序位置")
                                        return
                                    }
                                    saveSort(t.currPlace,popModel.title.get() ?: "")
                                    pop.dismiss()
                                }
                            }
                            pop.popModel = popModel
                            pop.showAtCenter(view)
                        }
                    }
                }

                override fun onError(msg: String) {
                    ToastUtil.show(msg)
                }
            }
        )
    }

    private fun saveSort(currPlace:String,sortNum:String){
        RetrofitHelper.instance().saveCommoditySort(
            currPlace,
            commodityItem.mixtureId,
            commodityItem.shopcommonId,
            sortMainType,
            sortNum,
            commodityItem.overseas,
            object : BaseObserver<BaseEntity>(){
                override fun onSuccess(t: BaseEntity) {
                    if (t.resultCode==1){
                        ToastUtil.show("操作成功")
                    }
                }

                override fun onError(msg: String) {
                    ToastUtil.show(msg)
                }

            }
        )
    }

    override fun refreshButton(){

    }



}