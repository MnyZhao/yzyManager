package com.idolmedia.yzymanager.model.net

import com.idolmedia.yzymanager.model.Api
import com.idolmedia.yzymanager.model.ApiConstant
import com.idolmedia.yzymanager.model.exception.NullOnEmptyConverterFactory
import com.idolmedia.yzymanager.model.exception.ToStringConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *  时间：2019/6/25-18:08
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.net RetrofitHttp
 *  描述：
 */
object RetrofitHttp {

    private val retrofit by lazy {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level= HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
            .connectTimeout(60, TimeUnit.SECONDS)//设置连接超时时间
            .addInterceptor(CommonInterceptor())//添加统一请求参数
            .addInterceptor(loggingInterceptor)//设置日志拦截器
            .hostnameVerifier { hostname, sslSession ->
                (hostname.contains(ApiConstant.BASEURL) || hostname.contains(ApiConstant.BASEURL_OVERSEAS)||
                        ApiConstant.BASEURL.contains(hostname) || ApiConstant.BASEURL_OVERSEAS.contains(hostname))
            }
            .build()

        return@lazy Retrofit.Builder()
            .baseUrl(ApiConstant.BASEURL)
            .client(okHttpClient)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(ToStringConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    }

    val api: Api by lazy {
        retrofit.create(Api::class.java)
    }

}