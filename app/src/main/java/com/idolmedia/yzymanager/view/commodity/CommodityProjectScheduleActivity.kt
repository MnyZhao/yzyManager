package com.idolmedia.yzymanager.view.commodity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommodityProjectScheduleBinding
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityProjectScheduleViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemProjectScheduleBean

/**
 *  时间：2020/10/26-14:32
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityProjectScheduleEditActivity
 *  描述：项目进度页Activity
 */
class CommodityProjectScheduleActivity : BaseStateActivity<ActivityCommodityProjectScheduleBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_project_schedule
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityProjectScheduleViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("编辑项目进度","保存",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
            }

        }))
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.viewModel?.adapter?.get()?.let {

            it.add(ItemProjectScheduleBean())
            it.add(ItemProjectScheduleBean())
            it.add(ItemProjectScheduleBean())

        }

    }

}