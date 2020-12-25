package com.idolmedia.yzymanager.model.entity

import com.idolmedia.yzymanager.base.BaseEntity

/**
 *  时间：2020/11/4-10:31
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.entity AdditionalEntity
 *  描述：
 */
class AdditionalEntity : BaseEntity(){

    var datas = ArrayList<Additional2>()

    class Additional2{
        var id :String? = ""
        var key = ""
        /**商品编辑时返回此字段*/
        var addition_key = ""
        var field = ""
        var placehold = ""
        var addition_is = ""
        var rowNumber = ""
        var addition_isalter = ""
        var LAY_TABLE_INDEX = ""
        var addition_value = ""
        var isAddition = ""

        var additionIs = ""
        var additionIsalter = ""

    }

    /**公共模板附加信息格式*/
    class AdditionalCommon{
        var key = ""
        var field = ""
        var placehold = ""
        var isAddition = ""
        /**0必选 1非必选*/
        var additionIs = ""
        /**0可编辑 1不可编辑*/
        var additionIsalter = ""
    }

    /**用于提交保存时的附加信息格式*/
    class AdditionalSave{
        /**自定义时需要自己创建唯一标识*/
        var addition_key = ""
        var field = ""
        var placehold = ""
        var isAddition = ""
        /**0必选 1非必选*/
        var addition_is = ""
        /**0可编辑 1不可编辑*/
        var addition_isalter = ""
        /**排序标识*/
        var rowNumber = ""
    }

}