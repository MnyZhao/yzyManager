package com.idolmedia.yzymanager.view.commodity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommodityManageSingleBinding
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.interfaces.OnResultListener
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityManageSingleViewModel

/**
 *  时间：2020/11/19-11:05
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityManageSingleActivity
 *  描述：审核单条商品页Activity
 */
class CommodityManageSingleActivity : BaseStateActivity<ActivityCommodityManageSingleBinding>(),OnItemDeleteListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_manage_single
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityManageSingleViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.isPassed = intent.getBooleanExtra("isPassed",false)
        binding.viewModel?.onDeleteListener = this
        binding.viewModel?.titleBar?.set(TitleBarViewModel("商品管理"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        binding.viewModel?.commodityId = intent.getStringExtra("commodityId") ?: ""

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.viewModel?.adapter?.set(BaseAdapter(this))
        binding.smartLayout.setOnRefreshListener { binding.viewModel?.queryCommodity()}
        binding.smartLayout.isEnableLoadmore = false

        binding.viewModel?.onPassedListener = object : OnResultListener{
            override fun onResultSuccess(entity: BaseEntity) {
                val intent = Intent()
                intent.putExtra("clickType","1")
                setResult(1,intent)
                finish()
            }
        }

        binding.viewModel?.onRefuseListener = object : OnResultListener{
            override fun onResultSuccess(entity: BaseEntity) {
                val intent = Intent()
                intent.putExtra("clickType","2")
                setResult(1,intent)
                finish()
            }
        }

        binding.smartLayout.autoRefresh()

    }

    override fun onItemDelete(position: Int, bean: BaseBean) {
        binding.viewModel?.adapter?.get()?.notifyDelete(position)
        val intent = Intent()
        intent.putExtra("clickType","3")
        setResult(1,intent)
    }

}