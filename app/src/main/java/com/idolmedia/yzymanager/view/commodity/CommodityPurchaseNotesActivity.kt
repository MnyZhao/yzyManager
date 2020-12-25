package com.idolmedia.yzymanager.view.commodity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommodityPurchaseNotesBinding
import com.idolmedia.yzymanager.model.bean.BuyNotesBean
import com.idolmedia.yzymanager.model.entity.CommodityPreconditionEntity
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityPurchaseNotesViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemPurchaseNotesBean

/**
 *  时间：2020/10/26-16:47
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddBuySpecificationActivity
 *  描述：添加商品购买须知页Activity
 */
class CommodityPurchaseNotesActivity : BaseStateActivity<ActivityCommodityPurchaseNotesBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_purchase_notes
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityPurchaseNotesViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("购买须知","保存",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {

                binding.viewModel?.adapter?.get()?.let {

                    val list = ArrayList<BuyNotesBean>()

                    for(bean in it.getDate()){
                        if (bean is ItemPurchaseNotesBean){
                            if (!bean.strContent.get().isNullOrEmpty()){
                                val item = BuyNotesBean()
                                item.id = bean.id
                                item.title = bean.strTitle.get() ?: ""
                                item.content = bean.strContent.get() ?: ""
                                list.add(item)
                            }

                        }
                    }

                    val intent = Intent()
                    intent.putExtra("json",Gson().toJson(list))
                    setResult(1,intent)
                    finish()
                }

            }

        }))
    }

    override fun initView(savedInstanceState: Bundle?) {
        val json = intent.getStringExtra("json") ?: ""

        val json1 = intent.getStringExtra("notes") ?: ""
        val notes = ArrayList<BuyNotesBean>()
        if (json1.isNotEmpty()){
            val bType = object : TypeToken<ArrayList<BuyNotesBean>>(){}.type

            val nType = object : TypeToken<ArrayList<CommodityPreconditionEntity.Note>>(){}.type
            val notesList = Gson().fromJson<ArrayList<CommodityPreconditionEntity.Note>>(json,nType)

            val bList = Gson().fromJson<ArrayList<BuyNotesBean>>(json1,bType)
            bList?.let {

                for(item in it){
                    if (item.title.isNullOrEmpty()){
                        notesList?.let { nList->
                            for(n in nList){
                                if (n.id == item.id || n.id == item.aboutType){
                                    item.title = n.title
                                    break
                                }
                            }
                        }
                    }

                    notes.add(item)
                }

            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        //binding.recyclerView.setItemViewCacheSize(20)
        ItemDecorationCommon.addItemDecoration(14,binding.recyclerView)
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.viewModel?.adapter?.get()?.let {

            for(item in notes){
                it.add(ItemPurchaseNotesBean(it,json,item))
            }

            it.notifyDataSetChanged()
        }

        binding.tvAdd.setOnClickListener {
            val intent = Intent(this,PurchaseNotesListActivity::class.java)
            intent.putExtra("json",json)

            binding.viewModel?.adapter?.get()?.let {
                var ids = ""
                for (item in it.getDate()){
                    if (item is ItemPurchaseNotesBean){
                        ids += "[${item.id}]"
                    }
                }
                intent.putExtra("ids",ids)
            }

            startActivityForResult(intent,100)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1){

            val id = data?.getStringExtra("id") ?: ""
            val title = data?.getStringExtra("title") ?: ""

            if (requestCode==100){
                binding.viewModel?.adapter?.get()?.let {
                    it.add(ItemPurchaseNotesBean(it,intent.getStringExtra("json") ?: "").apply {
                        this.id = id
                        this.strTitle.set(title)
                    })
                    it.notifyInsertedBodySize(1)
                }
            }else{
                val bean = binding.viewModel?.adapter?.get()?.getDate()!![requestCode] as ItemPurchaseNotesBean
                bean.id = id
                bean.strTitle.set(title)
            }
        }
    }

}