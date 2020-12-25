package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity


class FreightAddressEntity : BaseEntity() {

    var countriesDatas = ArrayList<Overseas>()
    var datas = ArrayList<Area>()

    class Overseas{
        var code = ""
        var en_name = ""
        var name = ""
    }

    class Area{
        var city_items = ArrayList<City>()
        var qy_name = ""
    }

    class City{
        var city_code = ""
        var city_name = ""
    }

}