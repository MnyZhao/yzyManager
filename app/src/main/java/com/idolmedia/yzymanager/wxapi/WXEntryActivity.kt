package com.idolmedia.yzymanager.wxapi

import android.util.Log
import com.umeng.socialize.weixin.view.WXCallbackActivity
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 *  时间：2019/7/24-16:37
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzy.wxapi WXEntryActivity
 *  描述：
 */
class WXEntryActivity : WXCallbackActivity(){

    private fun bindWallet(code:String){
        val client = OkHttpClient()
            .newBuilder()
            .connectTimeout(60*10, TimeUnit.SECONDS)
            .readTimeout(60*10, TimeUnit.SECONDS) //读取超时
            .retryOnConnectionFailure(true)
            .build()

        val request = Request.Builder()
            .url("https://api.weixin.qq.com/sns/oauth2/access_token?" +
                    "appid=wxcb7ae60115b5261c&" +
                    "secret=47801142bcb8cdc1a7345ca8825dfd6d&" +
                    "code=$code&" +
                    "grant_type=authorization_code")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("====","onFailure")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("====", response.body!!.string())
            }

        })
    }

}