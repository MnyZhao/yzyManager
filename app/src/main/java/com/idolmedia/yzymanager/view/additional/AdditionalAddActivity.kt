package com.idolmedia.yzymanager.view.additional

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityAdditionalAddBinding
import com.idolmedia.yzymanager.model.entity.AdditionalEntity
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.additional.AdditionalAddViewModel

/**
 *  时间：2020/10/27-10:55
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddAdditionalActivity
 *  描述：添加附加信息页Activity
 */
class AdditionalAddActivity : BaseStateActivity<ActivityAdditionalAddBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_additional_add
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(AdditionalAddViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("添加附加信息"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val json = intent.getStringExtra("json") ?: ""
        var bean = Gson().fromJson(json,AdditionalEntity.AdditionalSave::class.java)
        binding.viewModel?.bean = bean
        binding.viewModel?.bean?.let {

            binding.viewModel?.strName?.set(it.field)
            binding.viewModel?.strHint?.set(it.placehold)

            binding.viewModel?.isEdit?.set(it.addition_isalter == "0")
            binding.viewModel?.isMandatory?.set(it.addition_is == "0")

        }

        binding.tvSave.setOnClickListener {

            binding.viewModel?.let {

                if (it.strName.get().isNullOrEmpty()){
                    ToastUtil.show("请填写信息内容")
                    return@setOnClickListener
                }
                if (it.strHint.get().isNullOrEmpty()){
                    ToastUtil.show("请填写提示文字")
                    return@setOnClickListener
                }

                val intent = Intent()
                if (bean==null){
                    bean = AdditionalEntity.AdditionalSave()
                    bean.isAddition = "4"
                    bean.addition_key = System.currentTimeMillis().toString()
                }else{
                    intent.putExtra("position",this@AdditionalAddActivity.intent.getIntExtra("position",-1))
                }
                bean.field = binding.viewModel?.strName?.get() ?: ""
                bean.placehold = binding.viewModel?.strHint?.get() ?: ""
                bean.addition_isalter = if(binding.viewModel?.isEdit?.get() == true) "0" else "1"
                bean.addition_is = if(binding.viewModel?.isMandatory?.get() == true) "0" else "1"
                intent.putExtra("json",Gson().toJson(bean))
                setResult(1,intent)
                finish()

            }

        }

    }

}