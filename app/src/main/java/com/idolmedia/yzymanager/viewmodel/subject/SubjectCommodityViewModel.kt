package com.idolmedia.yzymanager.viewmodel.subject

import android.util.Log
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.bean.SubjectCommodityBean
import com.idolmedia.yzymanager.model.bean.SubjectSortBean
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.ItemEmptyBean

/**
 *  时间：2020/10/19-15:37
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.subject SubjectEditViewModel
 *  描述：
 */
class SubjectCommodityViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()

    var subjectId = ""
    var isOverseas = false
    var pageNo = 1

    private val commoditySort = ArrayList<String>()
    private val commodityDelete = ArrayList<SubjectCommodityBean>()
    private val sortCommodityList =  ArrayList<SubjectCommodityBean>()
    private val sortSubjectList =  ArrayList<SubjectSortBean>()


    fun querySubjectCommodity(){

        RetrofitHelper.instance().querySubjectCommodity(subjectId,object : BaseObserver<CommodityListEntity>(){
            override fun onSuccess(t: CommodityListEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {
                        it.clear()
                        commoditySort.clear()
                        commodityDelete.clear()

                        if (t.datas.isEmpty()){
                            it.emptyBean = ItemEmptyBean("此专题暂无商品")
                        }else{
                            for(item in t.datas){
                                it.add(ItemSubjectCommodityBean(item))
                                commoditySort.add(item.sortNo)
                            }
                            it.notifyDataSetChanged()
                        }
                        sortCommodity()
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

    fun saveSubjectCommoditySort(){
        RetrofitHelper.instance().saveSubjectCommoditySort(Gson().toJson(sortCommodityList),Gson().toJson(commodityDelete),subjectId,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("保存成功")
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    fun sortCommodity(){
        sortCommodityList.clear()
        for((index,s) in commoditySort.withIndex()){
            val bean = adapter.get()?.getDate()!![index] as ItemSubjectCommodityBean
            val c = SubjectCommodityBean()
            c.shopcommonId = bean.item.shopcommonId
            c.sortNo = s
            c.overseas = bean.item.overseas
            sortCommodityList.add(c)
        }
    }

    fun sortSubject(){
        sortSubjectList.clear()
        for((index,s) in commoditySort.withIndex()){
            val bean = adapter.get()?.getDate()!![index] as ItemSubjectBean
            val c = SubjectSortBean()
            c.subjectId = bean.item.subjectId
            sortSubjectList.add(c)
        }
        Log.d("===sort",Gson().toJson(sortSubjectList))
    }

    fun deleteCommodity(position:Int){
        val bean = adapter.get()?.getDate()!![position] as ItemSubjectCommodityBean
        val c = SubjectCommodityBean()
        c.shopcommonId = bean.item.shopcommonId
        c.sortNo = bean.item.sortNo
        c.overseas = bean.item.overseas
        commodityDelete.add(c)
        commoditySort.removeAt(position)
        sortCommodityList.removeAt(position)
        adapter.get()?.notifyDelete(position)
    }

}