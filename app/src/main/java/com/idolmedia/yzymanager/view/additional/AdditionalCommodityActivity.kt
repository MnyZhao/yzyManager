package com.idolmedia.yzymanager.view.additional

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
import com.idolmedia.yzymanager.databinding.ActivityAdditionalCommodityBinding
import com.idolmedia.yzymanager.model.entity.AdditionalEntity
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.additional.AdditionalViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemAdditionalBean

/**
 *  时间：2020/10/27-10:55
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddAdditionalActivity
 *  描述：商品的附加信息页Activity
 *  商品添加附加信息或者编辑商品查看附加信息时需先展示此页，可以选择公共模板或者自定义
 */
class AdditionalCommodityActivity : BaseStateActivity<ActivityAdditionalCommodityBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_additional_commodity
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(AdditionalViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("附加信息","保存",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                if (binding.viewModel?.toSave() == true){
                    setResult(1,binding.viewModel?.getResultIntent())
                    finish()
                }

            }

        }))
    }

    override fun initView(savedInstanceState: Bundle?) {

        //商品在已添加了附加信息或者编辑商品时的附加信息数据 json格式 ArrayList<AdditionalEntity.AdditionalSave>
        val json = intent.getStringExtra("json") ?: ""
        val type = object : TypeToken<ArrayList<AdditionalEntity.AdditionalSave>>(){}.type

        val list = Gson().fromJson<ArrayList<AdditionalEntity.AdditionalSave>>(json,type)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        ItemDecorationCommon.addItemDecoration(14,binding.recyclerView)

        binding.viewModel?.adapter?.set(BaseAdapter(this))
        list?.let {
            binding.viewModel?.adapter?.get()?.let { adapter->
                for(item in it){
                    adapter.add(ItemAdditionalBean(item,binding.viewModel!!).apply {
                        isCheck.set(true)
                    })
                }
                adapter.notifyDataSetChanged()
            }
        }

        binding.tvSelect.setOnClickListener {
            val intent = Intent(this, AdditionalListActivity::class.java)
            binding.viewModel?.adapter?.get()?.let {
                val isAddition = StringBuilder()
                for(bean in it.getDate()){
                    if (bean is ItemAdditionalBean){
                        if (bean.item.addition_key.isNotEmpty()){
                            isAddition.append("["+bean.item.addition_key+"]")
                        }
                    }
                }
                //通过公共附加信息的isAddition字段判断是否已经选择了
                intent.putExtra("keys",isAddition.toString())
            }
            startActivityForResult(intent,1000)
        }

        binding.tvAdd.setOnClickListener {
            //去自定义附加信息
            startActivityForResult(Intent(this,AdditionalAddActivity::class.java),1001)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1){
            val position = data?.getIntExtra("position",-1) ?: -1
            val json = data?.getStringExtra("json") ?: ""
            if (position>=0){
                //编辑附加信息回调
                val additional = Gson().fromJson(json,AdditionalEntity.AdditionalSave::class.java)
                binding.viewModel?.adapter?.get()?.let {
                    val bean = it.getDate()[requestCode] as ItemAdditionalBean
                    bean.refreshAdditional(additional)
                }
            }else{
                if (requestCode==1001){
                    //自定义附加信息回调
                    val additional = Gson().fromJson(json,AdditionalEntity.AdditionalSave::class.java)
                    binding.viewModel?.let {
                        it.adapter.get()?.add(ItemAdditionalBean(additional,it).apply {
                            isCheck.set(true)
                        })
                        it.adapter.get()?.notifyInsertedBodySize(1)
                    }
                }
                else if (requestCode==1000){
                    val type =object : TypeToken<ArrayList<AdditionalEntity.AdditionalSave>>(){}.type
                    val list = Gson().fromJson<ArrayList<AdditionalEntity.AdditionalSave>>(json,type)
                    list?.let {
                        binding.viewModel?.adapter?.get()?.let { adapter->
                            for(item in list){
                                adapter.add(ItemAdditionalBean(item,binding.viewModel!!).apply {
                                    isCheck.set(true)
                                })
                            }
                            adapter.notifyInsertedBodySize(it.size)
                        }
                    }
                }
            }
        }
    }

}