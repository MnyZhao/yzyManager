package com.idolmedia.yzymanager.viewmodel.commodity

import android.content.Context
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.databinding.ItemCommodityAddImgBinding
import com.idolmedia.yzymanager.interfaces.OnItemSwitchListener
import com.idolmedia.yzymanager.model.entity.AdditionalEntity
import com.idolmedia.yzymanager.model.entity.UploadImageEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.PhotoUtils
import com.idolmedia.yzymanager.utils.ToastUtil

/**
 *  时间：2020/10/26-15:21
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity ItemCommodityAddBean
 *  描述：
 */
class ItemCommodityAddBean() : BaseBean() {

    constructor(title:String):this(){
        layoutId = R.layout.item_commodity_add_title
        strTitle.set(title)
    }

    constructor(title:String,content:String?):this(){
        layoutId = R.layout.item_commodity_add_content
        strTitle.set(title)
        strContent.set(content)
    }

    constructor(title:String,content:String?,hint:String):this(){
        layoutId = R.layout.item_commodity_add_edit
        strTitle.set(title)
        strContent.set(content)
        strHint.set(hint)
    }

    constructor(title:String,content:String?,hint:String,tip:String):this(){
        layoutId = R.layout.item_commodity_add_edit
        strTitle.set(title)
        strContent.set(content)
        strHint.set(hint)
        strTip.set(tip)
    }

    constructor(additional:AdditionalEntity.Additional2):this(){
        this.additional = additional
        layoutId = R.layout.item_commodity_add_edit_right
        strTitle.set(additional.field)
        strContent.set(additional.addition_value)
        strHint.set(additional.placehold)
    }

    constructor(title:String,content:String?,hint:String,tip:String,inputType:Int):this(){
        layoutId = R.layout.item_commodity_add_edit
        strTitle.set(title)
        strContent.set(content)
        strHint.set(hint)
        strTip.set(tip)
        this.inputType.set(inputType)
    }

    constructor(title:String,open:Boolean):this(){
        layoutId = R.layout.item_commodity_add_switch
        strTitle.set(title)
        isOpen.set(open)
    }

    constructor(title:String,open:Boolean,itemSwitchListener: OnItemSwitchListener):this(){
        layoutId = R.layout.item_commodity_add_switch
        strTitle.set(title)
        isOpen.set(open)
        this.itemSwitchListener = itemSwitchListener
    }

    constructor(title:String,tip:String,isHaveAppendImg:Boolean):this(){
        isImg = true
        layoutId = if (isHaveAppendImg){

            viceFirst.set( ItemCommodityAddImgBean(PhotoUtils.CHOOSE_REQUEST_FIRST))
            viceSecond.set(ItemCommodityAddImgBean(PhotoUtils.CHOOSE_REQUEST_SECOND))
            viceThird.set( ItemCommodityAddImgBean(PhotoUtils.CHOOSE_REQUEST_THIRD))
            viceFourth.set(ItemCommodityAddImgBean(PhotoUtils.CHOOSE_REQUEST_FOURTH))

            R.layout.item_commodity_add_img
        }else{
            R.layout.item_commodity_add_img_no_append
        }
        strTitle.set(title)
        strImgTip.set(tip)
    }

    constructor(title:String,tip:String,img:String,isHaveAppendImg:Boolean):this(title, tip, isHaveAppendImg){
        if (img.isNotEmpty()){
            strImg.set(img)
        }
    }

    var layoutId = -1
    override fun getViewType() = layoutId

    val strTitle = ObservableField<String>()
    val strContent = ObservableField<String>()
    val strHint = ObservableField<String>()
    val strTip = ObservableField<String>()
    val inputType = ObservableInt()
    val isOpen = ObservableBoolean()
    val adapter = ObservableField<BaseAdapter>()
    val strImg = ObservableField<String>()
    val strImgTip = ObservableField<String>()
    val canDelete = ObservableBoolean()
    val visibleMoreButton = ObservableInt(View.VISIBLE)
    var viceFirst  = ObservableField<ItemCommodityAddImgBean>()
    var viceSecond = ObservableField<ItemCommodityAddImgBean>()
    var viceThird  = ObservableField<ItemCommodityAddImgBean>()
    var viceFourth = ObservableField<ItemCommodityAddImgBean>()
    var isImg = false
    var imgRealPath = ""
    var itemSwitchListener:OnItemSwitchListener? = null
    var viewModel:CommodityAddViewModel? = null
    var mainImgBg = ObservableInt(R.mipmap.ic_img_add_admin)

    /**能否编辑*/
    var canEdit = true

    var additional:AdditionalEntity.Additional2? = null
    var cropW = 1
    var cropH = 1


    fun onClickAddImg(view:View){
        if (canDelete.get()){
            return
        }
        PhotoUtils.onCameraClick(view.context,1,cropW,cropH,ArrayList())
    }

    fun onClickDelete(view:View){
        strImg.set(null)
        canDelete.set(false)
        imgRealPath = ""
        Glide.with(view.context).clear(view)
        viewModel?.commodityPublishBean?.imageUrl = ""
    }

    fun onClickSwitch(view:View){
        if (canEdit){
            isOpen.set(!isOpen.get())
            itemSwitchListener?.onItemSwitch(position,this,isOpen.get())
        }
    }

    fun upLoadImg(path:String){

        RetrofitHelper.instance().uploadImage(path,object : BaseObserver<UploadImageEntity>(){
            override fun onSuccess(t: UploadImageEntity) {
                if (t.resultCode==1){
                    imgRealPath = t.imageUrl
                    canDelete.set(true)
                    strImg.set(imgRealPath)
                    viceFirst.get()?.path?.set(imgRealPath)
                    viceFirst.get()?.imgRealPath = imgRealPath
                    viceFirst.get()?.canDelete?.set(true)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    fun upLoadImg(path:String,viewModel:CommodityAddViewModel?,observer:BaseObserver<UploadImageEntity>){
        this.viewModel = viewModel
        RetrofitHelper.instance().uploadImage(path,observer)
    }

    var imgView:View? = null
    override fun onBindViewModel(context: Context, binding: ViewDataBinding?, position: Int) {
        super.onBindViewModel(context, binding, position)
        if (binding is ItemCommodityAddImgBinding){
            imgView = binding.ivImg
        }
    }

}