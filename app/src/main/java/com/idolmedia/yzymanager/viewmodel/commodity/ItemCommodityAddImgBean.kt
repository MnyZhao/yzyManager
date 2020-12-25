package com.idolmedia.yzymanager.viewmodel.commodity

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
import com.idolmedia.yzymanager.utils.ToastUtil
import com.luck.picture.lib.entity.LocalMedia

/**
 *  时间：2020/10/26-15:53
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity ItemCommodityAddImgBean
 *  描述：
 */
class ItemCommodityAddImgBean() : BaseBean() {

    constructor(requestCode:Int):this(){
        this.requestCode = requestCode
    }

    override fun getViewType() = R.layout.item_commodity_add_img_vice

    val path = ObservableField<String>()
    val canDelete = ObservableBoolean()
    var maxSelect = 1
    var imgVice = ArrayList<LocalMedia>()
    var imgRealPath = ""

    var requestCode = PhotoUtils.CHOOSE_REQUEST

    fun setImgPath(path:String?){

        if (!path.isNullOrEmpty()){
            RetrofitHelper.instance().uploadImage(path,object : BaseObserver<UploadImageEntity>(){
                override fun onSuccess(t: UploadImageEntity) {
                    if (t.resultCode==1){
                        imgRealPath = t.imageUrl
                        this@ItemCommodityAddImgBean.path.set(imgRealPath)
                        canDelete.set(true)
                    }
                }

                override fun onError(msg: String) {
                    ToastUtil.show(msg)
                    canDelete.set(false)
                }

            })
        }else{
            ToastUtil.show("未获得图片路径")
        }

    }

    fun onClickAdd(view:View){
        if (maxSelect==1){
            PhotoUtils.onCameraClick(view.context,true,maxSelect,requestCode)
        }else{
            PhotoUtils.onCameraMultiClick(view.context,maxSelect,imgVice,requestCode)
        }
    }

    fun onClickDelete(view:View){
        path.set(null)
        canDelete.set(false)
        imgRealPath = ""
        Glide.with(view.context).clear(view)
    }

}