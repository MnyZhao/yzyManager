package com.idolmedia.yzymanager.view.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
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
 *  描述：基本信息页Activity
 */
class MineInformationActivity : BaseStateActivity<ActivityMineInformationBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_mine_information
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(MineInformationViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("基本信息"))
    }

    override fun initView(savedInstanceState: Bundle?) {
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        val user = SpManager.getUserEntity()
        user?.let {

            binding.viewModel?.adapter?.get()?.let {
                it.add(ItemCommodityAddBean("店铺LOGO","",true).apply {
                    layoutId = R.layout.item_commodity_add_head
                    strImg.set(user.datas.headImg)
                })
                it.add(ItemCommodityAddBean("店铺名称",user.datas.nickName))
                it.add(ItemCommodityAddBean("店铺ID",user.datas.userId))

                val attestationType = when(user.datas.attestationType){
                    "-1" ->"管理员"
                    "0" ->"第三方认证"
                    "1" ->"粉丝团认证"
                    "2" ->"一直娱认证"
                    else ->""
                }
                it.add(ItemCommodityAddBean("店铺类型",attestationType))

                it.notifyDataSetChanged()

            }


        }



    }

}