package com.idolmedia.yzymanager.view.commodity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommodityAddSellerNoticeBinding
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityAddSellerNoticeViewModel

/**
 *  时间：2020/10/27-13:48
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddSellerNoticeActivity
 *  描述：发布商品卖家公告页Activity
 */
class CommodityAddSellerNoticeActivity : BaseStateActivity<ActivityCommodityAddSellerNoticeBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_add_seller_notice
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityAddSellerNoticeViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("卖家公告"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val sellerNotice = intent.getStringExtra("sellerNotice") ?: ""
        binding.viewModel?.strContent?.set(sellerNotice)

        binding.tvSave.setOnClickListener {

            val intent = Intent()
            intent.putExtra("sellerNotice",binding.viewModel?.strContent?.get()?: "")
            setResult(1,intent)
            finish()

        }

    }
}