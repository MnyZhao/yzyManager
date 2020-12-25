package com.idolmedia.yzymanager.viewmodel.commodity

import android.content.Intent
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.model.entity.AdditionalEntity
import com.idolmedia.yzymanager.model.entity.CommodityPreconditionEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import kotlin.text.StringBuilder

/**
 *  时间：2020/10/27-10:56
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityAdditionalViewModel
 *  描述：
 */
class CommodityAdditionalViewModel : BaseViewModel(),OnItemDeleteListener {

    val adapter = ObservableField<BaseAdapter>()
    //上个页面已有的附加信息key字段 json格式 以[]包含
    var keys = ""

    fun queryAddition(){

        RetrofitHelper.instance().queryCommodityPrecondition(object : BaseObserver<CommodityPreconditionEntity>(){
            override fun onSuccess(t: CommodityPreconditionEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {

                        it.clear()

                        for(item in t.datas.defaultAdditionArray){
                            it.add(ItemAdditionalBean(item,this@CommodityAdditionalViewModel).apply {
                                if (keys.contains("[${item.key}]")){
                                    isCanCheck = false
                                }
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

    fun toSave():Boolean{

        adapter.get()?.let {

            for(item in it.getDate()){
                if (item is ItemAdditionalBean){
                    if (item.isCheck.get()){
                        return true
                    }
                }
            }

        }
        ToastUtil.show("请选择需要添加的附加信息")
        return false
    }

    fun getResultIntent():Intent{
        val intent = Intent()

        val additionType = StringBuilder()

        val list = ArrayList<AdditionalEntity.AdditionalSave>()

        adapter.get()?.let {

            for(bean in it.getDate()){
                if (bean is ItemAdditionalBean){
                    if (bean.isCheck.get()){
                        if (additionType.toString().isEmpty()){
                            additionType.append(bean.item.addition_key)
                        }else{
                            additionType.append("、${bean.item.addition_key}")
                        }
                        list.add(bean.getAdditional())
                    }
                }
            }

        }
        intent.putExtra("json",Gson().toJson(list))
        intent.putExtra("additionType",additionType.toString())
        return intent

    }

    override fun onItemDelete(position: Int, bean: BaseBean) {
        adapter.get()?.notifyDelete(position)
    }

}