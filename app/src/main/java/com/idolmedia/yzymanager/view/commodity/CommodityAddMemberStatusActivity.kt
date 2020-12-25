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
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommodityAddSelectBinding
import com.idolmedia.yzymanager.utils.Dp2PxUtils
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityAddMemberStatusViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddSelectBean

/**
 *  时间：2020/10/26-14:57
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddActivity
 *  描述：添加商品会员属性页Activity
 */
class CommodityAddMemberStatusActivity : BaseStateActivity<ActivityCommodityAddSelectBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_add_select
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityAddMemberStatusViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("会员属性"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val isVipShop = intent.getIntExtra("isVipShop",0)
        val shopType = intent.getStringExtra("shopType") ?: ""

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = Dp2PxUtils.dip2px(1).toInt()
            }
        })
        val list = ArrayList<BaseBean>()
        list.add(ItemCommodityAddSelectBean("仅限会员购买",isVipShop == 1))
        if (shopType != "reserve2"){
            list.add(ItemCommodityAddSelectBean("皆可购买",isVipShop == 2))
        }

        binding.viewModel?.adapter?.set(BaseAdapter(this,list).apply {
            setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
                override fun itemClick(position: Int, item: BaseBean, parent: View) {
                    for(bean in list){
                        bean as ItemCommodityAddSelectBean
                        bean.isSelect.set(false)
                    }
                    item as ItemCommodityAddSelectBean
                    item.isSelect.set(true)
                    val intent = Intent()
                    intent.putExtra("status",item.strTitle.get())
                    intent.putExtra("isVipShop",position+1)
                    setResult(1,intent)
                    finish()
                }
            })
        })
    }

}