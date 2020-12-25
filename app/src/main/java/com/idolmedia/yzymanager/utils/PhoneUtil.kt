package com.idolmedia.yzymanager.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.Log
import android.view.WindowManager
import com.idolmedia.yzymanager.base.BaseApplication

/**
 *  时间：2019/4/22-17:39
 *  公司:北京爱豆文化传媒有限公司
 *  com.example.module_base.utils PhoneUtil
 *  描述：
 */
object PhoneUtil{

    fun getAppVersionName():String{

        var versionName = ""
        try {
            val pm = BaseApplication.instance.packageManager
            val pi = pm.getPackageInfo(BaseApplication.instance.packageName, 0)
            versionName = pi.versionName
            var versioncode = pi.versionCode
            if (versionName == null || versionName.isEmpty()) {
                return ""
            }
        } catch (e: Exception) {
            return ""
        }

        return versionName
    }

    /**是否是全面屏*/
    fun isNotchScreen(activity:Activity):Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.d("====","androidP")
            return huaWeiScreen(activity)|| xiaoMiScreen(activity)|| oppoScreen(activity)|| vivoScreen(activity)|| commonScreen(activity)
        }
        return false
    }

    private fun huaWeiScreen(context:Context):Boolean{
        var ret = false
        try {
            val cl = context.classLoader
            val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("hasNotchInScreen")
            ret = get.invoke(HwNotchSizeUtil) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            Log.d("====", "华为 $ret")
            return ret
        }
    }

    private fun oppoScreen(context:Context):Boolean{
        return context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    private fun vivoScreen(context:Context):Boolean{
        var ret = false
        try {
            val cl = context.classLoader
            val ftFeature = cl.loadClass("com.util.FtFeature")
            val get = ftFeature.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            ret = get.invoke(ftFeature, 0x00000020) as Boolean
        } catch (e: Exception) {

        } finally {
            Log.d("====", "vivo $ret")
            return ret
        }
    }

    private fun xiaoMiScreen(context:Context):Boolean{
        var isNotch = false
        try {
            val  cl = context.classLoader
            val  cls = cl.loadClass("android.os.SystemProperties");
            val  method = cls.getMethod("getInt", String::class.java, Int::class.java)
            isNotch = method.invoke(null, "ro.miui.notch", 0) == 1
        } catch (e: Exception) {

        } finally {
            Log.d("====", "xiaomi $isNotch")
            return isNotch
        }
    }

    var mHasCheckAllScreen = false
    var mIsAllScreenDevice = false
    @SuppressLint("ObsoleteSdkInt")
    private fun commonScreen(context:Context):Boolean{

        if(mHasCheckAllScreen){
            return mIsAllScreenDevice
        }

        mHasCheckAllScreen = true
        mIsAllScreenDevice = false

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // 低于 API 21的，都不会是全面屏。。
            return false
        }

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE)
        windowManager?.let {
            it as WindowManager
            val display = it.defaultDisplay
            val point = Point()
            display.getRealSize(point)
            val width: Float
            val height: Float
            if (point.x < point.y) {
                width = point.x.toFloat()
                height = point.y.toFloat()
            } else {
                width = point.y.toFloat()
                height = point.x.toFloat()
            }
            if (height / width >= 1.97f) {
                mIsAllScreenDevice = true
            }
        }

        Log.d("====", "common $mIsAllScreenDevice")
        return mIsAllScreenDevice

    }

}