package com.idolmedia.yzymanager.viewmodel.order

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.databinding.ItemPictureBinding
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView

/**
 *  时间：2020/12/2-11:27
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order ItemPictureBean
 *  描述：
 */
class ItemPictureBean(val path:String) : BaseBean() {
    override fun getViewType(): Int {
        return R.layout.item_picture
    }

    val strImg = ObservableField(path)

}