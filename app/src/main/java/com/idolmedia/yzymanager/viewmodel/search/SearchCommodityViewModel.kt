package com.idolmedia.yzymanager.viewmodel.search

import android.text.InputType
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.interfaces.OnPopSelectListener
import com.idolmedia.yzymanager.interfaces.OnSubjectListener
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.model.entity.SubjectListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.search.SearchCommodityActivity
import com.idolmedia.yzymanager.view.subject.SubjectManagerActivity
import com.idolmedia.yzymanager.viewmodel.ItemEmptyBean
import com.idolmedia.yzymanager.viewmodel.commodity.ItemAuditAdminBean
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityManageBean
import com.idolmedia.yzymanager.viewmodel.pop.PopSearchCommodityViewModel
import com.idolmedia.yzymanager.viewmodel.subject.ItemSubjectBean

/**
 *  时间：2020/10/22-17:14
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.search SearchCommodityViewModel
 *  描述：
 */
class SearchCommodityViewModel : BaseViewModel(),OnItemDeleteListener,OnSubjectListener {

    val strSearchType = ObservableField("商品ID")
    val searchInputType = ObservableInt(InputType.TYPE_CLASS_NUMBER)
    val isShowPop = ObservableBoolean(false)
    val strWord = ObservableField<String>()
    val adapter = ObservableField<BaseAdapter>()

    var searchType = 0
    /**上下架(0:上架;1:下架)*/
    var putaway = 0
    var isAudit = false
    var isSubject = false
    var pageNo = 1

    fun onClickPop(view:View){
        if (isSubject){
            showSubjectPop(view)
        }else{
            showCommodityPop(view)
        }
    }

    fun onClickDismiss(view:View){
        strWord.set("")
    }

    fun search(){
        val map = HashMap<String,Any?>()
        if (isSubject){
            map["pageNo"] = pageNo
            if (searchType==0){
                map["subjectId"] = strWord.get() ?: ""
            }else if (searchType == 1){
                map["subjectName"] = strWord.get() ?: ""
            }

            RetrofitHelper.instance().querySubjectSearch(map,object : BaseObserver<SubjectListEntity>(){
                override fun onSuccess(t: SubjectListEntity) {
                    if (t.resultCode==1){
                        adapter.get()?.let {
                            if (pageNo == 1){
                                it.clear()
                            }

                            if (t.datas.isEmpty() && pageNo==1){
                                //无数据
                                it.emptyBean = ItemEmptyBean("没有搜到内容，换一个关键词试试呢")
                                isLoadMore.set(false)
                            }
                            else if(t.datas.isNotEmpty()){
                                val current = it.getMaxPosition()

                                for(item in t.datas){
                                    it.add(ItemSubjectBean(item,this@SearchCommodityViewModel))
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

        }else{

            if (!isAudit){
                map["putaway"] = putaway
            }
            if (searchType == 0){
                map["shopcommonId"] = strWord.get() ?: ""
            }else if (searchType == 1){
                map["shopName"] = strWord.get() ?: ""
            }else if (searchType == 2){
                map["virtualuserId"] = strWord.get() ?: ""
            }else if (searchType == 3){
                map["fromNickName"] = strWord.get() ?: ""
            }else if (searchType == 4){
                map["subjectId"] = strWord.get() ?: ""
            }
            map["pageNo"] = pageNo
            map["isBusiness"] = !SpManager.userIsManage()

            val observer = object : BaseObserver<CommodityListEntity>(){
                override fun onSuccess(t: CommodityListEntity) {
                    if (t.resultCode==1){
                        adapter.get()?.let {

                            if (pageNo == 1){
                                it.clear()
                            }

                            if (t.datas.isEmpty() && pageNo==1){
                                //无数据
                                it.emptyBean = ItemEmptyBean("没有搜到内容，换一个关键词试试呢")
                                isLoadMore.set(false)
                            }
                            else if(t.datas.isNotEmpty()){
                                val current = it.getMaxPosition()


                                for(item in t.datas){
                                    if (isAudit){
                                        if (SpManager.userIsManage()){
                                            item.currentTime = t.currentTime
                                            it.add(ItemAuditAdminBean(item,this@SearchCommodityViewModel))
                                        }else{
                                            item.currentTime = t.currentTime
                                            val bean = ItemCommodityManageBean(item,this@SearchCommodityViewModel)
                                            bean.layoutId = R.layout.item_audit_waiting
                                            bean.isAudit = true
                                            it.add(bean)
                                        }
                                    }else{
                                        item.currentTime = t.currentTime
                                        it.add(ItemCommodityManageBean(item,this@SearchCommodityViewModel))
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
            }

            if (isAudit){
                if (SpManager.userIsManage()){
                    RetrofitHelper.instance().commodityListAudioAdmin(map,observer)
                }else{
                    RetrofitHelper.instance().commodityListAudioWaiting(map,observer)
                }
            }
            else{
                RetrofitHelper.instance().commodityList(map,observer)
            }
        }

    }

    override fun onItemDelete(position: Int, bean: BaseBean) {
        adapter.get()?.notifyDelete(position)
    }

    private fun showCommodityPop(view: View){
        val pop = BasePopWindow(view.context, if (SpManager.userIsManage()) R.layout.pop_search_commodity_manage else R.layout.pop_search_commodity)
        pop.popModel = PopSearchCommodityViewModel(pop).apply {
            typeIndex.set(searchType)
            onSelectListener = object : OnPopSelectListener {
                override fun onSelect(position: Int) {
                    if (searchType != position){
                        strWord.set("")
                        searchType = position
                        if (position == 0){
                            strSearchType.set("商品ID")
                            searchInputType.set(InputType.TYPE_CLASS_NUMBER)
                        }
                        else if (position == 1){
                            strSearchType.set("商品名称")
                            searchInputType.set(InputType.TYPE_CLASS_TEXT)
                        }
                        else if (position == 2){
                            strSearchType.set("商家ID")
                            searchInputType.set(InputType.TYPE_CLASS_NUMBER)
                        }
                        else if (position == 3){
                            strSearchType.set("商家昵称")
                            searchInputType.set(InputType.TYPE_CLASS_TEXT)
                        }
                        else if (position == 4){
                            strSearchType.set("专题ID")
                            searchInputType.set(InputType.TYPE_CLASS_NUMBER)
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

    private fun showSubjectPop(view:View){
        val pop = BasePopWindow(view.context, R.layout.pop_search_subject)
        pop.popModel = PopSearchCommodityViewModel(pop).apply {
            typeIndex.set(searchType)
            onSelectListener = object : OnPopSelectListener {
                override fun onSelect(position: Int) {
                    if (searchType != position){
                        strWord.set("")
                        searchType = position
                        if (position == 0){
                            strSearchType.set("专题ID")
                            searchInputType.set(InputType.TYPE_CLASS_NUMBER)
                        }
                        else if (position == 1){
                            strSearchType.set("专题名称")
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

    override fun onDelete(position: Int) {
        adapter.get()?.notifyDelete(position)
    }

    override fun onPutAway(position: Int) {

    }

    override fun onRefresh(bean: BaseBean) {
        bean.context?.let {
            if (it is SearchCommodityActivity){
                it.binding.smartLayout.autoRefresh()
            }
        }
    }

}