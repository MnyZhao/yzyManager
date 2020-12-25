package com.idolmedia.yzymanager.model.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.idolmedia.yzymanager.base.BaseApplication
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException

/**
 *  时间：2019/7/1-14:37
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.utils NetWorkUtil
 *  描述：
 */
object NetWorkUtil {

    /**是否有网络*/
    fun isNetConnected() : Boolean{
        val mConnectivityManager = BaseApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = mConnectivityManager.getNetworkCapabilities(mConnectivityManager.activeNetwork)
        if (capabilities==null){
            return false
        }else{
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                return true
            }
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                return true
            }
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                return true
            }
        }
        return false
    }


    /**
     * 获取ip地址
     * @return
     */
    fun getHostIP(): String? {

        var hostIp: String? = null
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            var ia: InetAddress? = null
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement() as NetworkInterface
                val ias = ni.inetAddresses
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement()
                    if (ia is Inet6Address) {
                        continue// skip ipv6
                    }
                    val ip = ia!!.hostAddress
                    if ("127.0.0.1" != ip) {
                        hostIp = ia.hostAddress
                        break
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

        return hostIp

    }

    /**是否需要调用国内域名接口*/
    fun isDomesticUrl(baseUrl:String):Boolean{
        return  baseUrl.contains("/account/") ||
                baseUrl.contains("/message/") ||
                baseUrl.contains("/login") ||
                baseUrl.contains("/time/current")  ||
                baseUrl.contains("/pwd/update") ||
                baseUrl.contains("/mainAcc/sendVCode") ||
                baseUrl.contains("/mainAcc/update") ||
                baseUrl.contains("/subject/list")||
                baseUrl.contains("/sort/list")||
                baseUrl.contains("/sort/move")||
                baseUrl.contains("/sort/currSort")||
                baseUrl.contains("/product/savePrepose") ||
                baseUrl.contains("/statistics/messageInfo")||
                baseUrl.contains("/sort/onStick")
    }

}