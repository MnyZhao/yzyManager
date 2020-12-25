package com.idolmedia.yzymanager.utils.glide

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import androidx.fragment.app.Fragment
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.idolmedia.yzymanager.R

/**
 *  时间：2019/5/10-17:54
 *  公司:北京爱豆文化传媒有限公司
 *  com.example.module_base.utils GlideUtil
 *  描述：
 */
class GlideUtil {

    private val oldEmptyHeadImg = "http://otkny7iog.bkt.clouddn.com//product/main/201708/cae967e159f64af484b4d929c88126b7.png"

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var glideUtil:GlideUtil ?= null

        fun getInstance():GlideUtil{
            if (glideUtil==null){
                glideUtil = GlideUtil()
            }
            return glideUtil!!
        }
    }

    /**普通加载图片*/
    fun withPlaceholder(context: Context,url:String,view:ImageView){
        withPlaceholder(context, url, view, 0)
    }

    /**普通加载图片*/
    fun withPlaceholder(context: Context,url:String,view:ImageView,type: Int){
        val placeholder = getPlaceholder(type)
        if (url.isEmpty()||"null" == url||url == oldEmptyHeadImg){
            view.setImageResource(placeholder)
            return
        }

        glide(context,url,view,placeholder)
    }

    /**
     * 普通加载图片
     * 无占位图
     */
    fun with(context: Context,url:String?,view:ImageView){
        with(context, url, view, 0)
    }

    /**
     * 普通加载图片
     * 无占位图
     */
    fun with(context: Context,url:Int,view:ImageView){
        with(context, url, view, 0)
    }


    /**
     * 普通加载图片
     * 无占位图
     */
    fun with(context: Context,url:String?,view:ImageView,type: Int){
        val placeholder = getPlaceholder(type)

        if (url==null||url.isEmpty()||"null" == url ||url == oldEmptyHeadImg){
            view.setImageResource(placeholder)
            return
        }

        if (context is Activity && context.isDestroyed){
            return
        }

        if (context is Fragment && context.activity == null){
            return
        }

        Glide
            .with(context)
            .load(url)
            .transition(DrawableTransitionOptions().crossFade(1000))
            .into(view)
    }

    /**
     * 普通加载图片
     * 无占位图
     */
    fun with(context: Context,url:Int,view:ImageView,type: Int){
        val placeholder = getPlaceholder(type)
        if (url<=0){
            view.setImageResource(placeholder)
            return
        }

        if (context is Activity && context.isDestroyed){
            return
        }

        if (context is Fragment && context.activity == null){
            return
        }

        Glide
            .with(context)
            .load(url)
            .into(view)
    }

    /**加载圆图片*/
    fun withCircle(context: Context,url:String?,view:ImageView){

        if (context is Activity && context.isDestroyed){
            return
        }

        if (context is Fragment && context.activity == null){
            return
        }

        if (url==null||url.isEmpty()||"null" == url ||url == oldEmptyHeadImg){
            Glide
                .with(context)
                .load(R.mipmap.ic_yzy_placeholder_round)
                .apply(RequestOptions().circleCrop())
                .into(view)
            return
        }

        Glide
            .with(context)
            .load(url)
            .placeholder(R.mipmap.ic_yzy_placeholder_round)
            .error(R.mipmap.ic_yzy_placeholder_round)
            .apply(RequestOptions().circleCrop())
            .into(view)
    }

    /**加载圆图片*/
    fun withCircle(context: Context,url:Int,view:ImageView){

        if (context is Activity && context.isDestroyed){
            return
        }

        if (context is Fragment && context.activity == null){
            return
        }

        if (url<=0){
            Glide
                .with(context)
                .load(R.mipmap.ic_yzy_placeholder_round)
                .apply(RequestOptions().circleCrop())
                .into(view)
            return
        }

        Glide
            .with(context)
            .load(url)
            .placeholder(R.mipmap.ic_yzy_placeholder_round)
            .error(R.mipmap.ic_yzy_placeholder_round)
            .apply(RequestOptions().circleCrop())
            .into(view)
    }

    /**
     * 圆角-top
     * @param context context
     * @param url 图片路径String
     * @param view ImageView
     * 默认正方形占位图
     */
    fun withRoundedCorners(context: Context,url:String?,view:ImageView){
        withRoundedCorners(context, url, view, 0)
    }

    /**
     * 圆角-top
     * @param context context
     * @param url 图片路径String
     * @param view ImageView
     * @param type 0 正方形 1竖长方形 2横长方形
     */
    fun withRoundedCorners(context: Context,url:String?,view:ImageView,type:Int){

        val placeholder = getPlaceholder(type)

        val transform = CenterCropTransform(5f,CenterCropTransform.CornerType.TOP)

        if (url==null||url.isEmpty()||"null" == url||url == oldEmptyHeadImg){
            glideAdBitmap(context,placeholder,view,placeholder,transform)
        }else{
            glideAdBitmap(context,url,view,placeholder,transform)
        }

    }


    /**
     * 圆角-all
     * @param context context
     * @param url 图片路径String
     * @param view ImageView
     * 默认正方形
     */
    fun withRoundedCornersAll(context: Context,url:String?,view:ImageView){
       withRoundedCornersAll(context, url, view,0)
    }

    /**
     * 圆角-all
     * @param context context
     * @param url 图片路径String
     * @param view ImageView
     * @param type 0正方形 1高长方形 2宽长方形
     */
    fun withRoundedCornersAll(context: Context,url:String?,view:ImageView,type:Int){
        withRoundedCornersAll(context, url, view, 5f, type)
    }

    /**
     * 圆角-all
     * @param context context
     * @param url 本地图片路径Int
     * @param view ImageView
     * 默认正方形
     */
    fun withRoundedCornersAll(context: Context,url:Int,view:ImageView){
        withRoundedCornersAll(context, url, view, 0)
    }

    /**圆角-all*/
    fun withRoundedCornersAll(context: Context,url:Int,view:ImageView,type:Int){

        val placeholder = getPlaceholder(type)

        val transform = CenterCropTransform(5f,CenterCropTransform.CornerType.ALL)

        glideAdBitmap(context,url,view,placeholder,transform)
    }

    /**圆角-all
     * @param round 圆角半径
     * */
    fun withRoundedCornersAll(context: Context,url:String,view:ImageView,round:Float){
        withRoundedCornersAll(context, url, view, round, 0)
    }

    /**
     * 圆角-all
     * @param context context
     * @param url 图片路径String
     * @param view ImageView
     * @param round 圆角半径
     * @param type 0正方形 1高长方形 2宽长方形
     */
    fun withRoundedCornersAll(context: Context,url:String?,view:ImageView,round:Float,type: Int){

        val placeholder = getPlaceholder(type)

        val transform = CenterCropTransform(round,CenterCropTransform.CornerType.ALL)

        if (url==null||url.isEmpty()||"null" == url||url == oldEmptyHeadImg){
            glideAdBitmap(context,placeholder,view,placeholder,transform)
        }else{
            glideAdBitmap(context,url,view,placeholder,transform)
        }

    }

    /**
     * 圆角-all
     * @param context context
     * @param url 图片路径Int
     * @param view ImageView
     * @param round 圆角半径
     * @param type 0正方形 1高长方形 2宽长方形
     */
    fun withRoundedCornersAll(context: Context,url:Int,view:ImageView,round:Float,type: Int){

        val placeholder = getPlaceholder(type)

        if (url<=0){
            view.setImageResource(placeholder)
            return
        }
        val transform = CenterCropTransform(round,CenterCropTransform.CornerType.ALL)

        glideAdBitmap(context,url,view,placeholder,transform)

    }

    /**自定义圆角本地图片*/
    fun glideTransform(context: Context,url:Int,view:ImageView,cornerType: CenterCropTransform.CornerType){

        if (context is Activity && context.isDestroyed){
            return
        }

        if (context is Fragment && context.activity == null){
            return
        }

        Glide
            .with(context)
            .load(url)
            .transform(CenterCropTransform(5f,cornerType))
            .skipMemoryCache(true) //不使用内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE) //只在磁盘缓存 加载的尺寸
            .into(view)
    }

    private fun glide(context: Context,url:String,view:ImageView,placeholder:Int){

        if (context is Activity && context.isDestroyed){
            return
        }

        if (context is Fragment && context.activity == null){
            return
        }

        Glide
            .with(context)
            .load(url)
            .placeholder(placeholder)
            .error(placeholder)
            .skipMemoryCache(true) //不使用内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE) //只在磁盘缓存 加载的尺寸
            .into(view)
    }

    private fun glideAdBitmap(context: Context,url:String,view:ImageView,placeholder:Int,transform: Transformation<Bitmap>){

        if (context is Activity && context.isDestroyed){
            return
        }

        if (context is Fragment && context.activity == null){
            return
        }

        Glide
            .with(context)
            .asDrawable()
            .load(url)
            .placeholder(placeholder)
            .error(placeholder)
            .transform(transform)
            .skipMemoryCache(true) //不使用内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE) //只在磁盘缓存 加载的尺寸
            .into(view)
    }

    private fun glideAdBitmap(context: Context,url:Int,view:ImageView,placeholder:Int,transform: Transformation<Bitmap>){

        if (context is Activity && context.isDestroyed){
            return
        }

        if (context is Fragment && context.activity == null){
            return
        }

        Glide
            .with(context)
            .asDrawable()
            .load(url)
            .placeholder(placeholder)
            .error(placeholder)
            .transform(transform)
            .skipMemoryCache(true) //不使用内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE) //只在磁盘缓存 加载的尺寸
            .into(view)
    }

    fun glideBlur(url:String?,view:ImageView){
        if (url==null||url.isEmpty()){
            return
        }
        Glide
            .with(view)
            .asDrawable()
            .load(url)
            .transform(jp.wasabeef.glide.transformations.BlurTransformation())
            .skipMemoryCache(true) //不使用内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE) //只在磁盘缓存 加载的尺寸
            .into(view)
    }

    private fun getPlaceholder(type: Int):Int{
        return when(type){
            0 ->{
                R.mipmap.ic_yzy_placeholder
            }
            1 ->{
                R.mipmap.ic_yzy_placeholder_height
            }
            else ->{
                R.mipmap.ic_yzy_placeholder_width
            }
        }
    }

}