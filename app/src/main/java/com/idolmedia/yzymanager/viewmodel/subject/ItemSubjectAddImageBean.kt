package com.idolmedia.yzymanager.viewmodel.subject

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.bumptech.glide.Glide
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.model.entity.UploadImageEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.PhotoUtils

/**
 *  时间：2020/11/19-15:19
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.subject ItemSubjectAddImageBean
 *  描述：
 */
class ItemSubjectAddImageBean(val model:SubjectAddViewModel?) : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_subject_add_img
    }

    val canDeleteMain = ObservableBoolean()
    val canDeleteHead = ObservableBoolean()
    val strMainImg = ObservableField<String>()
    val strHeadImg = ObservableField<String>()

    fun onClickDeleteMain(view:View){
        strMainImg.set(null)
        canDeleteMain.set(false)
        model?.bean?.previewUrl = ""
        Glide.with(view.context).clear(view)
    }

    fun onClickDeleteFace(view:View){
        strHeadImg.set(null)
        canDeleteHead.set(false)
        model?.bean?.detailsHeadImgUrl = ""
        Glide.with(view.context).clear(view)
    }

    fun onClickMain(view:View){
        if (canDeleteMain.get()){
            return
        }
        PhotoUtils.onCameraClick(view.context,1,2,3,ArrayList(),PhotoUtils.CHOOSE_REQUEST_FIRST)
    }

    fun onClickHead(view:View){
        if (canDeleteHead.get()){
            return
        }
        PhotoUtils.onCameraClick(view.context,1,3,2,ArrayList(),PhotoUtils.CHOOSE_REQUEST_SECOND)
    }

    fun upLoadImg(path:String, observer: BaseObserver<UploadImageEntity>){
        RetrofitHelper.instance().uploadImage(path,observer)
    }


}