package com.idolmedia.yzymanager.view.subject

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.*
import com.idolmedia.yzymanager.databinding.ActivitySubjectAddBinding
import com.idolmedia.yzymanager.interfaces.OnItemSwitchListener
import com.idolmedia.yzymanager.model.bean.SubjectBean
import com.idolmedia.yzymanager.model.entity.SubjectSaveEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.idolmedia.yzymanager.viewmodel.common.ItemLineBean
import com.idolmedia.yzymanager.viewmodel.common.ItemTitleBean
import com.idolmedia.yzymanager.viewmodel.order.ItemOrderDetailsBean
import com.idolmedia.yzymanager.viewmodel.subject.ItemSaveBean
import com.idolmedia.yzymanager.viewmodel.subject.ItemSubjectAddImageBean
import com.idolmedia.yzymanager.viewmodel.subject.SubjectAddViewModel
import com.idolmedia.yzymanager.widget.CustomLoading

/**
 *  时间：2020/11/19-14:48
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.subject SubjectAddActivity
 *  描述：添加专题页Activity
 */
class SubjectAddActivity : BaseStateActivity<ActivitySubjectAddBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_subject_add
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(SubjectAddViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("添加专题"))
        binding.viewModel?.bean = SubjectBean()
    }

    override fun initView(savedInstanceState: Bundle?) {
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setItemViewCacheSize(30)
        binding.viewModel?.adapter?.set(BaseAdapter(this).apply {
            setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
                override fun itemClick(position: Int, item: BaseBean, parent: View) {
                    if (item is ItemCommodityAddBean){
                        if (item.strTitle.get() == "专题分类"){
                            val intent = Intent(this@SubjectAddActivity,SubjectClassifyActivity::class.java)
                            intent.putExtra("subjectCatalogId",binding.viewModel?.bean?.subjectCatalogId ?: "")
                            startActivityForResult(intent,0)
                        }
                        if (item.strTitle.get() == "专题标签"){
                            val intent = Intent(this@SubjectAddActivity,SubjectLabelActivity::class.java)
                            intent.putExtra("json",binding.viewModel?.bean?.subjectLabels ?: "")
                            startActivityForResult(intent,1)
                        }
                        if (item.strTitle.get() == "专题内容"){
                            val intent = Intent(this@SubjectAddActivity,SubjectContentActivity::class.java)
                            intent.putExtra("json",binding.viewModel?.bean?.subjectDetail ?: "")
                            startActivityForResult(intent,2)
                        }
                        if (item.strTitle.get() == "添加国内商品"){
                            val intent = Intent(this@SubjectAddActivity,SubjectCommodityAddActivity::class.java)
                            intent.putExtra("json",binding.viewModel?.bean?.commodityList ?: "")
                            intent.putExtra("freightId",binding.viewModel?.bean?.freightId)
                            intent.putExtra("isMergeExpress",binding.viewModel?.bean?.isMergeExpress)
                            intent.putExtra("subjectId",binding.viewModel?.subjectId)
                            startActivityForResult(intent,3)
                        }
                        if (item.strTitle.get() == "添加海外商品"){
                            val intent = Intent(this@SubjectAddActivity,SubjectCommodityAddActivity::class.java)
                            intent.putExtra("json",binding.viewModel?.bean?.commodityOverseasList ?: "")
                            intent.putExtra("freightId",binding.viewModel?.bean?.overseasFreightId)
                            intent.putExtra("isMergeExpress",binding.viewModel?.bean?.isMergeExpress)
                            intent.putExtra("isOverseas",true)
                            intent.putExtra("subjectId",binding.viewModel?.subjectId)
                            startActivityForResult(intent,4)
                        }
                        if (item.strTitle.get() == "添加电子刊"){
                            val intent = Intent(this@SubjectAddActivity,SubjectBookActivity::class.java)
                            intent.putExtra("json",binding.viewModel?.bean?.bookList ?: "")
                            startActivityForResult(intent,5)
                        }
                    }

                }
            })

        })

        binding.viewModel?.adapter?.get()?.let {

            it.add(ItemTitleBean("专题详情",R.color.white))
            it.add(ItemOrderDetailsBean("专题名称","","请输入专题名称"))
            it.add(ItemCommodityAddBean("专题分类",""))
            it.add(ItemCommodityAddBean("专题标签",""))
            it.add(ItemCommodityAddBean("专题内容",""))
            it.add(ItemSubjectAddImageBean(binding.viewModel))
            it.add(ItemLineBean(12,R.color.bg_fa))
            it.add(ItemCommodityAddBean("是否合并运费",false,object : OnItemSwitchListener {
                override fun onItemSwitch(position: Int, bean: BaseBean, switch: Boolean) {
                    if (switch){
                        binding.viewModel?.bean?.isMergeExpress = "1"
                    }else{
                        binding.viewModel?.bean?.isMergeExpress = "0"
                    }
                }
            }))
            it.add(ItemCommodityAddBean("添加国内商品",""))
            it.add(ItemCommodityAddBean("添加海外商品",""))
            it.add(ItemCommodityAddBean("添加电子刊",""))
            it.add(ItemSaveBean { view ->

                val map = binding.viewModel?.getSaveMap()
                map?.let {
                    CustomLoading.getInstance().showLoad(this)
                    RetrofitHelper.instance().saveSubject(map,object : BaseObserver<SubjectSaveEntity>(){
                        override fun onSuccess(t: SubjectSaveEntity) {
                            if (t.resultCode==1){
                                val pop = BasePopWindow(this@SubjectAddActivity,R.layout.pop_common_content_check)
                                pop.popModel = BasePopViewModel().apply {
                                    content.set("专题发布成功")
                                    listener = object : BasePopWindow.OnPopClickListener{
                                        override fun onPopLeft(model: BaseViewModel) {
                                        }

                                        override fun onPopRight(model: BaseViewModel) {
                                            pop.dismiss()
                                            startActivity(Intent(this@SubjectAddActivity,SubjectManagerActivity::class.java))
                                            finish()
                                        }
                                    }
                                }
                                pop.showAtCenter(view)
                            }
                            else if(t.resultCode==625){
                                saveSubject(t.shopcommonId,t.msg,map)
                            }
                        }

                        override fun onError(msg: String) {
                            ToastUtil.show(msg)
                        }

                        override fun onComplete() {
                            super.onComplete()
                            CustomLoading.getInstance().closeLoad()
                        }
                    })
                }
            })

            it.notifyDataSetChanged()
        }
    }

    private fun saveSubject(shopCommonId:String,msg:String,map:HashMap<String,Any?>){
        val pop = BasePopWindow(this,R.layout.pop_common_content_check)
        pop.popModel = BasePopViewModel().apply {
            content.set(msg)
            listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                }

                override fun onPopRight(model: BaseViewModel) {
                    pop.dismiss()
                    CustomLoading.getInstance().showLoad(this@SubjectAddActivity)
                    map["existIsMergeOrder"] = "1"
                    map["existIsMergeShopcommonIds"] = shopCommonId
                    RetrofitHelper.instance().saveSubject(map,object : BaseObserver<SubjectSaveEntity>(){
                        override fun onSuccess(t: SubjectSaveEntity) {
                            if (t.resultCode==1){
                                val pop = BasePopWindow(this@SubjectAddActivity,R.layout.pop_common_content_check)
                                pop.popModel = BasePopViewModel().apply {
                                    content.set("专题发布成功")
                                    listener = object : BasePopWindow.OnPopClickListener{
                                        override fun onPopLeft(model: BaseViewModel) {
                                        }

                                        override fun onPopRight(model: BaseViewModel) {
                                            pop.dismiss()
                                            startActivity(Intent(this@SubjectAddActivity,SubjectManagerActivity::class.java))
                                            finish()
                                        }
                                    }
                                }
                                pop.showAtCenter(binding.recyclerView)
                            }
                        }

                        override fun onError(msg: String) {
                            ToastUtil.show(msg,2000)
                        }

                        override fun onComplete() {
                            super.onComplete()
                            CustomLoading.getInstance().closeLoad()
                        }
                    })
                }
            }
        }
        pop.showAtCenter(binding.recyclerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.viewModel?.onActivityResult(requestCode, resultCode, data)
    }

}