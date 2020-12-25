package com.idolmedia.yzymanager.view.commodity

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommodityManagerBinding
import com.idolmedia.yzymanager.interfaces.OnPopItemSelectListener
import com.idolmedia.yzymanager.utils.Dp2PxUtils
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.view.search.SearchCommodityActivity
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityManagerViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityManageBean
import com.idolmedia.yzymanager.viewmodel.pop.PopCommodityViewModel

/**
 *  时间：2020/10/21-17:36
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodiy CommodityManagerActivity
 *  描述：商品管理页Activity
 */
class CommodityManagerActivity : BaseStateActivity<ActivityCommodityManagerBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_manager
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityManagerViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {

        val putaway = intent.getIntExtra("putaway",0)
        binding.viewModel?.putaway = putaway

        binding.viewModel?.titleBar?.set(TitleBarViewModel(if (putaway==0)"售中商品管理" else "待上架商品管理",R.mipmap.ic_search,object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                val intent = Intent(this@CommodityManagerActivity,SearchCommodityActivity::class.java)
                intent.putExtra("putaway",putaway)
                startActivity(intent)
            }

        }))
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildAdapterPosition(view)
                if (position>0){
                    outRect.top = Dp2PxUtils.dip2px(14).toInt()
                }else{
                    outRect.top = 0
                }
            }
        })
        binding.recyclerView.setItemViewCacheSize(30)
        var pop:BasePopWindow? = null

        binding.tvStatus.setOnClickListener {

            if (pop?.isShowing == true){
                pop?.dismiss()
            }

            val listener = object : OnPopItemSelectListener{
                override fun onPopItemSelect(index: Int, id: String?) {

                    if (binding.smartLayout.isRefreshing){
                        return
                    }

                    when(index){
                        0 -> binding.viewModel?.strFilterType?.set("全部分类")

                        1 -> binding.viewModel?.strFilterType?.set("普通商品")

                       // 2 -> binding.viewModel?.strFilterType?.set("特惠商品")

                        3 ->  binding.viewModel?.strFilterType?.set("定金商品")

                      //  4 -> binding.viewModel?.strFilterType?.set("活动商品")

                        5 -> binding.viewModel?.strFilterType?.set("应援商品")

                      //  6 -> binding.viewModel?.strFilterType?.set("众筹商品")

                        7 -> binding.viewModel?.strFilterType?.set("全部VIP商品")

                        8 -> binding.viewModel?.strFilterType?.set("VIP-仅限会员购买")

                        9 -> binding.viewModel?.strFilterType?.set("VIP-皆可购买")
                    }
                    if (binding.viewModel?.shopType != index){
                        binding.viewModel?.shopType = index
                        binding.smartLayout.autoRefresh()
                    }
                    pop?.dismiss()
                }
            }

            pop = BasePopWindow(this,R.layout.pop_commodity_status)
            pop!!.popModel = PopCommodityViewModel(pop!!,listener).apply {
                typeIndex.set( binding.viewModel?.shopType ?: 0 )
            }
            pop!!.setOnDismissListener {
                binding.viewPopBg.visibility = View.GONE
            }
            pop!!.showAtViewBottom(binding.tvStatus)
            binding.viewPopBg.visibility = View.VISIBLE

        }

        binding.tvClassify.setOnClickListener {

            if (pop?.isShowing == true){
                pop?.dismiss()
            }

            val listener = object : OnPopItemSelectListener{
                override fun onPopItemSelect(index: Int, id: String?) {

                    if (binding.smartLayout.isRefreshing){
                        return
                    }

                    if (binding.viewModel?.commodityType != index){
                        if (index==0){
                            binding.viewModel?.strFilterCommodity?.set("全部商品")
                        }
                        else if (index==1){
                            binding.viewModel?.strFilterCommodity?.set("合并邮费商品")
                        }
                        else if (index==2){
                            binding.viewModel?.strFilterCommodity?.set("非合并邮费商品")
                        }

                        binding.viewModel?.commodityType = index
                        binding.smartLayout.autoRefresh()
                    }
                    pop?.dismiss()
                }
            }

            pop = BasePopWindow(this,R.layout.pop_commodity_classify)
            pop!!.popModel = PopCommodityViewModel(pop!!,listener).apply {
                typeIndex.set(binding.viewModel?.commodityType ?: 0)
            }
            pop!!.setOnDismissListener {
                binding.viewPopBg.visibility = View.GONE
            }
            pop!!.showAtViewBottom(binding.tvStatus)
            binding.viewPopBg.visibility = View.VISIBLE

        }

        binding.tvIdentity.setOnClickListener {

            if (pop?.isShowing == true){
                pop?.dismiss()
            }

            val listener = object : OnPopItemSelectListener{
                override fun onPopItemSelect(index: Int, id: String?) {

                    if (binding.smartLayout.isRefreshing){
                        return
                    }

                    when(index){
                        0 ->{
                            binding.viewModel?.strFilterIdentity?.set("身份限购")
                        }
                        1 ->{
                            binding.viewModel?.strFilterIdentity?.set("仅限第三方商户购买")
                        }
                        2 ->{
                            binding.viewModel?.strFilterIdentity?.set("仅限粉丝团商户购买")
                        }
                        3 ->{
                            binding.viewModel?.strFilterIdentity?.set("仅限粉丝团和第三方商户购买")
                        }
                    }
                    if (binding.viewModel?.buyerGrade != index){
                        binding.viewModel?.buyerGrade = index
                        binding.smartLayout.autoRefresh()
                    }
                    pop?.dismiss()
                }
            }

            pop = BasePopWindow(this,R.layout.pop_commodity_identity)
            pop!!.popModel = PopCommodityViewModel(pop!!,listener).apply {
                typeIndex.set(binding.viewModel?.buyerGrade ?: 0)
            }
            pop!!.setOnDismissListener {
                binding.viewPopBg.visibility = View.GONE
            }
            pop!!.showAtViewBottom(binding.tvStatus)
            binding.viewPopBg.visibility = View.VISIBLE

        }

        binding.viewPopBg.setOnClickListener {

            pop?.dismiss()
            it.visibility = View.GONE

        }

        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.smartLayout.setOnRefreshListener {
            binding.viewModel?.pageNo = 1
            binding.viewModel?.queryCommodity()
        }
        binding.smartLayout.setOnLoadmoreListener {
            binding.viewModel!!.pageNo += 1
            binding.viewModel?.queryCommodity()
        }
        binding.smartLayout.autoRefresh()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1){

            if (requestCode==1){
                //设置退款
                val refunds = data?.getStringExtra("refunds") ?: ""
                val refundsTips = data?.getStringExtra("refundsTips") ?: ""
                val refundsReason = data?.getStringExtra("refundsReason") ?: ""
                val position = data?.getIntExtra("position",-1) ?: -1
                binding.viewModel?.adapter?.get()?.let {
                    if (position>=0 &&position<it.getDate().size){
                        val bean = it.getDate()[position] as ItemCommodityManageBean
                        bean.commodityItem.refunds = refunds
                        bean.commodityItem.refundsTips = refundsTips
                        bean.commodityItem.refundsReason = refundsReason
                    }
                }
            }
            else if(requestCode == 2){
                //开启尾款成功
                val position = data?.getIntExtra("position",-1) ?: -1
                if (position>=0){
                    val bean = binding.viewModel?.adapter?.get()?.getItem(position)
                    if (bean is ItemCommodityManageBean){
                        if (SpManager.userIsManage()){
                            bean.commodityItem.reserveStatus = "2"
                            binding.viewModel?.adapter?.get()?.notifyItemChanged(position)
                        }else{
                            bean.commodityItem.isEdit = true
                        }
                    }

                }
            }
            else if(requestCode == 3){
                val commodityId = data?.getStringExtra("commodityId") ?: ""
                binding.viewModel?.adapter?.get()?.let {
                    for(item in it.getDate()){
                        if (item is ItemCommodityManageBean){
                            if (item.commodityItem.shopcommonId == commodityId){
                                item.commodityItem.limitTimes = data?.getStringExtra("times") ?: "0"
                                item.commodityItem.limitCount = data?.getStringExtra("cunts") ?: "0"
                                if (item.commodityItem.limitCount.isNullOrEmpty() || item.commodityItem.limitCount.toInt()==0){
                                    item.strCommodityLimit.set("限购：不限购")
                                }else{
                                    item.strCommodityLimit.set("限购：${item.commodityItem.limitCount}")
                                }
                            }
                        }
                    }
                }
            }
            else if(requestCode == 4){
                //编辑商品提交
                binding.smartLayout.autoRefresh()
            }
        }
    }

}