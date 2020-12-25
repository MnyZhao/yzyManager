package com.idolmedia.yzymanager.view.main

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
import com.idolmedia.yzymanager.databinding.ActivityMineInformationBinding
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.idolmedia.yzymanager.viewmodel.main.MineInformationViewModel

/**
 *  时间：2020/11/25-17:01
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.main MineInformationActivity
 *  描述：设置页Activity
 */
class MineSettingActivity : BaseStateActivity<ActivityMineInformationBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_mine_information
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(MineInformationViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("设置"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val user = SpManager.getUserEntity()

        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.viewModel?.adapter?.set(BaseAdapter(this).apply {
            setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
                override fun itemClick(position: Int, item: BaseBean, parent: View) {
                    if (item is ItemCommodityAddBean){
                        if (item.strTitle.get() == "修改密码"){
                            startActivity(Intent(this@MineSettingActivity,PasswordChangeActivity::class.java))
                        }
                        else if (item.strTitle.get() == "更改主账号"){
                            val intent = Intent(this@MineSettingActivity,AccountChangeActivity::class.java)
                            if (user?.datas?.accountType == "phone"){
                                intent.putExtra("accountType","phone")
                            }
                            intent.putExtra("account",user?.datas?.username ?: "")
                            startActivityForResult(intent,0)
                        }
                    }
                }
            })
        })

        user?.let {entity->
            binding.viewModel?.adapter?.get()?.let {
                it.add(ItemCommodityAddBean("修改密码","******"))
                it.add(ItemCommodityAddBean("更改主账号",entity.datas.username))
                it.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1&&requestCode==0){
            val account = data?.getStringExtra("account") ?: ""
            binding.viewModel?.adapter?.get()?.let {
                ( it.getDate()[1] as ItemCommodityAddBean).strContent.set(account)
            }
        }
    }

}