package com.idolmedia.yzymanager.view.commodity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommoditySortBinding
import com.idolmedia.yzymanager.interfaces.OnSortListener
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommoditySortViewModel

/**
 *  时间：2020/11/27-16:44
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommoditySortActivity
 *  描述：
 */
class CommoditySortActivity : BaseStateActivity<ActivityCommoditySortBinding>(), OnSortListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_sort
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommoditySortViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("商品排序"))
        binding.viewModel?.sortListener = this
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.viewModel?.sortMainType = intent.getStringExtra("sortMainType") ?: ""

        ItemDecorationCommon.addItemDecoration(14,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.viewModel?.adapter?.set(BaseAdapter(this))
        binding.smartLayout.setOnRefreshListener {
            binding.viewModel?.let {
                it.pageNo = 1
                it.queryCommodity()
            }
        }
        binding.smartLayout.setOnLoadmoreListener {
            binding.viewModel?.let {
                it.pageNo+=1
                it.queryCommodity()
            }
        }
        binding.smartLayout.autoRefresh()

    }

    override fun onStick() {
        binding.smartLayout.autoRefresh()
    }

}