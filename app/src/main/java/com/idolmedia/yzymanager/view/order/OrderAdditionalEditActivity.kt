package com.idolmedia.yzymanager.view.order

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityOrderEditAdditionalBinding
import com.idolmedia.yzymanager.model.entity.AdditionalEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.idolmedia.yzymanager.viewmodel.order.OrderEditViewModel

/**
 *  时间：2020/11/17-17:41
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.order OrderAdditionalEditActivity
 *  描述：编辑订单附加信息页Activity
 */
class OrderAdditionalEditActivity : BaseStateActivity<ActivityOrderEditAdditionalBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_order_edit_additional
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(OrderEditViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("编辑订单附加信息"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        binding.viewModel?.orderNum = intent.getStringExtra("orderNum") ?: ""

        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.smartLayout.setOnRefreshListener { binding.viewModel?.queryOrderAdditional() }
        binding.smartLayout.isEnableLoadmore = false
        binding.smartLayout.autoRefresh()

        binding.tvSave.setOnClickListener {

            binding.viewModel?.adapter?.get()?.let {

                val list = ArrayList<AdditionalEntity.Additional2>()
                for(bean in it.getDate()){
                    bean as ItemCommodityAddBean
                    if (bean.position>0){
                        bean.additional?.addition_value = bean.strContent.get() ?: ""
                        list.add(bean.additional!!)
                    }
                }

                if (list.isNotEmpty()){
                    saveAdditional(Gson().toJson(list))
                }

            }

        }

    }

    private fun saveAdditional(json:String){
        RetrofitHelper.instance().saveOrderAdditional(binding.viewModel?.orderNum?:"",json,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("保存成功")
                    finish()
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

}