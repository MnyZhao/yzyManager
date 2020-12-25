package com.idolmedia.yzymanager.view.subject

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivitySubjectClassifyBinding
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddSelectBean
import com.idolmedia.yzymanager.viewmodel.subject.SubjectAddViewModel

/**
 *  时间：2020/11/19-15:45
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.subject SubjectClassifyActivity
 *  描述：专题分类页Activity
 */
class SubjectClassifyActivity : BaseStateActivity<ActivitySubjectClassifyBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_subject_classify
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(SubjectAddViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("专题分类"))
        binding.viewModel?.subjectCatalogId = intent.getStringExtra("subjectCatalogId") ?: ""
    }

    override fun initView(savedInstanceState: Bundle?) {
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.viewModel?.adapter?.set(BaseAdapter(this).apply {
            setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
                override fun itemClick(position: Int, item: BaseBean, parent: View) {
                    val intent = Intent()
                    item as ItemCommodityAddSelectBean
                    intent.putExtra("id",item.id)
                    intent.putExtra("title",item.strTitle.get() ?: "")
                    setResult(1,intent)
                    finish()
                }
            })
        })

        binding.smartLayout.setOnRefreshListener { binding.viewModel?.querySubjectClassify() }
        binding.smartLayout.isEnableLoadmore = false
        binding.smartLayout.autoRefresh()

    }

}