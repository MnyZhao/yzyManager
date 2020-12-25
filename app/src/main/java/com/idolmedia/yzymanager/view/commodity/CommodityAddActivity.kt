package com.idolmedia.yzymanager.view.commodity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.*
import com.idolmedia.yzymanager.databinding.ActivityCommodityAddBinding
import com.idolmedia.yzymanager.interfaces.OnItemSwitchListener
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.utils.Dp2PxUtils
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.TimeUtils
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.view.additional.AdditionalCommodityActivity
import com.idolmedia.yzymanager.view.freight.FreightListActivity
import com.idolmedia.yzymanager.view.ido.IdoAssociatedActivity
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityAddViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.idolmedia.yzymanager.viewmodel.common.ItemLineBean
import com.idolmedia.yzymanager.viewmodel.common.ItemTitleBean
import com.idolmedia.yzymanager.widget.CustomLoading

/**
 *  时间：2020/10/26-14:57
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddActivity
 *  描述：添加商品页Activity
 */
class CommodityAddActivity : BaseStateActivity<ActivityCommodityAddBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_add
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityAddViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel(if (SpManager.getUserIdentity() == "COMMON_PRODUCT_MERCHANT") "添加普通商品" else "添加会员商品","提交",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                onBackPressed()
            }

            override fun onRightClick(view: View) {

                binding.viewModel?.saveCommodity(this@CommodityAddActivity,object : BaseObserver<BaseEntity>(){
                    override fun onSuccess(t: BaseEntity) {
                        if (t.resultCode==1){
                            ToastUtil.show(t.msg)
                            finish()
                        }
                    }

                    override fun onError(msg: String) {
                        ToastUtil.show(msg)
                    }

                    override fun onComplete() {
                        super.onComplete()
                        CustomLoading.getInstance().closeLoad()
                    }

                })
            }

        }))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val userIdentity = SpManager.getUserIdentity()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setItemViewCacheSize(50)
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.viewModel?.adapter?.set(BaseAdapter(this))
        binding.viewModel?.adapter?.get()?.setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
            override fun itemClick(position: Int, item: BaseBean, parent: View) {
                binding.viewModel?.let {

                    if (item is ItemCommodityAddBean){
                        if (item.strTitle.get() != "商品分类" && it.commodityPublishBean.shopType.isEmpty()){
                            ToastUtil.show("请先选择商品分类")
                            return
                        }
                        else if (it.commodityPublishBean.shopType.isNotEmpty() && it.commodityPublishBean.isVipShop == -1 && item.strTitle.get() != "会员属性"){
                            ToastUtil.show("请先选择会员分类")
                            return
                        }

                        when(item.strTitle.get()){
                            "商品分类" ->{
                                val intent = Intent(this@CommodityAddActivity,CommodityAddClassifyActivity::class.java)
                                intent.putExtra("shopType",it.commodityPublishBean.shopType)
                                startActivityForResult(intent ,1)
                            }
                            "会员属性" ->{
                                val intent = Intent(this@CommodityAddActivity,CommodityAddMemberStatusActivity::class.java)
                                intent.putExtra("isVipShop",it.commodityPublishBean.isVipShop)
                                intent.putExtra("shopType",it.commodityPublishBean.shopType)
                                startActivityForResult(intent,2)
                            }
                            "一级分类" ->{
                                val intent = Intent(this@CommodityAddActivity,CommodityAddClassifyFirstActivity::class.java)
                                intent.putExtra("list",Gson().toJson(it.cpEntity?.datas?.goodsTypeArray))
                                intent.putExtra("goodsTypeId",it.commodityPublishBean.goodsTypeId)
                                startActivityForResult(intent ,3)
                            }
                            "身份限购" ->{
                                val intent = Intent(this@CommodityAddActivity,CommodityAddIdentityLimitActivity::class.java)
                                intent.putExtra("buyerGrade",it.commodityPublishBean.buyerGrade)
                                startActivityForResult(intent,4)
                            }
                            "售后客服" ->{
                                val intent = Intent(this@CommodityAddActivity,CommodityAddAfterSalesServiceActivity::class.java)
                                intent.putExtra("isProxies",it.commodityPublishBean.isProxies)
                                startActivityForResult(intent,5)
                            }
                            "地区限制" ->{
                                val intent = Intent(this@CommodityAddActivity,CommodityAddAreaLimitActivity::class.java)
                                intent.putExtra("isAreaLimit",it.commodityPublishBean.isAreaLimit)
                                startActivityForResult(intent,6)
                            }
                            "活动时间" ->{
                                val intent = Intent(this@CommodityAddActivity,CommodityAddActivityTimeActivity::class.java)
                                intent.putExtra("isLimitTime",it.commodityPublishBean.isLimitTime)
                                intent.putExtra("startTime",it.commodityPublishBean.startTime)
                                intent.putExtra("endTime",it.commodityPublishBean.endTime)
                                intent.putExtra("reserveStatus",if (it.commodityPublishBean.shopType=="reserve2") 1 else 0)
                                startActivityForResult(intent,7)
                            }
                            "定时上架时间","发行时间（选填）","配送时间（选填）" ->{
                                //时间选择器
                                val pvTime = TimePickerBuilder(this@CommodityAddActivity) { date, view ->
                                    val str = TimeUtils.getTime(date)
                                    item.strContent.set(str)
                                    binding.viewModel?.commodityPublishBean?.upTime = str
                                }
                                    .setType(booleanArrayOf(true, true, true, false, false, false))
                                    .setLineSpacingMultiplier(1.8f)
                                    .build()
                                pvTime.show()
                            }
                            "关联爱豆" ->{
                                val intent = Intent(this@CommodityAddActivity,IdoAssociatedActivity::class.java)
                                intent.putExtra("aidouIds",it.commodityPublishBean.aidouIds)
                                intent.putExtra("idoList",it.commodityPublishBean.idoList)
                                startActivityForResult(intent ,8)
                            }
                            "邮费模板" ->{
                                val intent = Intent(this@CommodityAddActivity,FreightListActivity::class.java)
                                intent.putExtra("freightId",it.commodityPublishBean.freightId)
                                startActivityForResult(intent,9)
                            }
                            "附加信息" ->{
                                val intent = Intent(this@CommodityAddActivity, AdditionalCommodityActivity::class.java)
                                intent.putExtra("json",it.commodityPublishBean.addition)
                                startActivityForResult(intent ,10)
                            }
                            "卖家公告" ->{
                                val intent = Intent(this@CommodityAddActivity,CommodityAddSellerNoticeActivity::class.java)
                                intent.putExtra("sellerNotice",it.commodityPublishBean.sellerNotice)
                                startActivityForResult(intent,11)
                            }
                            "购买须知" ->{
                                val intent = Intent(this@CommodityAddActivity,CommodityPurchaseNotesActivity::class.java)
                                intent.putExtra("json",Gson().toJson(it.cpEntity?.datas?.buyNotesArray))
                                intent.putExtra("notes",Gson().toJson(it.commodityPublishBean.buyNotes))
                                startActivityForResult( intent ,12)
                            }
                            "商品规格" ->{
                                val intent = Intent(this@CommodityAddActivity,CommoditySpecificationActivity::class.java)
                                intent.putExtra("json",Gson().toJson(it.commodityPublishBean))
                                startActivityForResult(intent,13)
                            }
                            "商品详情" ->{
                                val intent = Intent(this@CommodityAddActivity,CommodityDetailsEditActivity::class.java)
                                intent.putExtra("info",it.commodityPublishBean.shopDetails)
                                startActivityForResult(intent,14)
                            }
                        }
                    }

                }
            }
        })
        binding.viewModel?.adapter?.get()?.let {

            it.add(ItemTitleBean("商品分类",R.color.white))
            it.add(ItemCommodityAddBean("商品分类",""))

            if (userIdentity == "VIP_PRODUCT_MERCHANT"){
                it.add(ItemCommodityAddBean("会员属性",""))
            }else{
                binding.viewModel?.commodityPublishBean?.isVipShop = 0
            }

            it.add(ItemCommodityAddBean("一级分类",""))

            if (userIdentity == "OPERATIONAL_MANAGER"){
                it.add(ItemCommodityAddBean("身份限购",""))
                it.add(ItemCommodityAddBean("售后客服",""))
            }else{
                binding.viewModel?.commodityPublishBean?.buyerGrade = 0
            }

            it.add(ItemLineBean(12,R.color.bg_fa))
            it.add(ItemTitleBean("图文详情",R.color.white))
            it.add(ItemCommodityAddBean("商品名称","","请输入商品名称"))
            it.add(ItemCommodityAddBean("商品图片","(建议比例1:1)",true).apply {
                height.set(Dp2PxUtils.dip2px(90).toInt())
            })
            it.add(ItemCommodityAddBean("商品详情",""))
            it.add(ItemCommodityAddBean("商品规格",""))

            it.add(ItemLineBean(12,R.color.bg_fa))
            it.add(ItemTitleBean("商品属性",R.color.white))
            it.add(ItemCommodityAddBean("定时上架",false,object :OnItemSwitchListener{
                override fun onItemSwitch(position: Int, bean: BaseBean, switch: Boolean) {
                    if (switch){
                        it.add(position+1,ItemCommodityAddBean("定时上架时间",""))
                        it.notifyInserted(position+1)
                        binding.viewModel?.commodityPublishBean?.isTimmingUp = 1
                    }else{
                        it.notifyDelete(position+1)
                        binding.viewModel?.commodityPublishBean?.isTimmingUp = 0
                    }
                }
            }))

            it.add(ItemLineBean(12,R.color.bg_fa))
            it.add(ItemCommodityAddBean("地区限制",""))
            it.add(ItemCommodityAddBean("活动时间",""))
            it.add(ItemCommodityAddBean("发行时间（选填）","",))
            it.add(ItemCommodityAddBean("配送时间（选填）",""))
            it.add(ItemCommodityAddBean("关联爱豆",""))
            if (SpManager.isOverseasSystem()){
                it.add(ItemCommodityAddBean("海外直邮",true).apply {
                    canEdit = false
                })
            }else{
                it.add(ItemCommodityAddBean("海外直邮",false))
            }
            it.add(ItemCommodityAddBean("是否推送",false))

            it.add(ItemLineBean(12,R.color.bg_fa))
            it.add(ItemCommodityAddBean("是否包邮",false,object :OnItemSwitchListener{
                override fun onItemSwitch(position: Int, bean: BaseBean, switch: Boolean) {
                    if (switch){
                        it.notifyDelete(position+1)
                        binding.viewModel?.commodityPublishBean?.freightId = "0"
                    }else{
                        it.add(position+1,ItemCommodityAddBean("邮费模板",""))
                        it.notifyInserted(position+1)
                        binding.viewModel?.commodityPublishBean?.freightId = ""
                    }
                }
            }))
            it.add(ItemCommodityAddBean("邮费模板",""))

            it.add(ItemLineBean(12,R.color.bg_fa))
            it.add(ItemCommodityAddBean("附加信息",""))
            it.add(ItemCommodityAddBean("卖家公告",""))
            it.add(ItemCommodityAddBean("购买须知",""))

            it.notifyDataSetChanged()
        }

        binding.viewModel?.queryCommodityPrecondition()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.viewModel?.onActivityResult(requestCode, resultCode, data)
    }

    var count = 1
    override fun onBackPressed() {
        if (count==1){
            val pop = BasePopWindow(this,R.layout.pop_common_content)
            pop.popModel = BasePopViewModel().apply {
                content.set("正在编辑商品，退出后不保存，是否确认退出？")
                listener = object : BasePopWindow.OnPopClickListener{
                    override fun onPopLeft(model: BaseViewModel) {
                        pop.dismiss()
                    }

                    override fun onPopRight(model: BaseViewModel) {
                        pop.dismiss()
                        count+=1
                        onBackPressed()
                    }
                }
            }
            pop.showAtCenter(binding.recyclerView)
        }else{
            super.onBackPressed()
        }
    }

}