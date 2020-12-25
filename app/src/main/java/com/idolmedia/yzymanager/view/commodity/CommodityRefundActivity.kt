package com.idolmedia.yzymanager.view.commodity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommodityRefundSettingBinding
import com.idolmedia.yzymanager.interfaces.OnItemSwitchListener
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityRefundViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean

/**
 *  时间：2020/11/11-17:13
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity CommodityRefundActivity
 *  描述：商品管理设置退款页Activity
 */
class CommodityRefundActivity : BaseStateActivity<ActivityCommodityRefundSettingBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_refund_setting
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityRefundViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("设置退款内容"))
    }

    override fun initView(savedInstanceState: Bundle?) {
        val isAudit = intent.getBooleanExtra("isAudit",false)
        val refunds = intent.getStringExtra("refunds") //1设置退款 0未开启
        val refundsTips = intent.getStringExtra("refundsTips") ?: ""
        val refundsReason = intent.getStringExtra("refundsReason") ?: ""
        val shopcommonId = intent.getStringExtra("shopcommonId") ?: ""
        val position = intent.getIntExtra("position",-1)

        binding.viewModel?.switchBean?.set(ItemCommodityAddBean("是否设置退款",refunds=="1").apply {
            itemSwitchListener = object : OnItemSwitchListener{
                override fun onItemSwitch(position: Int, bean: BaseBean, switch: Boolean) {
                    if (switch){
                        binding.viewModel?.visibleContent?.set(View.VISIBLE)
                    }else{
                        binding.viewModel?.visibleContent?.set(View.GONE)
                    }
                }
            }
        })


        if (refunds=="1"){
            binding.viewModel?.strTitle?.set(refundsTips)
            binding.viewModel?.strContent?.set(refundsReason)
        }else{
            binding.viewModel?.visibleContent?.set(View.GONE)
        }

        binding.tvSave.setOnClickListener {

            val intent = Intent()

            binding.viewModel?.let {

                if (it.switchBean.get()?.isOpen?.get() == true){

                    if (it.strTitle.get().isNullOrEmpty()){
                        ToastUtil.show("请填写退款警告")
                        return@setOnClickListener
                    }
                    if (it.strContent.get().isNullOrEmpty()){
                        ToastUtil.show("请填写退款原因")
                        return@setOnClickListener
                    }

                    intent.putExtra("refunds","1")
                    intent.putExtra("refundsTips",it.strTitle.get())
                    intent.putExtra("refundsReason",it.strContent.get())
                }else{
                    intent.putExtra("refunds","0")
                    intent.putExtra("refundsTips","")
                    intent.putExtra("refundsReason","")
                }

                RetrofitHelper.instance().saveCommodityRefund(
                    isAudit,
                    if (it.switchBean.get()?.isOpen?.get() == true) "1" else "0",
                    it.strContent.get() ?: "",
                    it.strTitle.get() ?: "",
                    shopcommonId,
                    object : BaseObserver<BaseEntity>(){
                        override fun onSuccess(t: BaseEntity) {
                            if (t.resultCode==1){
                                intent.putExtra("position",position)
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