package com.idolmedia.yzymanager.viewmodel.freight

import android.util.Log
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnAddressSelectListener
import com.idolmedia.yzymanager.model.entity.FreightAddressEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil

class FreightAddressViewModel : BaseViewModel(), OnAddressSelectListener {

    val adapter = ObservableField<BaseAdapter>()
    var areaIds = ""
    var checkedIds = ""

    fun queryFreightAddress(){

        val str = SpManager.getValueString("FreightAddressEntity")
        if (str.isNullOrEmpty()){
            RetrofitHelper.instance().getFreightAddress(observer = object : BaseObserver<FreightAddressEntity>(){
                override fun onSuccess(t: FreightAddressEntity) {
                    if (t.resultCode==1){
                        SpManager.saveValue("FreightAddressEntity",Gson().toJson(t))
                        showAddress(t)
                    }
                }

                override fun onError(msg: String) {
                    ToastUtil.show(msg)
                }

                override fun onComplete() {
                    super.onComplete()
                    finishRefresh.set(finishRefresh.get()+1)
                }

            })
        }
        else{
            val entity = Gson().fromJson(str,FreightAddressEntity::class.java)
            Log.d("====OkHttp",str)
            showAddress(entity)
            finishRefresh.set(finishRefresh.get()+1)
        }

    }

    private fun showAddress(t:FreightAddressEntity){
        adapter.get()?.let {
            it.clear()
            for(area in t.datas){
                it.add(ItemFreightAddressAreaBean(this@FreightAddressViewModel,area))
                for(city in area.city_items){
                    it.add(ItemFreightAddressAreaBean(this@FreightAddressViewModel,area,city))
                }
            }
            it.notifyDataSetChanged()
        }

        ergodicItem()
    }

    private fun ergodicItem(){

        adapter.get()?.let {
            if (areaIds.isNotEmpty()){

                val cityIds = areaIds.split(",")

                for((position,bean) in it.getDate().withIndex()){
                    bean as ItemFreightAddressAreaBean
                    if (bean.city == null){
                        bean.area?.let { area ->
                            bean.isSelect.set(true)
                            for(index in position+1 .. position + area.city_items.size){
                                if (index<it.getMaxPosition()){
                                    val cityBean = it.getDate()[index]
                                    if (cityBean is ItemFreightAddressAreaBean){
                                        cityBean.city?.let { city->
                                            cityBean.isSelect.set(false)
                                            for(cityId in cityIds){
                                                if (cityId == city.city_code){
                                                    cityBean.isSelect.set(true)
                                                    Log.d("====cityBean",city.city_name)
                                                }
                                            }
                                            if (!cityBean.isSelect.get()){
                                                bean.isSelect.set(false)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (checkedIds.isNotEmpty()){
                for((position,bean) in it.getDate().withIndex()){
                    if (bean is ItemFreightAddressAreaBean){
                        if (bean.city==null){
                            bean.isChecked.set(true)
                            for(index in position .. position+bean.area!!.city_items.size){
                                if (index<it.getMaxPosition()){
                                    val cityBean = it.getDate()[index]
                                    if (cityBean is ItemFreightAddressAreaBean){
                                        cityBean.city?.let { city->
                                            if (checkedIds.contains(city.city_code)) {
                                                cityBean.isChecked.set(true)
                                            }else{
                                                bean.isChecked.set(false)
                                            }
                                        }

                                    }
                                }

                            }
                        }
                    }
                }
            }
        }

    }

    override fun onAddressSelect(position: Int, isSelect: Boolean, bean: BaseBean) {
        adapter.get()?.let {
            if (bean is ItemFreightAddressAreaBean){
                if (bean.layoutId == R.layout.item_freight_address_area){
                    for(index in position .. position+bean.area!!.city_items.size){
                        val item = it.getDate()[index] as ItemFreightAddressAreaBean
                        if (item.layoutId == R.layout.item_freight_address_city){
                            if(!item.isChecked.get()){
                                item.isSelect.set(isSelect)
                            }
                        }
                    }
                }
                else{
                    val parentId = bean.area!!.qy_name
                    for(areaBean in it.getDate()){
                        if (areaBean is ItemFreightAddressAreaBean){
                            if (areaBean.area!!.qy_name==parentId){
                                for(index in areaBean.position .. areaBean.position+areaBean.area!!.city_items.size){
                                    areaBean.isSelect.set(true)
                                    val city = it.getDate()[index]
                                    if (city is ItemFreightAddressAreaBean){
                                        if (!city.isSelect.get() && !city.isChecked.get()){
                                            areaBean.isSelect.set(false)
                                            break
                                        }
                                    }
                                }

                                break
                            }
                        }
                    }
                }
            }
        }

    }

}