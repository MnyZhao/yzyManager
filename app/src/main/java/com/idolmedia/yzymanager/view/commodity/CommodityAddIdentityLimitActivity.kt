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
 *  描述：添加商品身份限购页Activity
 */
class CommodityAddIdentityLimitActivity : BaseStateActivity<ActivityCommodityAddSelectBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_add_select
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityAddMemberStatusViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("身份限购"))
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        val list = ArrayList<BaseBean>()
        list.add(ItemCommodityAddSelectBean("全部用户",false))
        list.add(ItemCommodityAddSelectBean("第三方商家",false))
        list.add(ItemCommodityAddSelectBean("粉丝团商家",false))
        list.add(ItemCommodityAddSelectBean("第三方和粉丝团商家",false))

        val buyerGrade = intent.getIntExtra("buyerGrade",-1)
        if (buyerGrade>=0){
            (list[buyerGrade] as ItemCommodityAddSelectBean).isSelect.set(true)
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
                    intent.putExtra("buyerGrade",position)
                    setResult(1,intent)
                    finish()
                }
            })
        })
    }

}