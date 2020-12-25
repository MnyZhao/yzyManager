package com.idolmedia.yzymanager.view.main

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.*
import com.idolmedia.yzymanager.databinding.FragmentMainMineBinding
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.view.login.LoginActivity
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.idolmedia.yzymanager.viewmodel.main.ItemMineHeadBean
import com.idolmedia.yzymanager.viewmodel.main.MainMineViewModel

/**
 *  时间：2020/11/25-15:54
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.main MineFragment
 *  描述：
 */
class MineFragment : BaseFragment<FragmentMainMineBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_main_mine
    }

    override fun getViewModel(): BaseViewModel {
        return ViewModelProvider(this).get(MainMineViewModel::class.java)
    }

    override fun initViewOrData() {

        ItemDecorationCommon.addItemDecoration(1,binding?.recyclerView!!)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.viewModel?.adapter?.set(BaseAdapter(context!!).apply {
            setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
                override fun itemClick(position: Int, item: BaseBean, parent: View) {
                    if (item is ItemCommodityAddBean){
                        if (item.strTitle.get() == "设置"){
                            startActivity(Intent(activity,MineSettingActivity::class.java))
                        }else if(item.strTitle.get() == "退出"){
                            val pop = BasePopWindow(activity,R.layout.pop_common_content)
                            val popModel = BasePopViewModel()
                            popModel.content.set("确定退出该账号吗？")
                            popModel.listener = object : BasePopWindow.OnPopClickListener{
                                override fun onPopLeft(model: BaseViewModel) {
                                    pop.dismiss()
                                }

                                override fun onPopRight(model: BaseViewModel) {
                                    pop.dismiss()
                                    SpManager.logout()
                                    startActivity(Intent(activity,LoginActivity::class.java))
                                    activity?.finish()
                                }

                            }
                            pop.popModel = popModel
                            pop.showAtCenter(binding?.recyclerView)
                        }
                    }
                }

            })
        })

        binding?.viewModel?.adapter?.get()?.let {

            it.add(ItemMineHeadBean())
            it.add(ItemCommodityAddBean("设置",""))
            it.add(ItemCommodityAddBean("退出",""))

            it.notifyDataSetChanged()

        }

    }

    override fun onResume() {
        super.onResume()
        binding?.viewModel?.adapter?.get()?.let {
            for(item in it.getDate()){
                if (item is ItemMineHeadBean){
                    item.refresh()
                    break
                }
            }
        }
    }


}