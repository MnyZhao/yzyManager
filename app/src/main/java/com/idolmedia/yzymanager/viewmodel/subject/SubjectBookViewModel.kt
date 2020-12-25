package com.idolmedia.yzymanager.viewmodel.subject

import android.text.InputType
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.PopIdoAssociatedBinding
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.interfaces.OnPopSelectListener
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.pop.ItemPopCommodityBean
import com.idolmedia.yzymanager.viewmodel.pop.PopIdoAssociatedViewModel
import com.idolmedia.yzymanager.viewmodel.pop.PopSearchCommodityViewModel
import java.lang.StringBuilder

/**
 *  时间：2020/11/23-12:21
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.subject SubjectBookViewModel
 *  描述：
 */
class SubjectBookViewModel : BaseViewModel() , OnItemDeleteListener,OnItemSelectListener {

    val adapter = ObservableField<BaseAdapter>()

    val strSearchWord = ObservableField<String>()
    val searchInputType = ObservableInt(InputType.TYPE_CLASS_NUMBER)
    val strStatus = ObservableField("电子刊ID")
    val isShowPop = ObservableBoolean(false)
    val strCountTip = ObservableField("已选中0件电子刊")

    val selectList = ArrayList<CommodityListEntity.Data>()
    var searchType = 0
    var bookIds = StringBuilder()
    var pageNo = 1

    fun queryBook(){

        val shopName = if (searchType==0) "" else strSearchWord.get()?:""
        val id = if (searchType==0) strSearchWord.get() ?: "" else ""

        RetrofitHelper.instance().queryBook(pageNo,shopName,id,object : BaseObserver<CommodityListEntity>(){
            override fun onSuccess(t: CommodityListEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {

                        if (pageNo==1){
                            it.clear()
                        }

                        if (t.datas.isNullOrEmpty() && pageNo==1){
                            //空
                            isLoadMore.set(false)
                        }else if(t.datas.isNotEmpty()){
                            val current = it.getMaxPosition()
                            for(item in t.datas){
                                val bean = ItemSubjectCommodityBean(item,this@SubjectBookViewModel)
                                if (bookIds.toString().contains(item.shopcommonId)){
                                    bean.isCheck.set(true)
                                }
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

    fun onClickSearch(view: View){

    }

    fun onClickStatusPop(view:View){
        val pop = BasePopWindow(view.context, R.layout.pop_search_book)
        pop.popModel = PopSearchCommodityViewModel(pop).apply {
            typeIndex.set(searchType)
            onSelectListener = object : OnPopSelectListener {
                override fun onSelect(position: Int) {
                    if (searchType!=position){
                        searchType = position
                        strSearchWord.set("")
                        if (position==0){
                            strStatus.set("电子刊ID")
                            searchInputType.set(InputType.TYPE_CLASS_NUMBER)
                        }else{
                            strStatus.set("电子刊名称")
                            searchInputType.set(InputType.TYPE_CLASS_TEXT)
                        }
                    }
                }
            }
        }
        pop.showAtViewBottomDarken(view)
        pop.setOnDismissListener {
            isShowPop.set(false)
        }
        isShowPop.set(true)
    }

    fun onClickSelectPop(view:View){
        if (selectList.size>0){

            val list = ArrayList<BaseBean>()
            for(item in selectList){
                list.add(ItemPopCommodityBean(item,this))
            }

            val pop = BasePopWindow(view.context,R.layout.pop_ido_associated)
            (pop.binding as PopIdoAssociatedBinding).recyclerView.layoutManager = LinearLayoutManager(view.context)
            pop.binding.tvConfirm.setOnClickListener { pop.dismiss() }
            ItemDecorationCommon.addItemDecorationTop(1,R.color.line_color,pop.binding.recyclerView)
            pop.popModel = PopIdoAssociatedViewModel(this,list,BaseAdapter(view.context))
            pop.showAtBottom(view)
        }
    }

    override fun onItemDelete(position: Int, bean: BaseBean) {
        bean as ItemPopCommodityBean
        selectList.removeAt(position)
        adapter.get()?.let {
            for(item in it.getDate()){
                if (item is ItemSubjectCommodityBean){
                    if (item.item.shopcommonId == bean.item.shopcommonId){
                        item.isCheck.set(false)
                        break
                    }
                }
            }
        }
        strCountTip.set("已选中${selectList.size}个电子刊")
    }

    override fun onItemSelect(position: Int, bean: BaseBean) {
        adapter.get()?.let {
            bean as ItemSubjectCommodityBean
            if (bean.isCheck.get()){
                selectList.add(bean.item)
            }else{
                for(item in selectList){
                    if (item.shopcommonId == bean.item.shopcommonId){
                        selectList.remove(item)
                        break
                    }
                }
            }

            bookIds.clear()
            for(item in selectList){
                bookIds.append(item.shopcommonId+",")
            }

            strCountTip.set("已选中${selectList.size}件电子刊")
        }
    }

}