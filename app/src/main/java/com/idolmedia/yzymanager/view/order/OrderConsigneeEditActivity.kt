package com.idolmedia.yzymanager.view.order

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityOrderEditLogisticsBinding
import com.idolmedia.yzymanager.model.bean.CityBean
import com.idolmedia.yzymanager.model.entity.OrderDetailsEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.TimeUtils
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.idolmedia.yzymanager.viewmodel.order.ItemOrderAddressBean
import com.idolmedia.yzymanager.viewmodel.order.ItemOrderDetailsBean
import com.idolmedia.yzymanager.viewmodel.order.OrderEditViewModel
import org.json.JSONArray

/**
 *  时间：2020/11/17-17:41
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.order OrderAdditionalEditActivity
 *  描述：编辑订单收货人信息页Activity
 */
class OrderConsigneeEditActivity : BaseStateActivity<ActivityOrderEditLogisticsBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_order_edit_logistics
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(OrderEditViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("编辑订单收货人信息"))
    }

    /**地址：省份*/
    private val options1Items =  ArrayList<CityBean>()
    /**地址：市*/
    private val options2Items =  ArrayList<List<CityBean>>()
    /**地址：区*/
    private val options3Items =  ArrayList<List<List<CityBean>>>()

    override fun initView(savedInstanceState: Bundle?) {
        binding.viewModel?.orderNum = intent.getStringExtra("orderNum") ?: ""
        val json = intent.getStringExtra("json") ?: ""
        val order = Gson().fromJson(json,OrderDetailsEntity.Datas::class.java)
        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = BaseAdapter(this).apply {
            setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
                override fun itemClick(position: Int, item: BaseBean, parent: View) {
                    if (item is ItemOrderDetailsBean){
                        if(item.strTitle.get() == "省市区"){
                            showTimePicker(order,item)
                        }
                    }
                }
            })
        }
        adapter.add(ItemCommodityAddBean("收货人信息"))
        adapter.add(ItemOrderDetailsBean("收货人姓名",order.consignee))
        adapter.add(ItemOrderDetailsBean("收货电话",order.phone,"请填写收货电话"))
        adapter.add(ItemOrderDetailsBean("省市区",order.province+order.city+order.county,View.VISIBLE))
        adapter.add(ItemOrderAddressBean(order.consigneeAddress))

        binding.viewModel?.adapter?.set(adapter)

        binding.tvSave.setOnClickListener {

            binding.viewModel?.adapter?.get()?.let {

                val map = HashMap<String,Any?>()

                for (item in it.getDate()){
                    if (item is ItemOrderDetailsBean){
                        if (item.strTitle.get() == "收货人姓名"){
                            if (item.strContent.get().isNullOrEmpty()){
                                ToastUtil.show("请填写收货人姓名")
                                return@setOnClickListener
                            }else{
                                order.consignee = item.strContent.get() ?: ""
                            }
                        }
                        else if (item.strTitle.get() == "收货电话"){
                            if (item.strContent.get().isNullOrEmpty()){
                                ToastUtil.show("请填写收货电话")
                                return@setOnClickListener
                            }else{
                                order.phone = item.strContent.get() ?: ""
                            }
                        }
                    }
                    else if(item is ItemOrderAddressBean){
                        if (item.strContent.get().isNullOrEmpty()){
                            ToastUtil.show("请填写详细地址")
                            return@setOnClickListener
                        }else {
                            map["consigneeAddress"] = item.strContent.get() ?: ""
                        }
                    }
                }

                map["areaId"] = order.areaId
                map["city"] = order.city
                map["consignee"] = order.consignee
                map["county"] = order.county
                map["idCard"] = order.idCard
                map["isBusiness"] = !SpManager.userIsManage()
                map["mergeOrderNum"] = order.orderNum
                map["name"] = order.name
                map["phone"] = order.phone
                map["province"] = order.province

                saveLogistics(map)
            }

        }

    }

    private fun saveLogistics(map: HashMap<String,Any?>){
        RetrofitHelper.instance().saveOrderConsignee(map,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("操作成功")
                    setResult(1)
                    finish()
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    private fun showTimePicker(order:OrderDetailsEntity.Datas,bean:ItemOrderDetailsBean){

        hideKeyboard(binding.tvSave)

        if (options1Items.size<=0){
            initCityJson(this)
        }

        val options = OptionsPickerBuilder(this) { options1, options2, options3, v ->
            if (options1Items.size > options1) {
                order.province = options1Items[options1].name
            }
            if (options2Items.size > options1 && options2Items[options1].size > options2) {
                order.city = options2Items[options1][options2].name
            } else {
                order.city = ""
            }
            if (options3Items.size > options1 && options3Items[options1].size > options2 && options3Items[options1][options2].size > options3) {
                order.county = options3Items[options1][options2][options3].name
            } else {
                order.county = order.city
            }
            bean.strContent.set("${order.province}${order.city}${order.county}")
            if (options1Items.size > options1) {
                val code = options1Items[options1].code
                if (code == 0) {
                    order.areaId = options3Items[options1][options2][options3].code.toString()
                } else {
                    order.areaId = options1Items[options1].code.toString()
                }
            }

        }
            .setTitleText("城市选择")
            .setDividerColor(Color.BLACK)
            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
            .setContentTextSize(18)
            .build<CityBean>()
        options.setPicker(options1Items,options2Items,options3Items)
        options.show()
    }

    private fun initCityJson(context: Context){
        val jsonData = TimeUtils.getJson(context, "city.json")//获取assets目录下的json文件数据

        options1Items.clear()
        options2Items.clear()
        options3Items.clear()
        try {
            val data = JSONArray(jsonData)
            for(i in 0 until data.length()){
                val bean =Gson().fromJson(data.optJSONObject(i).toString(),CityBean::class.java)
                options1Items.add(bean)
                options2Items.add(bean.children)
                val area = ArrayList<ArrayList<CityBean>>()
                for(item in bean.children){
                    val province = ArrayList<CityBean>()
                    for (p in item.children){
                        province.add(p)
                    }
                    area.add(province)
                }
                options3Items.add(area)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

}