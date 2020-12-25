package com.idolmedia.yzymanager.view.commodity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommodityFinalPaymentDetailsBinding
import com.idolmedia.yzymanager.interfaces.OnFinalPaymentListener
import com.idolmedia.yzymanager.interfaces.OnItemSwitchListener
import com.idolmedia.yzymanager.model.bean.CommodityPublishBean
import com.idolmedia.yzymanager.model.bean.SpecificationBean
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.view.additional.AdditionalCommodityActivity
import com.idolmedia.yzymanager.view.freight.FreightListActivity
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityFinalPaymentViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.idolmedia.yzymanager.viewmodel.commodity.ItemFinalPaymentClassifyBean
import com.idolmedia.yzymanager.viewmodel.common.ItemLineBean
import java.lang.StringBuilder

/**
 *  时间：2020/10/26-11:27
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddFinalPaymentActivity
 *  描述：商品管理尾款详情页Activity
 */
class CommodityFinalPaymentActivity : BaseStateActivity<ActivityCommodityFinalPaymentDetailsBinding>() , OnFinalPaymentListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_final_payment_details
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityFinalPaymentViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("添加尾款"))
        binding.viewModel?.isAudit = intent.getBooleanExtra("isAudit",false)
        //审核中的商品选择邮费模板时要选择商户的
        binding.viewModel?.fromId = intent.getStringExtra("fromId") ?: ""
        binding.viewModel?.shopId = intent.getStringExtra("shopId") ?: ""
        binding.viewModel?.shopName = intent.getStringExtra("shopName") ?: ""
        binding.viewModel?.id = intent.getStringExtra("id") ?: ""
        binding.viewModel?.isVipShop = intent.getStringExtra("isVipShop")?.toInt() ?: 0
        binding.viewModel?.listener = this
    }

    override fun initView(savedInstanceState: Bundle?) {

        //商品详情里的尾款详情
        val json = intent.getStringExtra("json") ?: ""
        //定金商品的尾款审核
        val json1 = intent.getStringExtra("json1") ?: ""
        //俩json都没有就是开启尾款

        Gson().fromJson(json,CommodityPublishBean::class.java)?.let {
            binding.viewModel?.startTime = it.finalStartTime
            binding.viewModel?.endTime = it.finalEndTime
            binding.viewModel?.isLimitTime = it.isLimitTime
            binding.viewModel?.freightId = it.freightId
            binding.viewModel?.pushType = it.pushType.toInt()
            binding.viewModel?.addition = it.addition
            binding.viewModel?.sortCategory(it.catalogItems)
            binding.tvSave.text = "保存"
        }

        Gson().fromJson(json1,CommodityListEntity.Data::class.java)?.let {
            binding.viewModel?.startTime = it.finalStartTime
            binding.viewModel?.endTime = it.finalEndTime
            binding.viewModel?.isLimitTime = it.limitless == "0"
            binding.viewModel?.freightId = it.freightId
            binding.viewModel?.addition = it.addition
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.viewModel?.adapter?.set(BaseAdapter(this))
        binding.viewModel?.adapter?.get()?.setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
            override fun itemClick(position: Int, item: BaseBean, parent: View) {
                if (item is ItemCommodityAddBean){
                    when(item.strTitle.get()){
                        "活动时间" ->{
                            val intent = Intent(this@CommodityFinalPaymentActivity,CommodityAddActivityTimeActivity::class.java)
                            intent.putExtra("isLimitTime",binding.viewModel?.isLimitTime)
                            intent.putExtra("startTime",binding.viewModel?.startTime)
                            intent.putExtra("endTime",binding.viewModel?.endTime)
                            intent.putExtra("reserveStatus",2)
                            startActivityForResult(intent,1007)
                        }
                        "邮费模板" ->{
                            val intent = Intent(this@CommodityFinalPaymentActivity, FreightListActivity::class.java)
                            intent.putExtra("freightId",binding.viewModel?.freightId)
                            intent.putExtra("fromId",binding.viewModel?.fromId)
                            startActivityForResult(intent,1009)
                        }
                        "附加信息" ->{
                            val intent = Intent(this@CommodityFinalPaymentActivity, AdditionalCommodityActivity::class.java)
                            intent.putExtra("json",binding.viewModel?.addition)
                            startActivityForResult(intent ,1010)
                        }
                    }
                }
            }

        })
        binding.viewModel?.adapter?.get()?.let {

            it.add(ItemCommodityAddBean("尾款详情"))
            it.add(ItemCommodityAddBean("活动时间",if (binding.viewModel?.startTime.isNullOrEmpty())"" else "已编辑"))
            it.add(ItemCommodityAddBean("是否推送", binding.viewModel?.pushType == 1,object : OnItemSwitchListener {
                override fun onItemSwitch(position: Int, bean: BaseBean, switch: Boolean) {
                    if (switch){
                        binding.viewModel?.pushType = 1
                    }else{
                        binding.viewModel?.pushType = 0
                    }
                }
            }))

            //是否包邮
            val freightOpen = if (binding.viewModel?.freightId.isNullOrEmpty()){
                false
            }else binding.viewModel?.freightId == "0"

            it.add(ItemCommodityAddBean("是否包邮", freightOpen,object : OnItemSwitchListener {
                override fun onItemSwitch(position: Int, bean: BaseBean, switch: Boolean) {
                    if (switch){
                        it.notifyDelete(position+1)
                        binding.viewModel?.freightId = "0"
                    }else{
                        it.add(position+1,ItemCommodityAddBean("邮费模板",""))
                        it.notifyInserted(position+1)
                        binding.viewModel?.freightId = ""
                    }
                }
            }))
            if (!freightOpen){
                it.add(ItemCommodityAddBean("邮费模板",if (binding.viewModel?.freightId=="0" || binding.viewModel?.freightId.isNullOrEmpty()) "" else "已编辑"))
            }
            it.add(ItemLineBean(12,R.color.bg_fa))
            it.add(ItemCommodityAddBean("附加信息",if (!binding.viewModel?.addition.isNullOrEmpty() ) "已编辑" else ""))
            it.add(ItemCommodityAddBean("商品规格-补尾款","").apply {
                visibleMoreButton.set(View.GONE)
            })

            binding.viewModel?.categoryList?.let { list->
                for(item in list){
                    if (item.cataFlag == "1"){
                        binding.viewModel!!.reserveSize+=1
                    }
                    it.add(ItemFinalPaymentClassifyBean(item,binding.viewModel!!.isAudit,this,binding.viewModel?.isVipShop ?: 0).apply {
                        strShopName.set("商家信息:${binding.viewModel?.shopName}")
                        strShopId.set("商家ID:${binding.viewModel?.shopId}")
                    })
                }
            }

            it.notifyDataSetChanged()
        }

        binding.viewModel?.categoryList?.let {
            if (it.isEmpty()){
                binding.viewModel?.querySpecification()
            }
        }



        binding.tvSave.setOnClickListener {
            if ( binding.tvSave.text == "保存"){
                binding.viewModel?.saveFinal()?.let {
                    setResult(1,it)
                    finish()
                }
            }else{
                binding.viewModel?.openFinal(object : BaseObserver<BaseEntity>(){
                    override fun onSuccess(t: BaseEntity) {
                        if (t.resultCode==1){
                            if (SpManager.userIsManage()){
                                ToastUtil.show("操作成功，此商品已开启尾款")
                            }else{
                                ToastUtil.show("操作成功，此商品已提交尾款审核")
                            }
                            val intent = Intent()
                            intent.putExtra("position",this@CommodityFinalPaymentActivity.intent.getIntExtra("position",-1))
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

    override fun onItemDelete(position: Int, bean: BaseBean) {
        if (bean is ItemFinalPaymentClassifyBean){
            binding.viewModel?.let {
                if ( !bean.item?.catalogCheckId.isNullOrEmpty()){
                    if (it.deleteIds.toString().isEmpty()){
                        it.deleteIds.append(bean.item!!.catalogCheckId)
                    }else{
                        it.deleteIds.append(","+bean.item!!.catalogCheckId)
                    }
                }
            }
        }
        binding.viewModel?.adapter?.get()?.notifyDelete(position)
    }

    override fun onItemAdd(position: Int, bean: BaseBean) {
        binding.viewModel?.let {

            bean as ItemFinalPaymentClassifyBean

            if (it.reserveSize==1 || bean.position+1 == it.adapter.get()?.getDate()?.size){
                val intent = Intent(this,CommoditySpecificationAddActivity::class.java)
                intent.putExtra("position",position)
                intent.putExtra("djSscId",bean.item?.sscId)
                intent.putExtra("imgUrl",bean.item?.catalogImg)
                intent.putExtra("isVipShop",binding.viewModel?.isVipShop)
                intent.putExtra("cataFlag",2)
                intent.putExtra("catalogTitle",bean.item?.catalogTitle)

                startActivityForResult(intent,1000)
            }else {
                val item = it.adapter.get()?.getItem(bean.position+1)
                if (item is ItemFinalPaymentClassifyBean){
                    if (item.isAdd.get()){
                        val intent = Intent(this,CommoditySpecificationAddActivity::class.java)
                        intent.putExtra("position",position)
                        intent.putExtra("djSscId",bean.item?.sscId)
                        intent.putExtra("imgUrl",bean.item?.catalogImg)
                        intent.putExtra("isVipShop",binding.viewModel?.isVipShop)
                        intent.putExtra("cataFlag",2)
                        intent.putExtra("catalogTitle",bean.item?.catalogTitle)
                        startActivityForResult(intent,1000)
                    }else{
                        ToastUtil.show("已为此规格定金设置了尾款")
                    }
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==1){
            binding.viewModel?.let {
                val json = data?.getStringExtra("json") ?: ""
                if (requestCode==1000){
                    val bean = Gson().fromJson(json,SpecificationBean::class.java)
                    if (it.reserveSize>1){
                        //插入
                        val position = data?.getIntExtra("position",-1) ?: -1
                            it.adapter.get()?.add(position+1,ItemFinalPaymentClassifyBean(bean,false,this,it.isVipShop).apply {
                                strShopName.set("商家信息:${it.shopName}")
                                strShopId.set("商家ID:${it.shopId}")
                            })
                            it.adapter.get()?.notifyInserted(position+1)
                    }else{
                        //添加
                        it.adapter.get()?.add(ItemFinalPaymentClassifyBean(bean,false,this,it.isVipShop).apply {
                            strShopName.set("商家信息:${it.shopName}")
                            strShopId.set("商家ID:${it.shopId}")
                        })
                        it.adapter.get()?.notifyInsertedBodySize(1)
                    }
                }

                else if(requestCode == 1007){
                    val isLimit = data?.getBooleanExtra("isLimit",false) ?: false
                    val startTime = data?.getStringExtra("startTime") ?: ""
                    val endTime = data?.getStringExtra("endTime") ?: ""
                    binding.viewModel?.isLimitTime = isLimit
                    binding.viewModel?.startTime = startTime
                    binding.viewModel?.endTime = endTime
                    for(item in it.adapter.get()!!.getDate()){
                        if (item is ItemCommodityAddBean){
                            if (item.strTitle.get() == "活动时间"){
                                item.strContent.set("已编辑")
                                break
                            }
                        }
                    }
                }

                else if(requestCode == 1009){
                    val freightId = data?.getStringExtra("freightId") ?: ""
                    val freightName = data?.getStringExtra("freightName") ?: ""
                    binding.viewModel?.freightId = freightId
                    for(item in it.adapter.get()!!.getDate()){
                        if (item is ItemCommodityAddBean){
                            if (item.strTitle.get() == "邮费模板"){
                                item.strContent.set(freightName)
                                break
                            }
                        }
                    }
                }

                else if(requestCode == 1010){
                    val addition = data?.getStringExtra("json") ?: ""
                    if (addition.isNotEmpty()){
                        for(item in it.adapter.get()!!.getDate()){
                            if (item is ItemCommodityAddBean){
                                if (item.strTitle.get() == "附加信息"){
                                    item.strContent.set("已编辑")
                                    break
                                }
                            }
                        }
                    }
                    binding.viewModel?.addition = addition
                }

                else{
                    //编辑
                    val bean = Gson().fromJson(json,SpecificationBean::class.java)
                    val item = it.adapter.get()?.getItem(requestCode)
                    item?.let {
                        if (item is ItemFinalPaymentClassifyBean){
                            item.item = bean
                            item.showData()
                        }
                    }
                }
            }
        }
    }

}