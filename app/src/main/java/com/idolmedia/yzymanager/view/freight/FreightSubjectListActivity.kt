package com.idolmedia.yzymanager.view.freight

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
import com.idolmedia.yzymanager.databinding.ActivityFreightSubjectListBinding
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.freight.FreightViewModel
import com.idolmedia.yzymanager.viewmodel.freight.ItemFreightBean

/**
 *  时间：2020/10/26-18:01
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.freight FreightListActivity
 *  描述：专题选择运费模板页Activity
 */
class FreightSubjectListActivity : BaseStateActivity<ActivityFreightSubjectListBinding>(),OnItemSelectListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_freight_subject_list
    }

    override fun getViewModel(): BaseViewModel? {
        val viewModel = ViewModelProvider(this).get(FreightViewModel::class.java)
        viewModel.onItemSelectListener = this
        return viewModel
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("运费模板"))
        binding.viewModel?.isAdmin = intent.getBooleanExtra("isAdmin",false)
        binding.tvSave.visibility = View.VISIBLE
        binding.tvSave.setOnClickListener {
            val intent = Intent(this, FreightAddActivity::class.java)
            intent.putExtra("isOverseas", SpManager.isOverseasSystem())
            intent.putExtra("isMergeExpress","1")
            startActivityForResult(intent,0)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

        val freightId = intent.getStringExtra("freightId") ?: ""
        binding.viewModel?.freightId = freightId
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        ItemDecorationCommon.addItemDecoration(14,binding.recyclerView)

        binding.viewModel?.adapter?.set(BaseAdapter(this))

        val isOverseas = intent.getBooleanExtra("isOverseas",false)

        binding.smartLayout.setOnRefreshListener {
            binding.viewModel?.pageNo=1
            binding.viewModel?.querySubjectFreight(isOverseas)
        }

        binding.smartLayout.setOnLoadmoreListener {
            binding.viewModel!!.pageNo+=1
            binding.viewModel!!.querySubjectFreight(isOverseas)
        }

        binding.smartLayout.autoRefresh()

    }

    override fun onItemSelect(position: Int, bean: BaseBean) {
        binding.viewModel?.adapter?.get()?.let {

            for(item in it.getDate()){
                item as ItemFreightBean
                item.isSelect.set(false)
            }

            bean as ItemFreightBean
            bean.isSelect.set(true)

            val intent = Intent()
            intent.putExtra("freightId",bean.freight.freightId)
            intent.putExtra("freightName",bean.freight.name)
            setResult(1,intent)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1){
            binding.smartLayout.autoRefresh()
        }
    }

}