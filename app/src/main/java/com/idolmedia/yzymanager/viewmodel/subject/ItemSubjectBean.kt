package com.idolmedia.yzymanager.viewmodel.subject

import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.*
import com.idolmedia.yzymanager.interfaces.OnSubjectListener
import com.idolmedia.yzymanager.model.entity.SubjectListEntity
import com.idolmedia.yzymanager.model.entity.SubjectSortEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.CopyUtil
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.subject.SubjectCommodityActivity
import com.idolmedia.yzymanager.view.subject.SubjectEditActivity

/**
 *  时间：2020/10/19-14:14
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.subject ItemSubjectBean
 *  描述：
 */
class ItemSubjectBean(val item : SubjectListEntity.Item) : BaseBean() {

    override fun getViewType(): Int {
        return R.layout.item_subject
    }

    constructor(item : SubjectListEntity.Item, onSubjectListener: OnSubjectListener):this(item){
        this.onSubjectListener = onSubjectListener
    }

    val strId = ObservableField<String>()
    val strTitle = ObservableField<String>()
    val strImg = ObservableField<String>()
    val strName = ObservableField<String>()
    val strClassify = ObservableField<String>()
    val strPrice = ObservableField<String>()
    val strUpStatus = ObservableField<String>()
    var onSubjectListener: OnSubjectListener? = null

    init {
        strId.set("专题ID:${item.subjectId}")
        strName.set(item.subjectName)
        strClassify.set("分类:${item.sbjectCatalogTitle}")
        strPrice.set("添加时间:${item.createTimeStr}")
        strImg.set(item.previewUrl)
        strTitle.set(item.subjectDesc)

        if (item.isPutway == "0"){
            strUpStatus.set("下架")
        }else{
            strUpStatus.set("上架")
        }


    }

    fun onClickCopy(view:View){
        CopyUtil.copy(view.context,item.subjectId)
    }

    fun onClickDown(view:View){

        val putAway = if (item.isPutway == "0") "1" else "0"

        RetrofitHelper.instance().saveSubjectPutAway(item.subjectId,putAway,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("操作成功")
                    item.isPutway = putAway
                    if (item.isPutway == "0"){
                        strUpStatus.set("下架")
                    }else{
                        strUpStatus.set("上架")
                    }
                    onSubjectListener?.onPutAway(position)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    /**点击事件-设置排序*/
    fun onClickSettingSort(view:View){
        RetrofitHelper.instance().querySubjectSort(item.subjectId,item.sortNo,object : BaseObserver<SubjectSortEntity>(){
            override fun onSuccess(t: SubjectSortEntity) {
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
                            saveSubjectSort(t.currPlace,popModel.title.get()?: "")
                            pop.dismiss()
                        }
                    }
                    pop.popModel = popModel
                    pop.showAtCenter(view)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }
        })
    }

    /**设置商品排序*/
    fun onClickCommoditySettingSort(view:View){
        val intent = Intent(view.context,SubjectCommodityActivity::class.java)
        intent.putExtra("subjectId",item.subjectId)
        view.context.startActivity(intent)
    }

    fun onClickEdit(view:View){
        val intent = Intent(view.context,SubjectEditActivity::class.java)
        intent.putExtra("subjectId",item.subjectId)
        view.context.startActivity(intent)
    }

    fun onClickDelete(view:View){

        if (item.isMergeExpress == "1"){
            ToastUtil.show("合并邮费专题禁止删除")
            return
        }

        val pop = BasePopWindow(view.context,R.layout.pop_common_content)
        val popModel = BasePopViewModel()
        popModel.content.set("确定删除该专题吗？")
        popModel.listener = object : BasePopWindow.OnPopClickListener{
            override fun onPopLeft(model: BaseViewModel) {
                pop.dismiss()
            }

            override fun onPopRight(model: BaseViewModel) {
                pop.dismiss()
                deleteSubject()
            }

        }
        pop.popModel = popModel
        pop.showAtCenter(view)

    }

    private fun deleteSubject(){
        RetrofitHelper.instance().deleteSubject(item.subjectId,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    onSubjectListener?.onDelete(position)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    private fun saveSubjectSort(currPlace:String,sortNo:String){
        RetrofitHelper.instance().saveSubjectSort(currPlace,sortNo,item.subjectId,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("操作成功")
                    onSubjectListener?.onRefresh(this@ItemSubjectBean)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

}