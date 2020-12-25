package com.idolmedia.yzymanager.view.commodity

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
import com.idolmedia.yzymanager.databinding.ActivityCommodityAddSelectBinding
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityAddMemberStatusViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddSelectBean

/**
 *  时间：2020/10/26-14:57
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddActivity
 *  描述：添加商品地区限制页Activity
 */
class CommodityAddAreaLimitActivity : BaseStateActivity<ActivityCommodityAddSelectBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_add_select
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityAddMemberStatusViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("地区限制"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val isAreaLimit = intent.getStringExtra("isAreaLimit") ?: ""

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        val list = ArrayList<BaseBean>()
        list.add(ItemCommodityAddSelectBean("全球用户皆可购买","2",isAreaLimit=="2"))
        list.add(ItemCommodityAddSelectBean("仅限中国大陆购买","0",isAreaLimit=="0"))
        list.add(ItemCommodityAddSelectBean("仅限港澳台购买","3",isAreaLimit=="3"))
        list.add(ItemCommodityAddSelectBean("仅限国外购买","5",isAreaLimit=="5"))
        list.add(ItemCommodityAddSelectBean("仅限中国大陆+港澳台购买","4",isAreaLimit=="4"))
        list.add(ItemCommodityAddSelectBean("仅限中国大陆+国外购买","6",isAreaLimit=="6"))
        list.add(ItemCommodityAddSelectBean("仅限港澳台+国外购买","1",isAreaLimit=="1"))

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
                    intent.putExtra("isAreaLimit",item.id)
                    setResult(1,intent)
                    finish()
                }
            })
        })
    }

}