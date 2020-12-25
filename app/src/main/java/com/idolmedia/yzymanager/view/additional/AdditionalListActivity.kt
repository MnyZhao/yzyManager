package com.idolmedia.yzymanager.view.additional

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityAdditionalListBinding
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityAdditionalViewModel

/**
 *  时间：2020/10/27-10:55
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddAdditionalActivity
 *  描述：公共模板附加信息页Activity
 *  公共模板不可操作，只能进行选择
 */
class AdditionalListActivity : BaseStateActivity<ActivityAdditionalListBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_additional_list
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityAdditionalViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("附加信息","保存",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                if (binding.viewModel?.toSave() == true){
                    setResult(1,binding.viewModel?.getResultIntent())
                    finish()
                }
            }
        }))
    }

    override fun initView(savedInstanceState: Bundle?) {

        //上个页面已有的附加信息key字段 json格式 以[]包含
        binding.viewModel?.keys = intent.getStringExtra("keys") ?: ""

        ItemDecorationCommon.addItemDecoration(14,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.smartLayout.setOnRefreshListener {
            binding.viewModel?.queryAddition()
        }
        binding.smartLayout.isEnableLoadmore = false
        binding.smartLayout.autoRefresh()

    }

}