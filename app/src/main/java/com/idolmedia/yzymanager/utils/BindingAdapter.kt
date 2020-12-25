package com.idolmedia.yzymanager.utils

import androidx.databinding.BindingAdapter
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.utils.glide.GlideUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 *  时间：2019/6/26-16:12
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.bean BindingAdapter
 *  描述：
 */
object BindingAdapter {

    /**防多点击操作*/
    @JvmStatic
    @BindingAdapter("android:onClick")
    fun setOnClick(view: View,onClickListener: View.OnClickListener?){
        val mHits = LongArray(2)
        view.setOnClickListener {
            mHits[1] = System.currentTimeMillis()
            if (mHits[0]<(System.currentTimeMillis()-500)){
                mHits[0] = mHits[1]
                onClickListener?.onClick(view)
            }
        }
    }

    /**防多点击操作*/
    @JvmStatic
    @BindingAdapter("android:onClick","intervalTime")
    fun setOnClick(view: View,onClickListener: View.OnClickListener?,interval:Int){
        val mHits = LongArray(2)
        view.setOnClickListener {
            mHits[1] = System.currentTimeMillis()
            if (mHits[0]<(System.currentTimeMillis()-interval)){
                mHits[0] = mHits[1]
                onClickListener?.onClick(view)
            }
        }
    }


    /**防多点击操作
     *
     * @param isFast 是否可以快速点击
     * */
    @JvmStatic
    @BindingAdapter("android:onClick","isFast")
    fun setOnClick(view: View,onClickListener: View.OnClickListener?,isFast:Boolean){
        val mHits = LongArray(2)
        if (isFast){
            view.setOnClickListener {
                onClickListener?.onClick(view)
            }
        }else{
            view.setOnClickListener {
                mHits[1] = System.currentTimeMillis()
                if (mHits[0]<(System.currentTimeMillis()-500)){
                    mHits[0] = mHits[1]
                    onClickListener?.onClick(view)
                }
            }
        }
    }

    /**
     * 设置背景颜色
     */
    @JvmStatic
    @BindingAdapter("backgroundColor")
    fun backgroundColor(view:View,colorId:Int){
        if (colorId>0){
            view.setBackgroundColor(ContextCompat.getColor(view.context,colorId))
        }
    }

    /**
     * 设置背景颜色
     */
    @JvmStatic
    @BindingAdapter("backgroundDrawable")
    fun backgroundDrawable(view:View,drawableId:Int){
        if (drawableId>0){
            view.background = ContextCompat.getDrawable(view.context,drawableId)
        }
    }

    /**
     * 设置背景颜色
     */
    @JvmStatic
    @BindingAdapter("srcDrawable")
    fun srcDrawable(view:ImageView,drawableId:Int){
        if (drawableId>0){
            view.setImageDrawable(ContextCompat.getDrawable(view.context,drawableId))
        }
    }

    /**
     * 设置加载的图片
     */
    @JvmStatic
    @BindingAdapter("imageResource")
    fun imageResource(view: ImageView, resId:Int){
        if (resId>0){
            view.visibility = View.VISIBLE
            view.setImageResource(resId)
        }else{
            view.visibility = View.GONE
        }
    }

