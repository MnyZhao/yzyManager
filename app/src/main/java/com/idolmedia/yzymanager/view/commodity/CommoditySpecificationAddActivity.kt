package com.idolmedia.yzymanager.view.commodity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityCommodityAddBuySpecificationBinding
import com.idolmedia.yzymanager.interfaces.OnItemSwitchListener
import com.idolmedia.yzymanager.model.bean.SpecificationBean
import com.idolmedia.yzymanager.utils.PhotoUtils
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityBuySpecificationAddViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia

/**
 *  时间：2020/10/26-16:47
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.commodity CommodityAddBuySpecificationActivity
 *  描述：添加商品添加规格页Activity
 */
class CommoditySpecificationAddActivity : BaseStateActivity<ActivityCommodityAddBuySpecificationBinding>(),OnItemSwitchListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_commodity_add_buy_specification
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(CommodityBuySpecificationAddViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("添加规格"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val isEdit = intent.getBooleanExtra("isEdit",false)
        binding.viewModel?.isEdit = isEdit

        val imgUrl = intent.getStringExtra("imgUrl") ?: ""
        val isVipShop = intent.getIntExtra("isVipShop",0)
        binding.viewModel?.isVipShop = isVipShop
        val json = intent.getStringExtra("json") ?: ""
        if (json.isNotEmpty()){
            val bean = Gson().fromJson(json,SpecificationBean::class.java)
            binding.viewModel?.bean = bean
        }else{
            val cataFlag = intent.getIntExtra("cataFlag",0)
            binding.viewModel?.cataFlag = cataFlag
            binding.viewModel?.djSscId = intent.getStringExtra("djSscId") ?: ""
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.viewModel?.adapter?.get()?.let {

            val bean = binding.viewModel?.bean

            if (binding.viewModel?.cataFlag == 2){
                val catalogTitle = intent.getStringExtra("catalogTitle") ?: ""
                it.add(ItemCommodityAddBean("规格名称",catalogTitle,"请输入规格名称"))
            }else{
                it.add(ItemCommodityAddBean("规格名称",bean?.catalogTitle ?: "","请输入规格名称"))
            }

            if (isEdit){
                it.add(ItemCommodityAddBean("库存数量",bean?.surplusNo ?: ""))
            }else{
                it.add(ItemCommodityAddBean("库存数量",bean?.surplusNo ?: "","请输入数量","",InputType.TYPE_CLASS_NUMBER))
            }

            if (bean!=null){
                if (bean.limitBuy.isEmpty() || bean.limitBuy.toInt()<=0){
                    it.add(ItemCommodityAddBean("是否限购",false,this))
                }else{
                    it.add(ItemCommodityAddBean("是否限购",true,this))
                    it.add(ItemCommodityAddBean("限购数量",bean.limitBuy,"请输入数量","个",InputType.TYPE_CLASS_NUMBER))
                }
            }else{
                it.add(ItemCommodityAddBean("是否限购",false,this))
            }

            if (isVipShop == 1){
                //仅限会员购买
                it.add(ItemCommodityAddBean("划线价格",bean?.originalPrice ?: "","请输入价格","元",InputType.TYPE_CLASS_NUMBER  or InputType.TYPE_NUMBER_FLAG_DECIMAL))
                it.add(ItemCommodityAddBean("会员价格",bean?.vipPrice ?: "","请输入价格","元",InputType.TYPE_CLASS_NUMBER  or InputType.TYPE_NUMBER_FLAG_DECIMAL))
            }else if (isVipShop == 2){
                //皆可购买
                it.add(ItemCommodityAddBean("划线价格",bean?.originalPrice ?: "","请输入价格","元",InputType.TYPE_CLASS_NUMBER  or InputType.TYPE_NUMBER_FLAG_DECIMAL))
                it.add(ItemCommodityAddBean("普通价格",bean?.currentPrice ?: "","请输入价格","元",InputType.TYPE_CLASS_NUMBER  or InputType.TYPE_NUMBER_FLAG_DECIMAL))
                it.add(ItemCommodityAddBean("会员价格",bean?.vipPrice ?: "","请输入价格","元",InputType.TYPE_CLASS_NUMBER  or InputType.TYPE_NUMBER_FLAG_DECIMAL))
            }else{
                //普通商品
                it.add(ItemCommodityAddBean("划线价格",bean?.originalPrice ?: "","请输入价格","元",InputType.TYPE_CLASS_NUMBER  or InputType.TYPE_NUMBER_FLAG_DECIMAL))
                it.add(ItemCommodityAddBean("普通价格",bean?.currentPrice ?: "","请输入价格","元",InputType.TYPE_CLASS_NUMBER  or InputType.TYPE_NUMBER_FLAG_DECIMAL))
            }

            it.add(ItemCommodityAddBean("分类图片","(建议比例1:1)", bean?.catalogImg ?: "",false).apply {

                if (imgUrl.isNotEmpty()){
                    strImg.set(imgUrl)
                    imgRealPath = imgUrl
                    canDelete.set(true)
                }
                else if(bean != null){
                    strImg.set(bean.catalogImg)
                    imgRealPath = bean.catalogImg
                    canDelete.set(true)
                }

            })

            it.notifyDataSetChanged()
        }

        binding.tvSave.setOnClickListener {

            val bean = binding.viewModel?.getSpecificationBean()
            bean?.let {
                val intent = Intent()
                intent.putExtra("json",Gson().toJson(it))
                intent.putExtra("position",this.intent.getIntExtra("position",-1))
                setResult(1,intent)
                finish()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == PhotoUtils.CHOOSE_REQUEST){
            binding.viewModel?.adapter?.get()?.let {
                for(item in it.getDate()){
                    if (item is ItemCommodityAddBean){
                        if (item.isImg){
                            val list = PictureSelector.obtainMultipleResult(data) as ArrayList<LocalMedia>
                            item.upLoadImg(list[0].cutPath)
                            break
                        }
                    }
                }
            }
        }
    }

    override fun onItemSwitch(position: Int, bean: BaseBean, switch: Boolean) {
        if (switch){
            binding.viewModel?.adapter?.get()?.add(position+1,ItemCommodityAddBean("限购数量","","请输入数量","个",InputType.TYPE_CLASS_NUMBER))
            binding.viewModel?.adapter?.get()?.notifyInserted(position+1)
        }else{
            binding.viewModel?.adapter?.get()?.notifyDelete(position+1)
        }
    }

}