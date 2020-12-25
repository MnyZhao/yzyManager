package com.idolmedia.yzymanager.view.commodity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.*
import com.idolmedia.yzymanager.databinding.ActivityCommodityEditBinding
import com.idolmedia.yzymanager.interfaces.OnItemSwitchListener
import com.idolmedia.yzymanager.model.entity.AdditionalEntity
import com.idolmedia.yzymanager.model.entity.CommodityDetailsEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.utils.*
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.view.additional.AdditionalCommodityActivity
import com.idolmedia.yzymanager.view.freight.FreightListActivity
import com.idolmedia.yzymanager.view.ido.IdoAssociatedActivity
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityAddViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddImgBean
import com.idolmedia.yzymanager.viewmodel.common.ItemLineBean
import com.idolmedia.yzymanager.viewmodel.common.ItemTitleBean
import com.idolmedia.yzymanager.widget.CustomLoading

/**
 *  时间：2020/10/26-14:57
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddActivity
 *  描述：编辑商品页Activity
 */
class CommodityEditActivity : BaseStateActivity<ActivityCommodityEditBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_edit
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityAddViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel(if (SpManager.getUserIdentity() == "VIP_PRODUCT_MERCHANT") "编辑会员商品" else "编辑商品","提交",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                if (binding.viewModel?.adapter?.get()?.getDate().isNullOrEmpty()){
                    finish()
                }else{
                    onBackPressed()
                }
            }

            override fun onRightClick(view: View) {

                binding.viewModel?.saveEditCommodity(this@CommodityEditActivity,object : BaseObserver<BaseEntity>(){
                    override fun onSuccess(t: BaseEntity) {
                        if (t.resultCode==1){
                            ToastUtil.show(t.msg)
                            setResult(1)
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
        val vId = intent.getStringExtra("vId") ?: ""
        val vName = intent.getStringExtra("vName") ?: ""
        val commodityId = intent.getStringExtra("commodityId") ?: ""
        binding.viewModel?.commodityId = commodityId
        binding.viewModel?.isAudit = intent.getBooleanExtra("isAudit",false)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setItemViewCacheSize(50)
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.viewModel?.adapter?.set(BaseAdapter(this))
        binding.viewModel?.adapter?.get()?.setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
            override fun itemClick(position: Int, item: BaseBean, parent: View) {
                binding.viewModel?.let {
                    if (item is ItemCommodityAddBean){
                        when(item.strTitle.get()){
                            "一级分类" ->{
                                val intent = Intent(this@CommodityEditActivity,CommodityAddClassifyFirstActivity::class.java)
                                intent.putExtra("list",Gson().toJson(it.cpEntity?.datas?.goodsTypeArray))
                                intent.putExtra("goodsTypeId",it.commodityPublishBean.goodsTypeId)
                                startActivityForResult(intent ,3)
                            }
                            "身份限购" ->{
                                val intent = Intent(this@CommodityEditActivity,CommodityAddIdentityLimitActivity::class.java)
                                intent.putExtra("buyerGrade",it.commodityPublishBean.buyerGrade)
                                startActivityForResult(intent,4)
                            }
                            "售后客服" ->{
                                val intent = Intent(this@CommodityEditActivity,CommodityAddAfterSalesServiceActivity::class.java)
                                intent.putExtra("isProxies",it.commodityPublishBean.isProxies)
                                startActivityForResult(intent,5)
                            }
                            "地区限制" ->{
                                val intent = Intent(this@CommodityEditActivity,CommodityAddAreaLimitActivity::class.java)
                                intent.putExtra("isAreaLimit",it.commodityPublishBean.isAreaLimit)
                                startActivityForResult(intent,6)
                            }
                            "活动时间" ->{
                                val intent = Intent(this@CommodityEditActivity,CommodityAddActivityTimeActivity::class.java)
                                intent.putExtra("isLimitTime",it.commodityPublishBean.isLimitTime)
                                intent.putExtra("startTime",it.commodityPublishBean.startTime)
                                intent.putExtra("endTime",it.commodityPublishBean.endTime)
                                intent.putExtra("reserveStatus",it.commodityPublishBean.reserveStatus.toInt())
                                startActivityForResult(intent,7)
                            }
                            "定时上架时间","发行时间（选填）","配送时间（选填）" ->{
                                //时间选择器
                                val pvTime = TimePickerBuilder(this@CommodityEditActivity) { date, view ->
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
                                val intent = Intent(this@CommodityEditActivity,IdoAssociatedActivity::class.java)
                                intent.putExtra("aidouIds",it.commodityPublishBean.aidouIds)
                                intent.putExtra("idoList",it.commodityPublishBean.idoList)
                                startActivityForResult(intent ,8)
                            }
                            "邮费模板" ->{
                                val intent = Intent(this@CommodityEditActivity,FreightListActivity::class.java)
                                intent.putExtra("freightId",it.commodityPublishBean.freightId)
                                intent.putExtra("fromId",it.fromId)
                                startActivityForResult(intent,9)
                            }
                            "附加信息" ->{
                                val intent = Intent(this@CommodityEditActivity,
                                    AdditionalCommodityActivity::class.java)
                                intent.putExtra("json",it.commodityPublishBean.addition)
                                startActivityForResult(intent ,10)
                            }
                            "卖家公告" ->{
                                val intent = Intent(this@CommodityEditActivity,CommodityAddSellerNoticeActivity::class.java)
                                intent.putExtra("sellerNotice",it.commodityPublishBean.sellerNotice)
                                startActivityForResult(intent,11)
                            }
                            "购买须知" ->{
                                val intent = Intent(this@CommodityEditActivity,CommodityPurchaseNotesActivity::class.java)
                                intent.putExtra("json",Gson().toJson(it.cpEntity?.datas?.buyNotesArray))
                                intent.putExtra("notes",Gson().toJson(it.commodityPublishBean.buyNotes))
                                startActivityForResult( intent ,12)
                            }
                            "商品规格" ->{
                                val intent = Intent(this@CommodityEditActivity,CommoditySpecificationActivity::class.java)
                                intent.putExtra("json",Gson().toJson(it.commodityPublishBean))
                                intent.putExtra("isAudit",it.isAudit)
                                intent.putExtra("isEdit",true)
                                startActivityForResult(intent,13)
                            }
                            "商品详情" ->{
                                val intent = Intent(this@CommodityEditActivity,CommodityDetailsEditActivity::class.java)
                                intent.putExtra("info",it.commodityPublishBean.shopDetails)
                                startActivityForResult(intent,14)
                            }
                            "尾款信息" ->{
                                val intent = Intent(this@CommodityEditActivity,CommodityFinalPaymentActivity::class.java)
                                intent.putExtra("id",commodityId)
                                intent.putExtra("isVipShop",it.commodityPublishBean.isVipShop)
                                intent.putExtra("json",Gson().toJson(it.commodityPublishBean))
                                intent.putExtra("position",position)
                                intent.putExtra("isAudit",it.isAudit)
                                intent.putExtra("shopId",vId)
                                intent.putExtra("shopName",vName)
                                intent.putExtra("fromId",it.fromId)
                                startActivityForResult(intent,15)
                            }
                        }
                    }

                }
            }
        })

        binding.smartLayout.setOnRefreshListener { queryCommodityDetails() }
        binding.smartLayout.isEnableLoadmore = false
        binding.smartLayout.autoRefresh()

    }

    private fun queryCommodityDetails(){

        binding.viewModel?.queryCommodityDetails(object : BaseObserver<CommodityDetailsEntity>(){
            override fun onSuccess(t: CommodityDetailsEntity) {
                if (t.resultCode==1){

                    binding.smartLayout.isEnableRefresh = false

                    binding.viewModel?.timesTamp = t.datas.timesTamp
                    binding.viewModel?.fromId = t.datas.virtualuserId
                    binding.viewModel?.adapter?.get()?.let {

                        val userIdentity = SpManager.getUserIdentity()

                        it.clear()

                        it.add(ItemTitleBean("商品分类",R.color.white))

                        binding.viewModel?.commodityPublishBean?.shopType = t.datas.shopType
                        binding.viewModel?.commodityPublishBean?.reserveStatus = t.datas.reserveStatus
                        val shopType = when(t.datas.shopType){
                            "support" ->"应援商品"
                            "reserve2" ->"定金商品"
                            "crowdfunding" ->"众筹商品"
                            else ->"普通商品"
                        }
                        it.add(ItemCommodityAddBean("商品分类",shopType))


                        if (t.datas.isVipShop == "1" || t.datas.isVipShop == "2"){
                            binding.viewModel?.commodityPublishBean?.isVipShop = t.datas.isVipShop.toInt()
                            it.add(ItemCommodityAddBean("会员属性",if (t.datas.isVipShop == "1") "仅限会员购买" else "皆可购买"))
                        }else{
                            binding.viewModel?.commodityPublishBean?.isVipShop = 0
                        }

                        for(g in binding.viewModel?.cpEntity?.datas?.goodsTypeArray!!){
                            if (g.id == t.datas.goodsTypeId){
                                binding.viewModel?.commodityPublishBean?.goodsTypeId = t.datas.goodsTypeId
                                it.add(ItemCommodityAddBean("一级分类",g.goodsTypeName))
                                break
                            }
                        }

                        binding.viewModel?.commodityPublishBean?.buyerGrade = t.datas.buyerGrade
                        if (userIdentity == "OPERATIONAL_MANAGER"){
                            val status = when(t.datas.buyerGrade){
                                1 -> "第三方商家"
                                2 -> "粉丝团商家"
                                3 -> "第三方和粉丝团商家"
                                else ->"全部用户"

                            }
                            it.add(ItemCommodityAddBean("身份限购",status))

                            binding.viewModel?.commodityPublishBean?.isProxies = t.datas.isProxies

                            it.add(ItemCommodityAddBean("售后客服",if(t.datas.isProxies== 0) "客服自营" else "一直娱代理"))
                        }

                        it.add(ItemLineBean(12,R.color.bg_fa))

                        it.add(ItemTitleBean("图文详情",R.color.white))

                        binding.viewModel?.commodityPublishBean?.shopName = t.datas.shopName
                        it.add(ItemCommodityAddBean("商品名称",t.datas.shopName,"请输入商品名称"))

                        it.add(ItemCommodityAddBean("商品图片",if (t.datas.shopType == "support") "(建议比例2:1)" else "(建议比例1:1)",true).apply {
                            imgRealPath = t.datas.imageUrl
                            binding.viewModel?.commodityPublishBean?.imageUrl = t.datas.imageUrl
                            strImg.set(t.datas.imageUrl)
                            canDelete.set(true)
                            height.set(if (t.datas.shopType == "support") Dp2PxUtils.dip2px(180).toInt() else Dp2PxUtils.dip2px(90).toInt())
                            mainImgBg.set(if (t.datas.shopType == "support") R.mipmap.ic_img_add_admin_width else R.mipmap.ic_img_add_admin)
                            if (t.datas.imageUrlList.isNotEmpty()){
                                for((index,path) in t.datas.imageUrlList.withIndex()){
                                    when (index) {
                                        0 -> {
                                            viceFirst.set(ItemCommodityAddImgBean().apply {
                                                this.requestCode = PhotoUtils.CHOOSE_REQUEST_FIRST
                                                this.imgRealPath = path
                                                this.path.set(path)
                                                this.canDelete.set(true)
                                            })
                                        }
                                        1 -> {
                                            viceSecond.set(ItemCommodityAddImgBean().apply {
                                                this.requestCode = PhotoUtils.CHOOSE_REQUEST_SECOND
                                                this.imgRealPath = path
                                                this.path.set(path)
                                                this.canDelete.set(true)
                                            })
                                        }
                                        2 -> {
                                            viceThird.set(ItemCommodityAddImgBean().apply {
                                                this.requestCode = PhotoUtils.CHOOSE_REQUEST_THIRD
                                                this.imgRealPath = path
                                                this.path.set(path)
                                                this.canDelete.set(true)
                                            })
                                        }
                                        3 -> {
                                            viceFourth.set(ItemCommodityAddImgBean().apply {
                                                this.requestCode = PhotoUtils.CHOOSE_REQUEST_FOURTH
                                                this.imgRealPath = path
                                                this.path.set(path)
                                                this.canDelete.set(true)
                                            })
                                        }
                                    }
                                }
                            }

                        })

                        binding.viewModel?.commodityPublishBean?.shopDetails = t.datas.shopDetails ?: ""
                        it.add(ItemCommodityAddBean("商品详情","已编辑"))

                        for(item in t.datas.categoryList){
                            item.catalogEditType = "1"
                        }
                        binding.viewModel?.commodityPublishBean?.catalogItems = t.datas.categoryList
                        it.add(ItemCommodityAddBean("商品规格","已编辑"))

                        if (t.datas.shopType == "reserve2" && t.datas.reserveStatus == "2"){
                            it.add(ItemCommodityAddBean("尾款信息","已编辑"))
                        }

                        it.add(ItemLineBean(12,R.color.bg_fa))
                        it.add(ItemTitleBean("商品属性",R.color.white))

                        binding.viewModel?.commodityPublishBean?.isTimmingUp = t.datas.isTimmingUp
                        it.add(ItemCommodityAddBean("定时上架",t.datas.isTimmingUp == 1,object :OnItemSwitchListener{
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
                        if (t.datas.isTimmingUp==1){
                            binding.viewModel?.commodityPublishBean?.upTime = t.datas.upTime ?: ""
                            it.add(ItemCommodityAddBean("定时上架时间",t.datas.upTime))
                        }

                        it.add(ItemLineBean(12,R.color.bg_fa))


                        binding.viewModel?.commodityPublishBean?.isAreaLimit = t.datas.isAreaLimit ?: "2"
                        val strAreaLimit = when(t.datas.isAreaLimit){
                            "0" -> "仅限中国大陆购买"
                            "1" -> "仅限港澳台+国外购买"
                            "2" -> "全球用户皆可购买"
                            "3" -> "仅限港澳台购买"
                            "4" -> "仅限中国大陆+港澳台购买"
                            "5" -> "仅限国外购买"
                            "6" -> "仅限中国大陆+国外购买"
                            else ->""
                        }
                        it.add(ItemCommodityAddBean("地区限制",strAreaLimit))

                        binding.viewModel?.commodityPublishBean?.isLimitTime = t.datas.limitless=="0"
                        binding.viewModel?.commodityPublishBean?.finalStartTime = t.datas.finalStartTime ?: ""
                        binding.viewModel?.commodityPublishBean?.finalEndTime = t.datas.finalEndTime ?: ""
                        binding.viewModel?.commodityPublishBean?.startTime = t.datas.startTime ?: ""
                        binding.viewModel?.commodityPublishBean?.endTime = t.datas.endTime ?: ""
                        it.add(ItemCommodityAddBean("活动时间",""))

                        binding.viewModel?.commodityPublishBean?.issuingDate = t.datas.issuingDate ?: ""
                        it.add(ItemCommodityAddBean("发行时间（选填）",t.datas.issuingDate ?: ""))
                        binding.viewModel?.commodityPublishBean?.deliveryDate = t.datas.deliveryDate ?: ""
                        it.add(ItemCommodityAddBean("配送时间（选填）",t.datas.deliveryDate ?: ""))

                        binding.viewModel?.commodityPublishBean?.idoList = Gson().toJson(t.datas.aidouList)
                        val idoIds = StringBuilder()
                        val idoNames = StringBuilder()
                        for(item in t.datas.aidouList){
                            if (idoIds.toString().isEmpty()){
                                idoIds.append(item.idolId)
                                idoNames.append(item.idoName)
                            }else{
                                idoIds.append(",${item.idolId}")
                                idoNames.append(",${item.idoName}")
                            }
                        }
                        binding.viewModel?.commodityPublishBean?.aidouIds = idoIds.toString()

                        it.add(ItemCommodityAddBean("关联爱豆",idoNames.toString()))

                        binding.viewModel?.commodityPublishBean?.shoppingTo = t.datas.shoppingTo
                        it.add(ItemCommodityAddBean("海外直邮",t.datas.shoppingTo=="2").apply {
                            canEdit = !SpManager.isOverseasSystem()
                        })
                        binding.viewModel?.commodityPublishBean?.pushType = t.datas.pushType
                        it.add(ItemCommodityAddBean("是否推送",t.datas.pushType=="1"))

                        binding.viewModel?.commodityPublishBean?.freightId = t.datas.freightId ?: ""
                        if (t.datas.shopType != "reserve2"){
                            //非定金或者尾款状态时需要显示邮费模板
                            it.add(ItemCommodityAddBean("是否包邮",t.datas.freightId== "0" ,object :OnItemSwitchListener{
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
                            if (t.datas.freightId != "0"){
                                it.add(ItemCommodityAddBean("邮费模板","已编辑"))
                            }
                        }

                        it.add(ItemLineBean(12,R.color.bg_fa))

                        if (t.datas.shopType == "support" || t.datas.shopType == "crowdfunding"){
                            binding.viewModel?.commodityPublishBean?.supportMoney = t.datas.supportMoney
                            it.add(ItemCommodityAddBean("目标金额",t.datas.supportMoney,"请输入目标金额","元",
                                InputType.TYPE_CLASS_NUMBER  or InputType.TYPE_NUMBER_FLAG_DECIMAL))
                        }

                        val type = object : TypeToken<ArrayList<AdditionalEntity.AdditionalSave>>(){}.type
                        val additionList = Gson().fromJson<ArrayList<AdditionalEntity.AdditionalSave>>(t.datas.addition,type)
                        additionList?.let { list->
                            binding.viewModel?.commodityPublishBean?.addition = t.datas.addition
                            binding.viewModel?.commodityPublishBean?.additionList = list
                        }

                        if (t.datas.shopType != "reserve2"){
                            it.add(ItemCommodityAddBean("附加信息",if (additionList.isNullOrEmpty()) "" else "已编辑"))
                        }

                        binding.viewModel?.commodityPublishBean?.sellerNotice = t.datas.sellerNotice ?: ""
                        it.add(ItemCommodityAddBean("卖家公告",if (t.datas.sellerNotice.isNullOrEmpty()) "" else "已编辑"))

                        binding.viewModel?.commodityPublishBean?.buyNotes = t.datas.aboutArray
                        it.add(ItemCommodityAddBean("购买须知",if (t.datas.aboutArray.isEmpty()) "" else "已编辑"))

                        it.notifyDataSetChanged()
                    }

                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

            override fun onComplete() {
                super.onComplete()
                binding.viewModel?.let {
                    it.finishRefresh.set(it.finishRefresh.get()+1)
                }
            }

        })

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