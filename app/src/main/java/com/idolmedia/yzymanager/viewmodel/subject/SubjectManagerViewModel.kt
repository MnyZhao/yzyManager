package com.idolmedia.yzymanager.viewmodel.subject

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnPopSelectListener
import com.idolmedia.yzymanager.interfaces.OnSubjectListener
import com.idolmedia.yzymanager.model.entity.SubjectListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.subject.SubjectManagerActivity
import com.idolmedia.yzymanager.viewmodel.ItemEmptyBean
import com.idolmedia.yzymanager.viewmodel.pop.PopSubjectMergeViewModel
import com.idolmedia.yzymanager.viewmodel.pop.PopSubjectOperationViewModel
import kotlinx.android.synthetic.main.activity_subject_manager.*

/**
 *  时间：2020/10/19-13:40
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.subject SubjectManagerViewModel
 *  描述：
 */
class SubjectManagerViewModel : BaseViewModel(), OnSubjectListener {

    val adapter = ObservableField<BaseAdapter>()
    val visiblePopBg = ObservableInt(View.GONE)
    val strStatusMerge = ObservableField("全部")
    val strStatusUp = ObservableField("全部")

    var pageNo = 1
    var isMergeExpress = ""
    var isPutway = ""

    fun onClickMerge(view:View){
        val pop = BasePopWindow(view.context, R.layout.pop_subject_merge)
        pop.popModel = PopSubjectMergeViewModel(object : OnPopSelectListener{
            override fun onSelect(position: Int) {
                if (position==0){
                    isMergeExpress = ""
                    strStatusMerge.set("全部")
                }else if(position==1){
                    isMergeExpress = "1"
                    strStatusMerge.set("合并")
                }else{
                    isMergeExpress = "0"
                    strStatusMerge.set("非合并")
                }
                (view.context as SubjectManagerActivity).smart_layout.autoRefresh()
                pop.dismiss()
            }
        }).apply {
            mergeIndex.set(if (isMergeExpress == "") 0 else if (isMergeExpress=="1") 1 else 2)
        }
        pop.showAtViewBottom(view)
        pop.setOnDismissListener {
            visiblePopBg.set(View.GONE)
        }
        visiblePopBg.set(View.VISIBLE)
    }

    fun onClickOperation(view:View){
        val pop = BasePopWindow(view.context, R.layout.pop_subject_operation)
        pop.popModel = PopSubjectOperationViewModel(object : OnPopSelectListener{
            override fun onSelect(position: Int) {
                if (position==0){
                    isPutway = ""
                    strStatusUp.set("全部")
                }else if (position==1){
                    isPutway = "0"
                    strStatusUp.set("上架")
                }else{
                    isPutway = "1"
                    strStatusUp.set("下架")
                }
                (view.context as SubjectManagerActivity).smart_layout.autoRefresh()

                pop.dismiss()
            }

        }).apply {
            upIndex.set(if (isPutway == "") 0 else if (isPutway == "0") 1 else 2)
        }
        pop.showAtViewBottom(view)
        pop.setOnDismissListener {
            visiblePopBg.set(View.GONE)
        }
        visiblePopBg.set(View.VISIBLE)
    }

    fun querySubject(){
        RetrofitHelper.instance().querySubject(pageNo,isMergeExpress,isPutway,object : BaseObserver<SubjectListEntity>(){
            override fun onSuccess(t: SubjectListEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {

                        if (pageNo==1){
                            it.clear()
                        }

                        if (t.datas.isNullOrEmpty() && pageNo==1){
                            //空数据
                            it.emptyBean = ItemEmptyBean("没找到专题哦")
                            isLoadMore.set(false)
                        }else if(t.datas.isNotEmpty()){
                            val current = it.getMaxPosition()
                            for(item in t.datas){
                                it.add(ItemSubjectBean(item,this@SubjectManagerViewModel))
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

    override fun onDelete(position: Int) {
        adapter.get()?.notifyDelete(position)
    }

    override fun onPutAway(position: Int) {
        if (isPutway.isNotEmpty()){
            adapter.get()?.notifyDelete(position)
        }
    }

    override fun onRefresh(bean : BaseBean) {
        bean.context?.let {
            if (it is SubjectManagerActivity){
                it.binding.smartLayout.autoRefresh()
            }
        }
    }

}