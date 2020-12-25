package com.idolmedia.yzymanager.view.commodity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommoditySpecificationBinding
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.model.bean.CommodityPublishBean
import com.idolmedia.yzymanager.model.bean.SpecificationBean
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityBuySpecificationViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommoditySpecificationBean
import java.lang.StringBuilder

/**
 *  时间：2020/10/26-16:47
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddBuySpecificationActivity
 *  描述：添加商品商品规格页Activity
 */
class CommoditySpecificationActivity : BaseStateActivity<ActivityCommoditySpecificationBinding>(),OnItemDeleteListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_specification
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityBuySpecificationViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("商品规格","保存",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                setResultIntent()
            }

        }))
    }

    override fun initView(savedInstanceState: Bundle?) {

        /**是否是从商品详情过来的编辑操作*/
        val isEdit = intent.getBooleanExtra("isEdit",false)
        binding.viewModel?.isEdit = isEdit

        val json = intent.getStringExtra("json") ?: ""
        val bean = Gson().fromJson(json, CommodityPublishBean::class.java)
        binding.viewModel?.isVipShop = bean?.isVipShop ?: 0
        val list = ArrayList<BaseBean>()
        bean?.let {
            for(item in it.catalogItems){
                list.add(ItemCommoditySpecificationBean(item,this,binding.viewModel?.isVipShop ?: 0).apply {
                    this.isEdit = isEdit
                })
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        ItemDecorationCommon.addItemDecoration(14,binding.recyclerView)
        binding.viewModel?.adapter?.set(BaseAdapter(this,list))

        binding.tvAdd.setOnClickListener {
            val intent = Intent(this,CommoditySpecificationAddActivity::class.java)
            intent.putExtra("imgUrl",bean?.imageUrl)
            intent.putExtra("isVipShop",binding.viewModel?.isVipShop)
            intent.putExtra("cataFlag",if (bean?.shopType=="reserve2") 1 else 0)
            startActivityForResult(intent,1000)
        }

    }

    private val deleteIds = StringBuilder()
    override fun onItemDelete(position: Int, bean: BaseBean) {
        val isAudit = intent.getBooleanExtra("isAudit",false)
        bean as ItemCommoditySpecificationBean
        if (bean.isEdit){
            if (isAudit){
                //审核中商品删除时需要记录catalogCheckId
                if (!bean.bean.catalogCheckId.isNullOrEmpty()){
                    if (deleteIds.toString().isEmpty()){
                        deleteIds.append(bean.bean.catalogCheckId)
                    }else{
                        deleteIds.append(","+bean.bean.catalogCheckId)
                    }
                }
            }else{
                //在售的商品删除时需要记录sscId
                if (!bean.bean.sscId.isNullOrEmpty()){
                    if (deleteIds.toString().isEmpty()){
                        deleteIds.append(bean.bean.sscId)
                    }else{
                        deleteIds.append(","+bean.bean.sscId)
                    }
                }
            }
        }

        binding.viewModel?.adapter?.get()?.notifyDelete(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1){
            val json = data?.getStringExtra("json")
            val bean = Gson().fromJson(json,SpecificationBean::class.java)
            if (requestCode == 1000){
                binding.viewModel?.adapter?.get()?.let {
                    it.add(ItemCommoditySpecificationBean(bean,this,binding.viewModel?.isVipShop ?: 0))
                    it.notifyInsertedBodySize(1)
                }
            }else{
                binding.viewModel?.adapter?.get()?.let {
                    val item = it.getDate()[requestCode] as ItemCommoditySpecificationBean
                    item.setNewBean(bean)
                }
            }
        }
    }

    private fun setResultIntent(){
        binding.viewModel?.adapter?.get()?.let {

            val list = ArrayList<SpecificationBean>()
            for(item in it.getDate()){
                item as ItemCommoditySpecificationBean
                list.add(item.bean)
            }
            if (list.isNotEmpty()){
                val intent = Intent()
                intent.putExtra("json",Gson().toJson(list))
                intent.putExtra("deleteIds",deleteIds.toString())
                setResult(1,intent)
                finish()
            }else{
                ToastUtil.show("请添加商品规格")
            }

        }
    }

}