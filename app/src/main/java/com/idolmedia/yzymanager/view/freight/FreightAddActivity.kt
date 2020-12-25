package com.idolmedia.yzymanager.view.freight

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityFreightAddBinding
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.model.bean.FreightBean
import com.idolmedia.yzymanager.model.entity.FreightListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.utils.Dp2PxUtils
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.common.ItemLineBean
import com.idolmedia.yzymanager.viewmodel.common.ItemTitleBean
import com.idolmedia.yzymanager.viewmodel.freight.FreightAddViewModel
import com.idolmedia.yzymanager.viewmodel.freight.ItemFreightAddAreaBean
import com.idolmedia.yzymanager.viewmodel.freight.ItemFreightAddHeadBean

/**
 *  时间：2020/10/26-18:23
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.freight FreightAddActivity
 *  描述：添加运费模板页Activity
 */
class FreightAddActivity : BaseStateActivity<ActivityFreightAddBinding>(),OnItemSelectListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_freight_add
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(FreightAddViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("添加运费模板"))
        binding.viewModel?.isOverseas = intent.getBooleanExtra("isOverseas",SpManager.isOverseasSystem())
        binding.viewModel?.isMergeExpress = intent.getStringExtra("isMergeExpress") ?: "0"
    }

    override fun initView(savedInstanceState: Bundle?) {

        val json = intent.getStringExtra("json") ?: ""

        if (json.isNotEmpty()){
            val bean = Gson().fromJson(json,FreightListEntity.FreightItem::class.java)
            binding.viewModel?.bean = bean
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildAdapterPosition(view)
                if (position == parent.adapter!!.itemCount-1){
                    outRect.bottom = Dp2PxUtils.dip2px(120).toInt()
                }else{
                    outRect.bottom = 0
                }
            }
        })
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.viewModel?.adapter?.get()?.let {

            if(binding.viewModel?.bean == null){
                it.add(ItemFreightAddHeadBean())
                it.add(ItemLineBean(14))
                it.add(ItemTitleBean("追加特定地区运费模板"))
                it.add(ItemFreightAddAreaBean(true,this))
            }else{

                it.add(ItemFreightAddHeadBean(binding.viewModel?.bean!!))
                it.add(ItemLineBean(14))
                it.add(ItemTitleBean("追加特定地区运费模板"))

                for(item in binding.viewModel?.bean!!.freightitems){
                    it.add(ItemFreightAddAreaBean(item,this))
                }

                it.add(ItemFreightAddAreaBean(true,this))
            }

        }

        binding.tvSave.setOnClickListener {

            if (binding.viewModel?.canSave() == true){

                binding.viewModel?.saveFreight(object : BaseObserver<BaseEntity>(){
                    override fun onSuccess(t: BaseEntity) {
                        if (t.resultCode==1){
                            setResult(1)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1){
            val areaIds = data?.getStringExtra("areaIds") ?: ""
            val areaNames = data?.getStringExtra("areaNames") ?: ""
            binding.viewModel?.adapter?.get()?.let {
                val index = it.getMaxPosition()-1
                if (requestCode == index){
                    it.add(index,ItemFreightAddAreaBean(areaIds,areaNames,this))
                    it.notifyInserted(index)
                }else{
                    val bean = it.getDate()[requestCode] as ItemFreightAddAreaBean
                    bean.bean.areaIds = areaIds
                    bean.bean.areaNames = areaNames
                    bean.strArea.set(areaNames)
                }
            }
        }
        else if(resultCode==2){
            val json = data?.getStringExtra("freight") ?: ""
            val freight = Gson().fromJson(json,FreightBean::class.java)
            val bean = binding.viewModel?.adapter?.get()?.getDate()!![requestCode] as ItemFreightAddAreaBean
            bean.bean.firstPart = freight.firstPart
            bean.bean.firstPrice= freight.firstPrice
            bean.bean.continuePart = freight.continuePart
            bean.bean.continuePrice = freight.continuePrice
        }
    }

    override fun onItemSelect(position: Int, bean: BaseBean) {
        bean as ItemFreightAddAreaBean
        val intent = Intent(this,FreightAddressActivity::class.java)

        val list = binding.viewModel?.adapter?.get()?.getDate()!!
        if (bean.layoutId == R.layout.item_freight_add_area_add){
            intent.putExtra("areaIds","")

            val ids = StringBuilder()

            for(item in list){
                if (item is ItemFreightAddAreaBean){
                    if (ids.toString().isEmpty()){
                        ids.append(item.bean.areaIds)
                    }else{
                        ids.append(","+item.bean.areaIds)
                    }
                }
            }

            intent.putExtra("checkedIds",ids.toString())

        }else{
            intent.putExtra("areaIds",bean.bean.areaIds)

            val ids = StringBuilder()

            for(item in list){
                if (item is ItemFreightAddAreaBean){
                    if (item.position!=position){
                        if (ids.toString().isEmpty()){
                            ids.append(item.bean.areaIds)
                        }else{
                            ids.append(","+item.bean.areaIds)
                        }
                    }
                }
            }

            intent.putExtra("checkedIds",ids.toString())

        }

        startActivityForResult(intent,position)
    }

}