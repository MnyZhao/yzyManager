package com.idolmedia.yzymanager.utils

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import com.idolmedia.yzymanager.base.BaseApplication

/**
 *  时间：2019/4/17-17:47
 *  公司:北京爱豆文化传媒有限公司
 *  com.example.module_base.utils Dp2PxUtils
 *  描述：
 */
object Dp2PxUtils {


    fun dip2px(dp : Int) : Float{
        val density = BaseApplication.instance.resources.displayMetrics.density
        return (dp*density+0.5f)
    }

    fun dip2px(dp : Float) : Float{
        val density = BaseApplication.instance.resources.displayMetrics.density
        return (dp*density+0.5f)
    }

    fun px2dip(px : Int) : Int{
        val scale  = BaseApplication.instance.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    fun px2sp(px : Int) : Float{
        val fontScale  = BaseApplication.instance.resources.displayMetrics.scaledDensity
        return (px/fontScale+0.5f)
    }

    fun sp2px(sp : Int) : Float{
        val fontScale  = BaseApplication.instance.resources.displayMetrics.scaledDensity
        return (sp*fontScale+0.5f)
    }

    /**获取屏幕宽度*/
    fun getScreenWidth():Int{
        val wm = BaseApplication.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**获取屏幕宽度*/
    fun getScreenHeight():Int{
        val wm = BaseApplication.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    /**获取状态栏高度*/
    fun getStatusBarHeight(window: Window?, context: Context?):Int{
        val localRect = Rect()
        window?.decorView?.getWindowVisibleDisplayFrame(localRect)
        var mStatusBarHeight = localRect.top
        if (0 == mStatusBarHeight) {
            try {
                val localClass = Class.forName("com.android.internal.R\$dimen")
                val localObject = localClass.newInstance()
                val i5 =
                    localClass.getField("status_bar_height")[localObject].toString().toInt()
                mStatusBarHeight = context?.resources?.getDimensionPixelSize(i5) ?: 0
            } catch (var6: ClassNotFoundException) {
                var6.printStackTrace()
            } catch (var7: IllegalAccessException) {
                var7.printStackTrace()
            } catch (var8: InstantiationException) {
                var8.printStackTrace()
            } catch (var9: NumberFormatException) {
                var9.printStackTrace()
            } catch (var10: IllegalArgumentException) {
                var10.printStackTrace()
            } catch (var11: SecurityException) {
                var11.printStackTrace()
            } catch (var12: NoSuchFieldException) {
                var12.printStackTrace()
            }
        }
        if (0 == mStatusBarHeight) {
            val resourceId: Int =
                context?.resources?.getIdentifier("status_bar_height", "dimen", "android") ?: 0
            if (resourceId > 0) {
                mStatusBarHeight = context?.resources?.getDimensionPixelSize(resourceId) ?: 0
            }
        }
        return mStatusBarHeight
    }

    /**获取屏幕宽度*/
    fun getScreenHeightToWeb(more:Int):Int{
        val wm = BaseApplication.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return ((outMetrics.heightPixels-more).toFloat()/outMetrics.density).toInt()
    }

}