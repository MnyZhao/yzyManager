package com.idolmedia.yzymanager.view.freight

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.databinding.ActivityFreightAddressBinding
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.freight.FreightAddressViewModel
import com.idolmedia.yzymanager.viewmodel.freight.ItemFreightAddressAreaBean
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import java.lang.StringBuilder

/**
 *  时间：2020/10/26-18:23
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.freight FreightAddActivity
 *  描述：添加运费添加指定地区页Activity
 */
class FreightAddressActivity : BaseStateActivity<ActivityFreightAddressBinding>(),OnRefreshListener {
    override fun getLayoutId() = R.layout.activity_freight_address

    override fun getViewModel() = ViewModelProvider(this).get(FreightAddressViewModel::class.java)

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("地区选择","保存",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {

                binding.viewModel?.adapter?.get()?.let { adapter->
                    val areaIds = StringBuilder()
                    val areaNames = StringBuilder()
                    for(item in adapter.getDate()){
                        if (item is ItemFreightAddressAreaBean){
                            item.city?.let {
                                if (item.isSelect.get() && !item.isChecked.get()){
                                    if (areaIds.toString().isEmpty()){
                                        areaIds.append(it.city_code)
                                        areaNames.append(it.city_name)
                                    }else{
                                        areaIds.append(","+it.city_code)
                                        areaNames.append("、"+it.city_name)
                                    }
                                }
                            }
                        }
                    }

                    if (areaIds.toString().isNotEmpty()){
                        val intent = Intent()
                        intent.putExtra("areaIds",areaIds.toString())
                        intent.putExtra("areaNames",areaNames.toString())
                        setResult(1,intent)
                        finish()
                    }else{
                        ToastUtil.show("请选择城市")
                    }
                }

            }
        }))
    }

    override fun initView(savedInstanceState: Bundle?) {

        binding.viewModel?.areaIds = intent.getStringExtra("areaIds") ?: ""
        binding.viewModel?.checkedIds = intent.getStringExtra("checkedIds") ?: "" //编辑时不可改变的id
        binding.smartLayout.setOnRefreshListener(this)
        binding.smartLayout.isEnableLoadmore = false
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setItemViewCacheSize(100)
        ItemDecorationCommon.addItemDecoration(1,R.color.gray_e,binding.recyclerView)
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.smartLayout.autoRefresh()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        binding.viewModel?.queryFreightAddress()
    }

}