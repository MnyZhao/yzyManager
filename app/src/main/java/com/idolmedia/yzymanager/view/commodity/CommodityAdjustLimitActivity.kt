package com.idolmedia.yzymanager.view.commodity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommodityAdjustLimitBinding
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityAdjustLimitViewModel

/**
 *  时间：2020/10/22-15:13
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodiy CommodityAdjustInventoryActivity
 *  描述：调整限购数量页Activity
 */
class CommodityAdjustLimitActivity : BaseStateActivity<ActivityCommodityAdjustLimitBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_adjust_limit
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityAdjustLimitViewModel::class.java)
}

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("调整限购数量"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val isAudit = intent.getBooleanExtra("isAudit",false)
        val commodityId = intent.getStringExtra("commodityId") ?: ""
        val commodityName = intent.getStringExtra("commodityName") ?: ""
        val limitCount = intent.getStringExtra("limitCount") ?: ""
        val limitTimes = intent.getStringExtra("limitTimes") ?: ""

        binding.viewModel?.strCommodityName?.set(commodityName)
        binding.viewModel?.strLimitCount?.set(limitCount)
        binding.viewModel?.strLimitTimes?.set(limitTimes)

        binding.tvSave.setOnClickListener {

            binding.viewModel?.let {

                if (it.strLimitCount.get().isNullOrEmpty()){
                    ToastUtil.show("请设置限购数量")
                    return@setOnClickListener
                }

                if (it.strLimitTimes.get().isNullOrEmpty()){
                    ToastUtil.show("请设置限购次数量")
                    return@setOnClickListener
                }

                RetrofitHelper.instance().setCommodityLimit(
                    isAudit,
                    it.strLimitCount.get()!!,
                    it.strLimitTimes.get()!!,
                    commodityId,
                    object:BaseObserver<BaseEntity>(){
                        override fun onSuccess(t: BaseEntity) {
                            if (t.resultCode==1){
                                ToastUtil.show("调整成功")
                                val intent = Intent()
                                intent.putExtra("commodityId",commodityId)
                                intent.putExtra("cunts",it.strLimitCount.get())
                                intent.putExtra("times",it.strLimitTimes.get())
                                setResult(1,intent)
                                finish()
                            }
                        }

                        override fun onError(msg: String) {
                            ToastUtil.show(msg)
                        }

                    })

            }

        }

    }

}