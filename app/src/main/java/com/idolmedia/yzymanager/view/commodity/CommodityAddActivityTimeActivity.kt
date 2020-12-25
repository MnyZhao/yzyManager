package com.idolmedia.yzymanager.view.commodity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommodityAddSelectBinding
import com.idolmedia.yzymanager.interfaces.OnItemSwitchListener
import com.idolmedia.yzymanager.utils.TimeUtils
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityAddMemberStatusViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean

/**
 *  时间：2020/10/26-14:57
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddActivity
 *  描述：添加商品活动时间页Activity
 */
class CommodityAddActivityTimeActivity : BaseStateActivity<ActivityCommodityAddSelectBinding>(),OnItemSwitchListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_add_select
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityAddMemberStatusViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("活动时间"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val reserveStatus = intent.getIntExtra("reserveStatus",0)
        val isLimitTime = intent.getBooleanExtra("isLimitTime",false)
        val startTime = intent.getStringExtra("startTime") ?: ""
        val endTime = intent.getStringExtra("endTime") ?: ""

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        val list = ArrayList<BaseBean>()
        list.add(ItemCommodityAddBean("是否有时间限制",if (reserveStatus==1) true else isLimitTime,this).apply {
            if (reserveStatus==1){
                canEdit = false
            }
        })
        list.add(ItemCommodityAddBean("活动开始时间",if (startTime.isEmpty()) "点击设置" else startTime))
        if (reserveStatus==1||isLimitTime){
            list.add(ItemCommodityAddBean("活动结束时间",if (endTime.isEmpty()) "点击设置" else endTime))
        }
        binding.viewModel?.adapter?.set(BaseAdapter(this,list).apply {
            setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
                override fun itemClick(position: Int, item: BaseBean, parent: View) {
                    //时间选择器
                    val pvTime = TimePickerBuilder(this@CommodityAddActivityTimeActivity) { date, view ->
                        val str = TimeUtils.getTime(date)
                        item as ItemCommodityAddBean
                        item.strContent.set(str)

                        setResult(1,binding.viewModel?.getIntent())

                    }
                        .setType(booleanArrayOf(true, true, true, true, true, true))
                        .setLineSpacingMultiplier(1.5f)
                        .build()
                    pvTime.show()
                }
            })
        })

    }

    override fun onItemSwitch(position: Int, bean: BaseBean, switch: Boolean) {
        if (switch) {
            binding.viewModel?.adapter?.get()?.add(ItemCommodityAddBean("活动结束结束","点击设置"))
            binding.viewModel?.adapter?.get()?.notifyDataSetChanged()
        }else{
            binding.viewModel?.adapter?.get()?.notifyDelete(2)
        }
        setResult(1,binding.viewModel?.getIntent())
    }


    override fun onBackPressed() {

        binding.viewModel?.let {
            it.adapter.get()?.let {
                val isOpen = (it.getDate()[0] as ItemCommodityAddBean).isOpen.get()
                val startTime = (it.getDate()[1] as ItemCommodityAddBean).strContent.get() ?: ""

                if (startTime.isEmpty() || startTime == "点击设置"){
                    ToastUtil.show("请设置活动开始时间")
                    return
                }

                if (isOpen){
                    val endTime = (it.getDate()[2] as ItemCommodityAddBean).strContent.get() ?: ""
                    if (endTime.isEmpty() || endTime == "点击设置"){
                        ToastUtil.show("请设置活动结束时间")
                        return
                    }
                }
            }
        }

        super.onBackPressed()
    }

}