package com.idolmedia.yzymanager.viewmodel.freight

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.interfaces.OnAddressSelectListener
import com.idolmedia.yzymanager.model.entity.FreightAddressEntity

class ItemFreightAddressAreaBean(val listener: OnAddressSelectListener) : BaseBean() {

    constructor(listener: OnAddressSelectListener,area:FreightAddressEntity.Area):this(listener){
        layoutId = R.layout.item_freight_address_area
        this.area = area
        strName.set(area.qy_name)
    }

    constructor(listener: OnAddressSelectListener,area:FreightAddressEntity.Area, city : FreightAddressEntity.City):this(listener){
        layoutId = R.layout.item_freight_address_city
        this.area = area
        this.city = city
        strName.set(city.city_name)
    }

    var layoutId = R.layout.item_freight_address_area
    override fun getViewType() = layoutId

    val isSelect = ObservableBoolean()
    val strName = ObservableField<String>()
    val isChecked = ObservableBoolean()
    var area : FreightAddressEntity.Area? = null
    var city : FreightAddressEntity.City? = null

    fun onClickSelectArea(view: View){
        if (!isChecked.get()){
            isSelect.set(!isSelect.get())
            listener.onAddressSelect(position,isSelect.get(),this)
        }
    }

    fun onClickSelectCity(view:View){
        if (!isChecked.get()){
            isSelect.set(!isSelect.get())
            listener.onAddressSelect(position,isSelect.get(),this)
        }
    }

}