    /**
     * 控制view的隐藏显示
     */
    @JvmStatic
    @BindingAdapter("visible")
    fun visible(view : View, visible:Int){
        view.visibility = visible
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun setRecyclerViewAdapter(view : RecyclerView, adapter: BaseAdapter?){
        if (adapter!=null){
            view.adapter = adapter
        }
    }

    @JvmStatic
    @BindingAdapter("finishSmart")
    fun finishSmart(layout: SmartRefreshLayout, type:Int){
        if (type>0){
            layout.finishRefresh()
            layout.finishLoadmore()
        }
    }

    /**
     * 控制RecyclerView滑动到指定位置
     */
    @JvmStatic
    @BindingAdapter("isEnableLoadMore")
    fun isEnableLoadMore(smartLayout: SmartRefreshLayout,isLoadMore:Boolean){
        smartLayout.isEnableLoadmore = isLoadMore
    }

    /**加载模糊图片*/
    @JvmStatic
    @BindingAdapter("glideBlur")
    fun glideBlur(image:ImageView,headPath:String?){
        GlideUtil.getInstance().glideBlur(headPath,image)
    }

    /**加载圆形头像*/
    @JvmStatic
    @BindingAdapter("glideHead")
    fun glideHead(image:ImageView,headPath:String?){
        GlideUtil.getInstance().withCircle(image.context,headPath,image)
    }

    /**加载圆形头像*/
    @JvmStatic
    @BindingAdapter("glideHead")
    fun glideHead(image:ImageView,headPath:Int){
        if (headPath>0){
            GlideUtil.getInstance().withCircle(image.context,headPath,image)
        }
    }

    /**加载普通图片*/
    @JvmStatic
    @BindingAdapter("glideImg")
    fun glideImg(image:ImageView,headPath:String?){
        if (!headPath.isNullOrEmpty()){
            GlideUtil.getInstance().with(image.context,headPath,image)
        }
    }

    /**加载普通图片*/
    @JvmStatic
    @BindingAdapter("glideImg")
    fun glideImg(image:ImageView,imgResource:Int){
        if(imgResource>0){
            image.setImageResource(imgResource)
        }
    }

    /**
     * 加载上边俩个角是圆角的图片
     * 默认正方形的图
     */
    @JvmStatic
    @BindingAdapter("glideTop")
    fun glideTop(image:ImageView,headPath:String?){
        GlideUtil.getInstance().withRoundedCorners(image.context,headPath,image)
    }

    /**
     * 加载上边俩个角是圆角的图片
     * 默认正方形的图
     */
    @JvmStatic
    @BindingAdapter("glideTop","glideTopType")
    fun glideTop(image:ImageView,headPath:String?,type:Int){
        GlideUtil.getInstance().withRoundedCorners(image.context,headPath,image,type)
    }

    /**
     * 加载圆角的图片
     * 默认正方形的图
     */
    @JvmStatic
    @BindingAdapter("glideAll")
    fun glideAll(image:ImageView,headPath:String?){
        GlideUtil.getInstance().withRoundedCornersAll(image.context,headPath,image)
    }

    /**
     * 加载圆角的图片
     * 默认正方形的图
     */
    @JvmStatic
    @BindingAdapter("glidePhoto")
    fun glidePhoto(image:ImageView,headPath:String?){
        headPath?.let {
            GlideUtil.getInstance().withRoundedCornersAll(image.context,headPath,image)
        }
    }

    /**
     * 加载圆角的图片
     * 默认正方形的图 0正方形 1高长方形 2宽长方形
     */
    @JvmStatic
    @BindingAdapter("glideAllImage","glideAllType")
    fun glideAllImage(image:ImageView,headPath:String?,type:Int){
        GlideUtil.getInstance().withRoundedCornersAll(image.context,headPath,image,type)
    }


    /**
     * 加载圆角的图片
     * 默认正方形的图 0正方形 1高长方形 2宽长方形
     */
    @JvmStatic
    @BindingAdapter("inputType")
    fun editInputType(editText : EditText,inputType:Int){
        if (inputType>0){
            editText.inputType = inputType
        }
    }

    @JvmStatic
    @BindingAdapter("setHeight")
    fun setViewHeight(view : View,height:Int){
        view.post {
            if (height>0){
                val lp = view.layoutParams
                lp.height = height
                view.layoutParams = lp
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setWidth")
    fun setViewWidth(view : View,width:Int){
        view.post {
            if (width>0){
                val lp = view.layoutParams
                lp.width = width
                view.layoutParams = lp
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setBackgroundImage")
    fun setBackgroundImage(view : View,background:Int){
        view.background = ContextCompat.getDrawable(view.context,background)
    }

}