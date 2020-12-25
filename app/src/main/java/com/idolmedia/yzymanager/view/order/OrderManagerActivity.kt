package com.idolmedia.yzymanager.view.order

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.ViewPager2Adapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityOrderManagerBinding
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.TimeUtils
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.order.OrderManagerViewModel
import kotlinx.android.synthetic.main.fragment_order_manager.*

/**
 *  时间：2020/10/20-11:35
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.order OrderListActivity
 *  描述：国内/海外系统订单列表页Activity
 */
class OrderManagerActivity : BaseStateActivity<ActivityOrderManagerBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_order_manager
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(OrderManagerViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        if (SpManager.isOverseasSystem()){
            binding.viewModel?.titleBar?.set(TitleBarViewModel("海外系统订单列表"))
        }else{
            binding.viewModel?.titleBar?.set(TitleBarViewModel("国内系统订单列表"))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

        binding.viewModel?.strOrderStartTime?.set(TimeUtils.getOrderHalfYear())
        binding.viewModel?.startTime = binding.viewModel?.strOrderStartTime?.get() ?: ""

        val list = ArrayList<Fragment>()
        val tabTitle = arrayOf("全部","待发货","待付尾款","待收货","待评价","已完成","待退款","备货中","订单超时","待支付","订单无效","定金无效")
        val tabStatus = arrayOf("","waitingDelivery","waitingFinalPay","waitingGoods","waitingApprise","orderOver","waitingRefunded","stockUp","invalid","toPay","orderDisable","reserveDisable")
        for ((index,tab) in tabTitle.withIndex()){
            list.add(OrderManagerFragment.newInstance(tabStatus[index]))
        }

        binding.viewModel?.fragmentList = list as ArrayList<OrderManagerFragment>
        binding.tvScreen.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.consMore)
            hideKeyboard(it)
        }

        binding.viewPager.offscreenPageLimit = tabTitle.size
        binding.viewPager.adapter = ViewPager2Adapter(this,list)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.viewModel?.viewPagerCurrentIndex = position
            }
        })

        binding.viewPager.currentItem = intent.getIntExtra("current",0)

        val mediator  = TabLayoutMediator(binding.tabLayout,binding.viewPager) { tab, position ->
            tab.text = tabTitle[position]
        }

        mediator.attach()

        binding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                view?.let {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH){
                        binding.viewModel?.let {model->
                            model.onClickReset(it)
                            model.searchType = 1
                            for(fragment in model.fragmentList){
                                fragment.pageNo = 1
                                fragment.isFirstVisibleHint = true
                            }
                            model.fragmentList[model.viewPagerCurrentIndex].smart_layout.autoRefresh()
                        }
                        hideKeyboard(view)
                        return true
                    }
                }
                return false
            }
        })
        binding.tvConfirm.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            binding.viewModel?.onClickConfirm(it)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(binding.consMore)){
            binding.drawerLayout.closeDrawers()
        }else{
            super.onBackPressed()
        }
    }

}