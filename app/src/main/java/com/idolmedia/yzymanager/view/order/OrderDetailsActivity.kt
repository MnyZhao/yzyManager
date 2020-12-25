package com.idolmedia.yzymanager.view.order

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
import com.idolmedia.yzymanager.databinding.ActivityOrderDetailsBinding
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.order.*

/**
 *  时间：2020/10/20-18:01
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.order OrderDetailsActivity
 *  描述：订单详情页Activity
 */
class OrderDetailsActivity : BaseStateActivity<ActivityOrderDetailsBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_order_details
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(OrderDetailsViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("订单详情"))
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.viewModel?.orderId = intent.getStringExtra("orderId") ?: ""

        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setItemViewCacheSize(30)
        binding.viewModel?.adapter?.set(BaseAdapter(this).apply {
            setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
                override fun itemClick(position: Int, item: BaseBean, parent: View) {
                    if (item is ItemOrderDetailsBean){
                        if (item.strTitle.get() == "身份证信息"){
                            if (item.strContent.get() != "暂无"){
                                val intent = Intent(this@OrderDetailsActivity,OrderIdentityActivity::class.java)
                                intent.putExtra("name",binding.viewModel?.orderEntity?.name)
                                intent.putExtra("number",binding.viewModel?.orderEntity?.idCard)
                                intent.putExtra("img1",binding.viewModel?.orderEntity?.frontIdcardImg)
                                intent.putExtra("img2",binding.viewModel?.orderEntity?.backIdcardImg)
                                startActivity(intent)
                            }
                        }
                    }
                }
            })
        })
        binding.smartLayout.setOnRefreshListener { binding.viewModel?.queryOrderDetails() }
        binding.smartLayout.isEnableLoadmore = false
        binding.smartLayout.autoRefresh()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0 && resultCode==1){
            binding.smartLayout.autoRefresh()
        }
    }

}