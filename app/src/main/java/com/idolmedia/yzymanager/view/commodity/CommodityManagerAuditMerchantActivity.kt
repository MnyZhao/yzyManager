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
import com.idolmedia.yzymanager.databinding.ActivityCommodityManagerAudioMerchantBinding
import com.idolmedia.yzymanager.interfaces.OnPopItemSelectListener
import com.idolmedia.yzymanager.utils.Dp2PxUtils
import com.idolmedia.yzymanager.view.search.SearchCommodityActivity
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityManagerViewModel
import com.idolmedia.yzymanager.viewmodel.pop.PopCommodityViewModel

/**
 *  时间：2020/10/21-17:36
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodiy CommodityManagerActivity
 *  描述：商品管理审核中页Activity
 *  商家
 */
class CommodityManagerAuditMerchantActivity : BaseStateActivity<ActivityCommodityManagerAudioMerchantBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_manager_audio_merchant
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityManagerViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {

        binding.viewModel?.titleBar?.set(TitleBarViewModel("审核中商品",R.mipmap.ic_search,object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                val intent = Intent(this@CommodityManagerAuditMerchantActivity,SearchCommodityActivity::class.java)
                intent.putExtra("isAudit",true)
                startActivity(intent)
            }

        }))

        binding.viewModel?.strFilterIdentity?.set("全部审核")
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
                    when(index){
                        0 -> binding.viewModel?.strFilterType?.set("全部分类")

                        1 -> binding.viewModel?.strFilterType?.set("普通商品")

                        2 -> binding.viewModel?.strFilterType?.set("特惠商品")

                        3 ->  binding.viewModel?.strFilterType?.set("定金商品")

                        4 -> binding.viewModel?.strFilterType?.set("活动商品")

                        5 -> binding.viewModel?.strFilterType?.set("应援商品")

                        6 -> binding.viewModel?.strFilterType?.set("众筹商品")

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
                    when(index){
                        0 -> binding.viewModel?.strFilterIdentity?.set("全部审核")

                        1 -> binding.viewModel?.strFilterIdentity?.set("审核中")

                        2 -> binding.viewModel?.strFilterIdentity?.set("已撤销")

                        3 ->  binding.viewModel?.strFilterIdentity?.set("审核失败")
                    }
                    if (binding.viewModel?.checkStatus != index){
                        binding.viewModel?.checkStatus = index
                        binding.smartLayout.autoRefresh()
                    }
                    pop?.dismiss()
                }
            }

            pop = BasePopWindow(this,R.layout.pop_commodity_audit)
            pop!!.popModel = PopCommodityViewModel(pop!!,listener).apply {
                typeIndex.set(binding.viewModel?.checkStatus ?: 0)
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
            binding.viewModel?.queryCommodityAudit()
        }
        binding.smartLayout.setOnLoadmoreListener {
            binding.viewModel!!.pageNo += 1
            binding.viewModel?.queryCommodityAudit()
        }
        binding.smartLayout.autoRefresh()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1){
            //编辑商品提交
            binding.smartLayout.autoRefresh()
        }
    }

}