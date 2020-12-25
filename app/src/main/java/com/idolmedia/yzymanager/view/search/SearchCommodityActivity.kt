package com.idolmedia.yzymanager.view.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivitySearchCommodityBinding
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.search.SearchCommodityViewModel

/**
 *  时间：2020/10/22-17:13
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.search SearchCommodityActivity
 *  描述：商品管理搜索页Activity
 */
class SearchCommodityActivity : BaseStateActivity<ActivitySearchCommodityBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_search_commodity
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(SearchCommodityViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("搜索"))
        binding.viewModel?.putaway = intent.getIntExtra("putaway",0)
        binding.viewModel?.isAudit = intent.getBooleanExtra("isAudit",false)
        binding.viewModel?.isSubject = intent.getBooleanExtra("isSubject",false)
        if (binding.viewModel?.isSubject == true){
            binding.viewModel?.strSearchType?.set("专题ID")
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

        binding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                view?.let {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH){
                        if (view.text.toString().isNotEmpty()){
                            binding.smartLayout.autoRefresh()
                            hideKeyboard(view)
                            return true
                        }
                    }
                }
                return false
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.smartLayout.setOnRefreshListener {
            binding.viewModel?.pageNo = 1
            binding.viewModel?.search()
        }
        binding.smartLayout.setOnLoadmoreListener {
            binding.viewModel!!.pageNo += 1
            binding.viewModel?.search()
        }

    }

}