package com.idolmedia.yzymanager.viewmodel.subject

import android.app.Activity
import android.content.Intent
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.*
import com.idolmedia.yzymanager.interfaces.OnResultListener
import com.idolmedia.yzymanager.model.bean.SubjectBean
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.model.entity.SubjectClassifyEntity
import com.idolmedia.yzymanager.model.entity.SubjectDetailsEntity
import com.idolmedia.yzymanager.model.entity.UploadImageEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.PhotoUtils
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddSelectBean
import com.idolmedia.yzymanager.viewmodel.common.ItemLineBean
import com.idolmedia.yzymanager.viewmodel.common.ItemTitleBean
import com.idolmedia.yzymanager.viewmodel.order.ItemOrderDetailsBean
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia

/**
 *  时间：2020/11/19-14:49
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.subject SubjectAddViewModel
 *  描述：
 */
class SubjectAddViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()
    val strContent = ObservableField<String>()

    var bean: SubjectBean? = null
    var subjectId = ""

    var subjectCatalogId = ""

    fun querySubjectClassify(){
        RetrofitHelper.instance().querySubjectClassify(object : BaseObserver<SubjectClassifyEntity>(){
            override fun onSuccess(t: SubjectClassifyEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {

                        it.clear()

                        for(item in t.datas){
                            it.add(ItemCommodityAddSelectBean(item,item.id == subjectCatalogId))
                        }

                        it.notifyDataSetChanged()

                    }
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

    fun querySubjectDetails(listener: OnResultListener){
        RetrofitHelper.instance().querySubjectDetails(subjectId,object : BaseObserver<SubjectDetailsEntity>(){
            override fun onSuccess(t: SubjectDetailsEntity) {
                if (t.resultCode==1){
                    adapter.get()?.let {
                        it.clear()

                        it.add(ItemTitleBean("专题详情", R.color.white))
                        it.add(ItemOrderDetailsBean("专题名称",t.datas.subjectName,"请输入专题名称"))
                        bean?.subjectCatalogId = t.datas.subjectCatalogId
                        it.add(ItemCommodityAddBean("专题分类",t.datas.sbjectCatalogTitle))

                        val label= StringBuilder()
                        val labelList = ArrayList<String>()
                        for(item in t.datas.labelList){
                            label.append(item.label+",")
                            labelList.add(item.label)
                        }
                        bean?.subjectLabels = Gson().toJson(labelList)
                        it.add(ItemCommodityAddBean("专题标签",label.toString()))

                        bean?.subjectDetail = t.datas.subjectDetails ?: ""
                        it.add(ItemCommodityAddBean("专题内容",if (t.datas.subjectDetails.isNullOrEmpty()) "" else "已编辑"))

                        bean?.previewUrl = t.datas.previewUrl
                        bean?.detailsHeadImgUrl = t.datas.detailsHeadImgUrl
                        it.add(ItemSubjectAddImageBean(this@SubjectAddViewModel).apply {
                            strMainImg.set(t.datas.previewUrl)
                            strHeadImg.set(t.datas.detailsHeadImgUrl)
                            canDeleteMain.set(true)
                            canDeleteHead.set(true)
                        })
                        it.add(ItemLineBean(12,R.color.bg_fa))
                        bean?.isMergeExpress = t.datas.isMergeExpress
                        it.add(ItemCommodityAddBean("是否合并运费",t.datas.isMergeExpress == "1").apply {
                            canEdit = false
                        })

                        val commodityList = ArrayList<CommodityListEntity.Data>()
                        val commodityOverseasList = ArrayList<CommodityListEntity.Data>()
                        t.datas.productList?.let {
                            for(item in t.datas.productList){
                                val c = CommodityListEntity.Data()
                                c.shopcommonId = item.shopcommonId
                                c.imageUrl = item.imageUrl
                                c.shopName = item.shopName
                                if (item.overseas){
                                    commodityOverseasList.add(c)
                                }else{
                                    commodityList.add(c)
                                }
                            }
                        }

                        bean?.commodityList = Gson().toJson(commodityList)
                        bean?.freightId = t.datas.freightId ?: ""
                        bean?.overseasFreightId = t.datas.overseasFreightId ?: ""
                        bean?.commodityOverseasList = Gson().toJson(commodityOverseasList)
                        it.add(ItemCommodityAddBean("添加国内商品",if (commodityList.isEmpty()) "" else "已编辑"))
                        it.add(ItemCommodityAddBean("添加海外商品",if (commodityOverseasList.isEmpty()) "" else "已编辑"))

                        val bookIds = ArrayList<CommodityListEntity.Data>()
                        t.datas.eleBookList?.let {
                            for(item in t.datas.eleBookList){
                                val c = CommodityListEntity.Data()
                                c.shopcommonId = item.shopcommonId
                                c.imageUrl = item.imageUrl
                                c.shopName = item.shopName
                                bookIds.add(c)
                            }
                        }

                        bean?.bookList =  Gson().toJson(bookIds)
                        it.add(ItemCommodityAddBean("添加电子刊",if (bookIds.isEmpty()) "" else "已编辑"))

                        listener.onResultSuccess(t)

                    }
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

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && PhotoUtils.verifyImgRequestIsCode(requestCode)){
            val list = PictureSelector.obtainMultipleResult(data) as ArrayList<LocalMedia>
            adapter.get()?.let {
                for(item in it.getDate()){
                    if (item is ItemSubjectAddImageBean){
                        item.upLoadImg(list[0].cutPath,object : BaseObserver<UploadImageEntity>(){
                            override fun onSuccess(t: UploadImageEntity) {
                                if (t.resultCode==1){
                                    if (requestCode == PhotoUtils.CHOOSE_REQUEST_FIRST){
                                        item.canDeleteMain.set(true)
                                        item.strMainImg.set(t.imageUrl)
                                        bean?.previewUrl = t.imageUrl
                                    }else{
                                        item.canDeleteHead.set(true)
                                        item.strHeadImg.set(t.imageUrl)
                                        bean?.detailsHeadImgUrl = t.imageUrl
                                    }
                                }
                            }

                            override fun onError(msg: String) {
                                ToastUtil.show(msg)
                            }

                        })

                    }
                }
            }
        }
        else if (resultCode==1){
            adapter.get()?.let {
                for(item in it.getDate()){
                    if (item is ItemCommodityAddBean){
                        if (requestCode == 0 && item.strTitle.get() == "专题分类"){
                            val id = data?.getStringExtra("id") ?: ""
                            val title = data?.getStringExtra("title") ?: ""
                            item.strContent.set(title)
                            bean?.subjectCatalogId  = id
                            break
                        }
                        else if(requestCode == 1 && item.strTitle.get() == "专题标签"){
                            val title = data?.getStringExtra("title") ?: ""
                            val json = data?.getStringExtra("json") ?: ""
                            item.strContent.set(title)
                            bean?.subjectLabels  = json
                            break
                        }
                        else if(requestCode == 2 && item.strTitle.get() == "专题内容"){
                            val json = data?.getStringExtra("json") ?: ""
                            item.strContent.set("已编辑")
                            bean?.subjectDetail= json
                            break
                        }
                        else if(requestCode == 3 && item.strTitle.get() == "添加国内商品"){
                            val json = data?.getStringExtra("json") ?: ""
                            item.strContent.set("已编辑")
                            bean?.commodityList= json
                            bean?.freightId = data?.getStringExtra("freightId") ?: ""
                            break
                        }
                        else if(requestCode == 4 && item.strTitle.get() == "添加海外商品"){
                            val json = data?.getStringExtra("json") ?: ""
                            item.strContent.set("已编辑")
                            bean?.commodityOverseasList= json
                            bean?.overseasFreightId = data?.getStringExtra("freightId") ?: ""
                            break
                        }
                        else if(requestCode == 5 && item.strTitle.get() == "添加电子刊"){
                            val json = data?.getStringExtra("json") ?: ""
                            if (json.isEmpty()){
                                item.strContent.set("")
                            }else{
                                item.strContent.set("已编辑")
                            }
                            bean?.bookList = json
                            break
                        }
                    }

                }
            }
        }
    }

    fun getSaveMap():HashMap<String,Any?>?{
        adapter.get()?.let {
            for(item in it.getDate()){
                if (item is ItemOrderDetailsBean){
                    if (item.strTitle.get() == "专题名称"){
                        if (item.strContent.get().isNullOrEmpty()){
                            ToastUtil.show("请设置专题名称")
                            return null
                        }else{
                            bean?.subjectName = item.strContent.get() ?: ""
                            break
                        }
                    }
                }
            }

            if (bean?.subjectCatalogId.isNullOrEmpty()){
                ToastUtil.show("请设置专题分类")
                return null
            }
            if (bean?.subjectDetail.isNullOrEmpty()){
                ToastUtil.show("请设置专题内容")
                return null
            }
            if (bean?.previewUrl.isNullOrEmpty()){
                ToastUtil.show("请设置专题封面图")
                return null
            }
            if (bean?.detailsHeadImgUrl.isNullOrEmpty()){
                ToastUtil.show("请设置专题头部图")
                return null
            }
            if (bean?.isMergeExpress == "1"){
                if (!bean?.commodityList.isNullOrEmpty() && bean?.commodityList!!.length>5 && bean?.freightId.isNullOrEmpty()){
                    //合并运费时若选择了商品必须选择运费模板
                    ToastUtil.show("请为国内商品选择运费模板")
                    return null
                }
                if (!bean?.commodityOverseasList.isNullOrEmpty() && bean?.commodityOverseasList!!.length>5  && bean?.overseasFreightId.isNullOrEmpty()){
                    //合并运费时若选择了商品必须选择运费模板
                    ToastUtil.show("请为海外商品选择运费模板")
                    return null
                }
            }
            val map = HashMap<String,Any?>()
            map["subjectName"] = bean?.subjectName ?: ""
            map["subjectCatalogId"] = bean?.subjectCatalogId ?: ""
            map["subjectLabels"] = bean?.subjectLabels ?: ""
            map["subjectDetail"] = bean?.subjectDetail ?: ""
            map["previewUrl"] = bean?.previewUrl ?: ""
            map["detailsHeadImgUrl"] = bean?.detailsHeadImgUrl ?: ""
            map["isMergeExpress"] = bean?.isMergeExpress?: ""
            map["existIsMergeOrder"] = "0"
            if (bean?.isMergeExpress == "1"){
                map["freightId"] = bean?.freightId
                map["overseasFreightId"] = bean?.overseasFreightId
            }
            map["modityType"] = true

            val list = ArrayList<SubjectBean.Commodity>()
            val type = object:TypeToken<ArrayList<CommodityListEntity.Data>>(){}.type
            if (!bean?.commodityList.isNullOrEmpty()){
                val c = Gson().fromJson<ArrayList<CommodityListEntity.Data>>(bean?.commodityList,type)
                c?.let {
                    for((index,item) in it.withIndex()){
                        list.add(SubjectBean.Commodity().apply {
                            shopcommonId = item.shopcommonId
                            sortNo = index
                            overseas = false
                        })
                    }
                }
            }
            if (!bean?.commodityOverseasList.isNullOrEmpty()){
                val c = Gson().fromJson<ArrayList<CommodityListEntity.Data>>(bean?.commodityOverseasList,type)
                c?.let {
                    for((index,item) in it.withIndex()){
                        list.add(SubjectBean.Commodity().apply {
                            shopcommonId = item.shopcommonId
                            sortNo = list.size+index
                            overseas = true
                        })
                    }
                }
            }
            if (list.isNotEmpty()){
                map["sortProductList"] = Gson().toJson(list)
            }

            if (!bean?.bookList.isNullOrEmpty()){
                val c = Gson().fromJson<ArrayList<CommodityListEntity.Data>>(bean?.bookList,type)
                c?.let {
                    val eleBookId = StringBuilder()
                    for(item in it){
                        if (eleBookId.toString().isEmpty()){
                            eleBookId.append(item.shopcommonId)
                        }else{
                            eleBookId.append(","+item.shopcommonId)
                        }
                    }
                    map["eleBookId"] = eleBookId.toString()
                }
            }

            return map

        }

        return null
    }

}