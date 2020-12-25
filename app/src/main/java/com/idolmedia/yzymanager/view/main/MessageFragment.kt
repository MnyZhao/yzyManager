package com.idolmedia.yzymanager.view.main

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseFragment
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.FragmentMainMessageBinding
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.viewmodel.main.ItemMessageBean
import com.idolmedia.yzymanager.viewmodel.main.MainMessageViewModel

/**
 *  时间：2020/11/18-20:47
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.main MessageFragment
 *  描述：
 */
class MessageFragment : BaseFragment<FragmentMainMessageBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_main_message
    }

    override fun getViewModel(): BaseViewModel {
        return ViewModelProvider(this).get(MainMessageViewModel::class.java)
    }

    private var currentUserId = ""
    override fun initViewOrData() {

        currentUserId = SpManager.getUserEntity()?.datas?.userId ?: ""

        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.viewModel?.adapter?.set(BaseAdapter(context!!))

        binding?.smartLayout?.setOnRefreshListener {
            binding?.viewModel?.let {
                it.pageNo = 1
                it.queryHomeMessage()
            }
        }
        binding?.smartLayout?.setOnLoadmoreListener {
            binding?.viewModel?.let {
                it.pageNo += 1
                it.queryHomeMessage()
            }
        }

        binding?.viewModel?.queryHomeMessage()

    }

    override fun onResume() {
        super.onResume()
        if (currentUserId != SpManager.getUserEntity()?.datas?.userId){
            currentUserId = SpManager.getUserEntity()?.datas?.userId ?: ""
            binding?.viewModel?.queryHomeMessage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1){
            binding?.viewModel?.adapter?.get()?.let {
                val bean = it.getDate()[requestCode] as ItemMessageBean
                bean.item.clickType = data?.getStringExtra("clickType")  ?: ""
            }
        }
    }

}