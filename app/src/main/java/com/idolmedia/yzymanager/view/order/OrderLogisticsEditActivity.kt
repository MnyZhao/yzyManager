package com.idolmedia.yzymanager.view.order

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityOrderEditLogisticsBinding
import com.idolmedia.yzymanager.model.entity.OrderDetailsEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.idolmedia.yzymanager.viewmodel.common.ItemLineBean
import com.idolmedia.yzymanager.viewmodel.order.ItemOrderDetailsFreightBean
import com.idolmedia.yzymanager.viewmodel.order.ItemOrderDetailsFreightTitleBean
import com.idolmedia.yzymanager.viewmodel.order.OrderEditViewModel

/**
 *  时间：2020/11/17-17:41
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.order OrderAdditionalEditActivity
 *  描述：编辑订单物流信息页Activity
 */
class OrderLogisticsEditActivity : BaseStateActivity<ActivityOrderEditLogisticsBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_order_edit_logistics
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(OrderEditViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("编辑订单物流信息"))
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.viewModel?.orderNum = intent.getStringExtra("orderNum") ?: ""
        val json = intent.getStringExtra("json") ?: ""
        val type = object : TypeToken<ArrayList<OrderDetailsEntity.Express>>(){}.type
        val list = Gson().fromJson<ArrayList<OrderDetailsEntity.Express>>(json,type)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = BaseAdapter(this)
        adapter.add(ItemCommodityAddBean("附加信息"))
        adapter.add(ItemLineBean(1, R.color.bg_fa))
        adapter.add(ItemOrderDetailsFreightTitleBean())
        for(e in list){
            adapter.add(ItemOrderDetailsFreightBean(e).apply {
                isEdit = true
            })
        }
        adapter.add(ItemLineBean(13, R.color.white))

        binding.viewModel?.adapter?.set(adapter)

        binding.tvSave.setOnClickListener {

            binding.viewModel?.adapter?.get()?.let {

                val list = ArrayList<OrderDetailsEntity.Express>()
                for(bean in it.getDate()){
                    if (bean is ItemOrderDetailsFreightBean){
                        if (bean.strNumber.get().isNullOrEmpty()){
                            ToastUtil.show("请将物流单号补充完整")
                            return@setOnClickListener
                        }
                        bean.item.waybillNo = bean.strNumber.get() ?: ""
                        list.add(bean.item)
                    }
                }

                if (list.isNotEmpty()){
                    saveLogistics(Gson().toJson(list))
                }

            }

        }

    }

    private fun saveLogistics(json:String){
        RetrofitHelper.instance().saveOrderLogistics(binding.viewModel?.orderNum!!,json,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("操作成功")
                    setResult(1)
                    finish()
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

}