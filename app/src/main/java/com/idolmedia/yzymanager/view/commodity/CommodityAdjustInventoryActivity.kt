package com.idolmedia.yzymanager.view.commodity

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommodityAdjustInventoryBinding
import com.idolmedia.yzymanager.utils.Dp2PxUtils
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityAdjustInventoryViewModel

/**
 *  时间：2020/10/22-15:13
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodiy CommodityAdjustInventoryActivity
 *  描述：调整库存页Activity
 */
class CommodityAdjustInventoryActivity : BaseStateActivity<ActivityCommodityAdjustInventoryBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_adjust_inventory
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityAdjustInventoryViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("调整库存"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val id = intent.getStringExtra("id") ?: ""
        binding.viewModel?.id = id
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = Dp2PxUtils.dip2px(14).toInt()
            }
        })
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.smartLayout.setOnRefreshListener { binding.viewModel?.queryCommodityStore() }
        binding.smartLayout.isEnableLoadmore = false
        binding.smartLayout.autoRefresh()

    }

}