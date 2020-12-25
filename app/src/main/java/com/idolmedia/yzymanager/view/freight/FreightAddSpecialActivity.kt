package com.idolmedia.yzymanager.view.freight

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityFreightAddSpecialBinding
import com.idolmedia.yzymanager.model.bean.FreightBean
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.freight.FreightAddSpecialViewModel

/**
 *  时间：2020/10/26-18:23
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.freight FreightAddActivity
 *  描述：运费模板特定地区配置计价方式页Activity
 */
class FreightAddSpecialActivity : BaseStateActivity<ActivityFreightAddSpecialBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_freight_add_special
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(FreightAddSpecialViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("配置计价方式"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val json = intent.getStringExtra("freight") ?: ""
        val freight = Gson().fromJson(json,FreightBean::class.java)

        freight?.let {
            binding.viewModel?.strFirstPrice?.set(it.firstPrice)
            binding.viewModel?.strFirstCount?.set(it.firstPart)
            binding.viewModel?.strSecondPrice?.set(it.continuePrice)
            binding.viewModel?.strSecondCount?.set(it.continuePart)
        }

        binding.tvSave.setOnClickListener {
            binding.viewModel?.let {

                if (it.strFirstCount.get().isNullOrEmpty()){
                    ToastUtil.show("请输入首件数量")
                    return@setOnClickListener
                }
                if (it.strFirstPrice.get().isNullOrEmpty()){
                    ToastUtil.show("请输入首件价格")
                    return@setOnClickListener
                }
                if (it.strSecondCount.get().isNullOrEmpty()){
                    ToastUtil.show("请输入续件数量")
                    return@setOnClickListener
                }
                if (it.strSecondPrice.get().isNullOrEmpty()){
                    ToastUtil.show("请输入续件价格")
                    return@setOnClickListener
                }
                val bean = FreightBean()
                bean.firstPart = it.strFirstCount.get()!!
                bean.firstPrice = it.strFirstPrice.get()!!
                bean.continuePart = it.strSecondCount.get()!!
                bean.continuePrice = it.strSecondPrice.get()!!
                val intent = Intent()
                intent.putExtra("freight",Gson().toJson(bean))
                setResult(2,intent)

                finish()

            }

        }

    }

}