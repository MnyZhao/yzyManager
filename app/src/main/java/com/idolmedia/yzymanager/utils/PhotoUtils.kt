package com.idolmedia.yzymanager.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import com.idolmedia.yzymanager.utils.glide.GlideEngine
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.SdkVersionUtils

/**
 *  时间：2020/10/28-15:42
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.utils PhotoUtils
 *  描述：
 */
object PhotoUtils {

    const val CHOOSE_REQUEST = 1001
    const val CHOOSE_REQUEST_FIRST  = 10001
    const val CHOOSE_REQUEST_SECOND = 10002
    const val CHOOSE_REQUEST_THIRD  = 10003
    const val CHOOSE_REQUEST_FOURTH = 10004
    const val CHOOSE_REQUEST_MULTI  = 1002
    const val CHOOSE_REQUEST_VIDEO  = 1003

    /**返回是否是图片选择的code*/
    fun verifyImgRequestIsCode(code:Int):Boolean{
        return code == CHOOSE_REQUEST ||
                code == CHOOSE_REQUEST_FIRST ||
                code == CHOOSE_REQUEST_SECOND ||
                code == CHOOSE_REQUEST_THIRD ||
                code == CHOOSE_REQUEST_FOURTH ||
                code == CHOOSE_REQUEST_MULTI
    }


    /**
     * 单选 不开启裁剪  默认requestCode 不限制图片大小与比例
     */
    fun onCameraClick(context: Context){
        onCameraClick(context, 1, ArrayList(), CHOOSE_REQUEST)
    }

    /**
     * @param maxSelect 最大选择数量
     * @param selectList 已选择的图片
     * 不开启裁剪  默认requestCode 不限制图片大小与比例
     */
    fun onCameraClick(context: Context, maxSelect: Int, selectList: ArrayList<LocalMedia>){
        onCameraClick(context, maxSelect, 0, 0, false, 1, 1, selectList, CHOOSE_REQUEST)
    }

    /**
     * @param maxSelect 最大选择数量
     * @param selectList 已选择的图片
     * @param cropWidth 裁减宽高比例 宽
     * @param cropHeight 裁减宽高比例 高
     * 开启裁剪  默认requestCode 不限制图片大小与比例
     */
    fun onCameraClick(context: Context, maxSelect: Int, cropWidth: Int, cropHeight: Int, selectList: ArrayList<LocalMedia>){
        onCameraClick(context, maxSelect, cropWidth, cropHeight, selectList, CHOOSE_REQUEST)
    }

    /**
     * @param maxSelect 最大选择数量
     * @param selectList 已选择的图片
     * @param cropWidth 裁减宽高比例 宽
     * @param cropHeight 裁减宽高比例 高
     * 开启裁剪  不限制图片大小与比例
     */
    fun onCameraClick(context: Context, maxSelect: Int, cropWidth: Int, cropHeight: Int, selectList: ArrayList<LocalMedia>,result:Int){
        onCameraClick(context, maxSelect, 0, 0, true, cropWidth, cropHeight, selectList, result)
    }

    /**
     * @param maxSelect 最大选择数量
     * @param selectList 已选择的图片
     * @param requestCode 自定义requestCode
     * 不开启裁剪  不限制图片大小与比例
     */
    fun onCameraClick(context: Context, maxSelect: Int, selectList: ArrayList<LocalMedia>, requestCode: Int){
        onCameraClick(context, maxSelect, 0, 0, false, 1, 1, selectList, requestCode)
    }

    /**
     * @param isEnableCrop 是否开启裁剪  比例1:1
     * @param maxSelect 最大选择数量
     * @param requestCode 自定义requestCode
     * 不显示已选择
     */
    fun onCameraClick(context: Context,isEnableCrop: Boolean, maxSelect: Int, requestCode: Int){
        onCameraClick(context, maxSelect, 0, 0, isEnableCrop, 1, 1, ArrayList(), requestCode)
    }

    /**
     * @param isEnableCrop 是否开启裁剪  比例1:1
     * @param maxSelect 最大选择数量
     * 不显示已选择
     * requestCode：CHOOSE_REQUEST
     */
    fun onCameraClick(context: Context,isEnableCrop: Boolean, maxSelect: Int){
        onCameraClick(context, isEnableCrop, maxSelect, CHOOSE_REQUEST)
    }

    /**
     * 多选 不裁剪 默认requestCode:CHOOSE_REQUEST_MULTI
     */
    fun onCameraMultiClick(context: Context, maxSelect: Int, selectList: ArrayList<LocalMedia>){
        onCameraMultiClick(context, maxSelect, selectList, CHOOSE_REQUEST_MULTI)
    }

    /**
     * 多选 不裁剪 自定义requestCode
     */
    fun onCameraMultiClick(context: Context, maxSelect: Int, selectList: ArrayList<LocalMedia>,requestCode:Int){
        onCameraClick(context, maxSelect, 0, 0, false, 1, 1, selectList, requestCode)
    }

    fun onCameraClick(context: Context, maxSelect: Int, maxFileSize: Int, maxImageScale: Int,
                      isEnableCrop: Boolean, cropWidth: Int, cropHeight: Int,
                      selectList: ArrayList<LocalMedia>, requestCode: Int){

        PictureSelector.create(context as Activity)
            .openGallery(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
            .maxFileSize(maxFileSize)
            .maxImageScale(maxImageScale)
            .maxSelectNum(maxSelect) // 最大图片选择数量
            .minSelectNum(1) // 最小选择数量
            .imageSpanCount(4) // 每行显示个数
            .isPreviewImage(true)// 是否可预览图片
            .isCamera(true)// 是否显示拍照按钮
            .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
            .isEnableCrop(isEnableCrop) // 是否裁剪
            .isCompress(false)// 是否压缩
            .synOrAsy(false) //同步true或异步false 压缩 默认同步
            .withAspectRatio(cropWidth, cropHeight) // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            .freeStyleCropEnabled(false) // 裁剪框是否可拖拽
            .circleDimmedLayer(false) // 是否圆形裁剪
            .showCropFrame(true) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
            .showCropGrid(true) // 是否显示裁剪矩形网格 圆形裁剪时建议设为false
            .isOpenClickSound(false) // 是否开启点击声音
            .isReturnEmpty(false) // 未选择数据时点击按钮是否可以返回
            .closeAndroidQChangeWH(true) //如果图片有旋转角度则对换宽高,默认为true
            .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q()) // 如果视频有旋转角度则对换宽高,默认为false
            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) // 设置相册Activity方向，不设置默认使用系统
            .selectionMode(PictureConfig.MULTIPLE) // 多选 or 单选
            .imageFormat(if (SdkVersionUtils.checkedAndroid_Q() ) PictureMimeType.PNG_Q else PictureMimeType.PNG) // 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
            .selectionData(selectList)
            .forResult(requestCode)

    }

}