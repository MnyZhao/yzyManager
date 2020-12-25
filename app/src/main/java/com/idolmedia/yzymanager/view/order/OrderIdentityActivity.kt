package com.idolmedia.yzymanager.view.order

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityOrderIdentityBinding
import com.idolmedia.yzymanager.utils.CopyUtil
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.order.OrderIdentityViewModel

/**
 *  时间：2020/11/17-19:59
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.order OrderIdentityActivity
 *  描述：
 */
class OrderIdentityActivity : BaseStateActivity<ActivityOrderIdentityBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_order_identity
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(OrderIdentityViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("身份证信息"))
    }

    override fun initView(savedInstanceState: Bundle?) {
        val img1 = intent.getStringExtra("img1") ?: ""
        val img2 = intent.getStringExtra("img2") ?: ""
        val name = intent.getStringExtra("name") ?: ""
        val number = intent.getStringExtra("number") ?: ""

        binding.viewModel?.let {
            it.strImgOne.set(img1)
            it.strImgTwo.set(img2)
            it.strName.set(name)
            it.strNumber.set(number)
        }

        binding.ivCopy.setOnClickListener {

            CopyUtil.copy(this,number)

        }

    }
}