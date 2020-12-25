package com.idolmedia.yzymanager.viewmodel.ido

import android.content.Intent
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.model.entity.IdoAssociatedEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.Dp2PxUtils
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.common.ItemLineBean
import com.idolmedia.yzymanager.viewmodel.common.ItemTitleBean
import kotlin.text.StringBuilder

/**
 *  时间：2020/10/29-11:37
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.ido IdoAssociatedViewModel
 *  描述：
 */
class IdoAssociatedViewModel : BaseViewModel(),OnItemSelectListener {

    val adapter = ObservableField<BaseAdapter>()
    val strSearchKey = ObservableField<String>()
    val strIdoCountTip = ObservableField("已选中0个爱豆")
    val selectList = ArrayList<BaseBean>()
    var idoIds = ""
    var pageNo = 1

    fun queryIdo(){
        RetrofitHelper.instance().queryIdoAssociated(strSearchKey.get()?.trim(),pageNo,object : BaseObserver<IdoAssociatedEntity>(){
            override fun onSuccess(t: IdoAssociatedEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {
                        if (pageNo == 1){
                            it.clear()
                        }

                        val current = it.getMaxPosition()

                        if (pageNo==1){
                            it.add(ItemTitleBean("快捷关联认证爱豆"))
                            for(item in t.attestationIdol){
                                val bean =  ItemIdoBean(this@IdoAssociatedViewModel,item)
                                if (idoIds.contains(item.idolId)){
                                    bean.isSelect.set(true)
                                }
                                it.add(bean)
                            }
                            it.add(ItemLineBean(Dp2PxUtils.dip2px(14).toInt()))
                            it.add(ItemTitleBean("其他爱豆"))
                        }

                        for(item in t.datas){
                            val bean =  ItemIdoBean(this@IdoAssociatedViewModel,item)
                            if (idoIds.contains(item.idolId)){
                                bean.isSelect.set(true)
                            }
                            it.add(bean)
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

    override fun onItemSelect(position: Int, bean: BaseBean) {
        if (bean is ItemIdoBean){
            if (bean.isSelect.get()){
                selectList.add(bean)
            }else{
                var deleteId = -1
                for((index,item) in selectList.withIndex()){
                    item as ItemIdoBean
                    if (item.ido.idolId == bean.ido.idolId){
                        deleteId = index
                        break
                    }
                }
                if (deleteId>=0){
                    selectList.removeAt(deleteId)
                }
            }

            val idoIds = StringBuilder()
            for(item in selectList){
                item as ItemIdoBean
                if (idoIds.toString().isEmpty()){
                    idoIds.append(item.ido.idolId)
                }else{
                    idoIds.append(",${item.ido.idolId}")
                }
            }
            this.idoIds = idoIds.toString()

            strIdoCountTip.set("已选中${selectList.size}个爱豆")
        }
    }

    fun getIdoIntent():Intent{
        val intent = Intent()

        val idoIds = StringBuilder()
        val idoNames = StringBuilder()

        val idoList = ArrayList<IdoAssociatedEntity.Ido>()

        for(item in selectList){
            item as ItemIdoBean
            if (idoIds.toString().isEmpty()){
                idoIds.append(item.ido.idolId)
                idoNames.append(item.ido.idoName)
            }else{
                idoIds.append(",${item.ido.idolId}")
                idoNames.append(",${item.ido.idoName}")
            }
            idoList.add(item.ido)
        }
        intent.putExtra("idoIds",idoIds.toString())
        intent.putExtra("idoNames",idoNames.toString())
        intent.putExtra("idoList",Gson().toJson(idoList))
        return intent
    }

}