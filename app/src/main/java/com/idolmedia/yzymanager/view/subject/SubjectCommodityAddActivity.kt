package com.idolmedia.yzymanager.view.subject

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivitySubjectCommodityAddBinding
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.view.freight.FreightSubjectListActivity
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.idolmedia.yzymanager.viewmodel.subject.SubjectCommodityAddViewModel
import kotlinx.android.synthetic.main.activity_subject_commodity_add.*
import kotlin.collections.ArrayList

/**
 *  时间：2020/10/19-16:38
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.subject SubjectTitleActivity
 *  描述：专题添加商品页Activity
 */
class SubjectCommodityAddActivity : BaseStateActivity<ActivitySubjectCommodityAddBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_subject_commodity_add
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(SubjectCommodityAddViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        val isOverseas = intent.getBooleanExtra("isOverseas",false)
        val isMergeExpress = intent.getStringExtra("isMergeExpress") ?: "0"
        binding.viewModel?.isOverseas = isOverseas
        binding.viewModel?.subjectId = intent.getStringExtra("subjectId") ?: ""
        binding.viewModel?.titleBar?.set(TitleBarViewModel(if (isOverseas) "添加海外商品" else "添加国内商品","保存",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                if (isMergeExpress == "1" && (binding.viewModel?.freightId.isNullOrEmpty() || binding.viewModel?.freightId == "0")){
                    ToastUtil.show("请选择邮费模板,如无需选择邮费模板可关闭合并邮费模板",3000)
                    return
                }
                if (isMergeExpress == "1" && binding.viewModel?.selectList.isNullOrEmpty()){
                    ToastUtil.show("请选择商品")
                    return
                }
                val intent = Intent()
                intent.putExtra("json",Gson().toJson(binding.viewModel?.selectList))
                intent.putExtra("freightId",binding.viewModel?.freightId)
                setResult(1,intent)
                finish()
            }

        }))

        val json = intent.getStringExtra("json") ?: ""
        val type = object : TypeToken<ArrayList<CommodityListEntity.Data>>(){}.type
        val selectList = Gson().fromJson<ArrayList<CommodityListEntity.Data>>(json,type)
        selectList?.let {
            binding.viewModel?.selectList?.addAll(it)
            binding.viewModel?.strCountTip?.set("已选中${it.size}件商品")

            for(item in it){
                binding.viewModel?.selectIds?.append("[${item.shopcommonId}]")
            }

        }

        binding.viewModel?.statusBean?.set(ItemCommodityAddBean("商品属性",if (isOverseas) "海外商品" else "国内商品").apply {
            visibleMoreButton.set(View.INVISIBLE)
        })

        val freightId = intent.getStringExtra("freightId") ?: ""
        binding.viewModel?.freightId = freightId

        if (isMergeExpress == "1"){
            binding.viewModel?.freightBean?.set(ItemCommodityAddBean(if (isOverseas) "海外运费模板" else "国内运费模板", if (freightId.isEmpty() || freightId == "0") "未选择" else "已选择"))
            include_freight.setOnClickListener {
                val intent = Intent(this,FreightSubjectListActivity::class.java)
                intent.putExtra("freightId",binding.viewModel?.freightId)
                intent.putExtra("isOverseas",isOverseas)
                startActivityForResult(intent,0)
            }
        }else{
            include_freight.visibility = View.GONE
        }

        binding.tvSearch.setOnClickListener {
            binding.smartLayout.autoRefresh()
        }

    }

    override fun initView(savedInstanceState: Bundle?) {
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setItemViewCacheSize(30)
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.smartLayout.setOnRefreshListener {
            binding.viewModel?.let {
                it.pageNo = 1
                it.querySubjectCommodity()
            }
        }
        binding.smartLayout.setOnLoadmoreListener {
            binding.viewModel?.let {
                it.pageNo += 1
                it.querySubjectCommodity()
            }
        }
        binding.smartLayout.autoRefresh()

        binding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                view?.let {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH){
                        binding.viewModel?.let {model->
                            model.pageNo = 1
                            model.querySubjectCommodity()
                        }
                        hideKeyboard(view)
                        return true
                    }
                }
                return false
            }
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0 && resultCode==1){
            val freightId = data?.getStringExtra("freightId") ?: ""
            val freightName = data?.getStringExtra("freightName") ?: ""
            binding.viewModel?.freightBean?.get()?.strContent?.set(freightName)
            binding.viewModel?.freightId = freightId
        }
    }

}