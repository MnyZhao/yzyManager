package com.idolmedia.yzymanager.view.subject

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivitySubjectManagerBinding
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.view.search.SearchCommodityActivity
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.subject.SubjectManagerViewModel

/**
 *  时间：2020/10/19-13:39
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.subject SubjectManagerActivity
 *  描述：专题管理页Activity
 */
class SubjectManagerActivity : BaseStateActivity<ActivitySubjectManagerBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_subject_manager
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(SubjectManagerViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("专题管理",R.mipmap.ic_search,object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                val intent = Intent(this@SubjectManagerActivity,SearchCommodityActivity::class.java)
                intent.putExtra("isSubject",true)
                startActivity(intent)
            }

        }))
    }

    override fun initView(savedInstanceState: Bundle?) {
        ItemDecorationCommon.addItemDecoration(14,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setItemViewCacheSize(30)
        binding.smartLayout.setOnRefreshListener {
            binding.viewModel?.pageNo = 1
            binding.viewModel?.querySubject()
        }
        binding.smartLayout.setOnLoadmoreListener {
            binding.viewModel!!.pageNo += 1
            binding.viewModel?.querySubject()
        }
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.smartLayout.post {
            binding.smartLayout.autoRefresh()
        }
    }

}