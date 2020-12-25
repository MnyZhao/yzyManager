package com.idolmedia.yzymanager.viewmodel.commodity

import android.view.View
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.bean.SpecificationBean
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.pop.PopInventoryViewModel

/**
 *  时间：2020/10/22-16:22
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity ItemAdjustInventoryBean
 *  描述：
 */
class ItemAdjustInventoryBean(val item:SpecificationBean) : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_adjust_inventory
    }

    val strClassifyName = ObservableField<String>()
    val strCommodityInventory = ObservableField<String>()
    val strCommodityLimit = ObservableField<String>()
    val strPriceCurrent = ObservableField<String>()
    val strPriceOriginal = ObservableField<String>()
    val strClassifyImg = ObservableField<String>()

    var parentViewModel:CommodityAdjustInventoryViewModel? = null

    init {
        strClassifyImg.set(item.catalogImg)
        strClassifyName.set("规格名称：${item.catalogTitle}")
        strCommodityInventory.set("库存数量：${item.surplusNo}")
        strCommodityLimit.set("限购：${if (item.limitBuy.isNullOrEmpty() || item.limitBuy.toInt()<=0) "不限购" else item.limitBuy}")
        strPriceOriginal.set("划线价格：¥${item.originalPrice}")
        strPriceCurrent.set("普通价格：¥${item.currentPrice}")
    }

    fun onClickAdd(view:View){
        val pop = BasePopWindow(view.context,R.layout.pop_identity_add)
        pop.popModel = PopInventoryViewModel().apply {
            listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                    pop.dismiss()
                }

                override fun onPopRight(model: BaseViewModel) {
                    if (strCount.get().isNullOrEmpty()){
                        ToastUtil.show("请填写数量")
                    }else{
                        adjustInventory(0,strCount.get()!!.toInt())
                        pop.dismiss()
                    }
                }
            }
        }
        pop.showAtCenter(view)
    }

    fun onClickSubtract(view:View){
        val pop = BasePopWindow(view.context,R.layout.pop_identity_subtract)
        pop.popModel = PopInventoryViewModel().apply {
            listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                    pop.dismiss()
                }

                override fun onPopRight(model: BaseViewModel) {
                    if (strCount.get().isNullOrEmpty()){
                        ToastUtil.show("请填写数量")
                    }else{

                        val current = item.surplusNo.toInt()
                        val put = strCount.get()?.toInt() ?: 0
                        if (put>current){
                            ToastUtil.show("请填写正确的调整数量")
                            return
                        }

                        adjustInventory(1,strCount.get()!!.toInt())
                        pop.dismiss()
                    }
                }
            }
        }
        pop.showAtCenter(view)
    }

    private fun adjustInventory(operationStore:Int,store:Int){
        RetrofitHelper.instance().saveInventory(operationStore,store,item.sscId,parentViewModel!!.id,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("操作成功")
                    parentViewModel?.queryCommodityStore()
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

}