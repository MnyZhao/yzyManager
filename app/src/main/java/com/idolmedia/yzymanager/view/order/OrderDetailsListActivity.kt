package com.idolmedia.yzymanager.view.order

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityOrderDetailsBinding
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.order.*

/**
 *  时间：2020/10/20-18:01
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.order OrderDetailsActivity
 *  描述：订单详情多单列表页Activity
 */
class OrderDetailsListActivity : BaseStateActivity<ActivityOrderDetailsBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_order_details
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(OrderDetailsViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("订单详情列表"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        binding.viewModel?.orderId = intent.getStringExtra("orderId") ?: ""

        binding.smartLayout.setOnRefreshListener {
            binding.viewModel?.pageNo = 1
            binding.viewModel?.queryOrderCommodity()
        }
        binding.smartLayout.isEnableLoadmore = false
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setItemViewCacheSize(30)
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.smartLayout.autoRefresh()

    }

}