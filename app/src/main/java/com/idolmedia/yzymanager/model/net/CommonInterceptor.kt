package com.idolmedia.yzymanager.model.net

import com.idolmedia.yzymanager.model.ApiConstant
import com.idolmedia.yzymanager.utils.Md5Security
import com.idolmedia.yzymanager.utils.PhoneUtil
import com.idolmedia.yzymanager.utils.SpManager
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Response
import java.net.URLDecoder
import java.util.*
import kotlin.collections.HashMap

/**
 *  时间：2019/4/25-14:11
 *  公司:北京爱豆文化传媒有限公司
 *  com.example.module_base.api CommonInterceptor
 *  描述：
 */
class CommonInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()

        /**先区分本地系统环境*/
        var isOverseas = SpManager.isOverseasSystem()

        val path = oldRequest.url.toUrl().path
        var url = if (NetWorkUtil.isDomesticUrl(path ?: "")){
            //这些接口只有国内域名
            isOverseas = false
            "${ApiConstant.BASEURL}$path"
        }else{
            if (isOverseas){
                "${ApiConstant.BASEURL_OVERSEAS}$path"
            }else{
                "${ApiConstant.BASEURL}$path"
            }
        }

        if (oldRequest.method == "POST"){
            if (oldRequest.body is FormBody){
                val bodyBuilder = FormBody.Builder()
                bodyBuilder.addEncoded("app","android")
                bodyBuilder.addEncoded("version", PhoneUtil.getAppVersionName())
                var body = oldRequest.body as FormBody

                for(i in 0 until body.size){
                    if (body.encodedName(i) == "isOverseas"){
                        //若接口字段包含isOverseas 需要根据接口选择域名
                        isOverseas = body.encodedValue(i) == "true"
                    }else{
                        bodyBuilder.addEncoded(body.encodedName(i),body.encodedValue(i))
                    }
                }

                url = if (isOverseas){
                    "${ApiConstant.BASEURL_OVERSEAS}$path"
                }else{
                    "${ApiConstant.BASEURL}$path"
                }

                bodyBuilder.addEncoded("overseas","$isOverseas")

                val user = SpManager.getUserEntity()

                body = if (user==null){
                    bodyBuilder.build()
                } else{
                    if (oldRequest.url.encodedPath.contains("account/login")||
                        oldRequest.url.encodedPath.contains("wx/login")||
                        oldRequest.url.encodedPath.contains("wb/login")||
                        oldRequest.url.encodedPath.contains("qq/login")){
                        bodyBuilder.build()
                    }
                    else{

                        val b = bodyBuilder.addEncoded("token",user.datas.token)
                            .addEncoded("usKey",user.datas.usKey)
                            .build()

                        val map = HashMap<String,String>()
                        for(i in 0 until b.size){
                            map[b.encodedName(i)] = URLDecoder.decode(b.encodedValue(i),"UTF-8")
                        }
                        val str = StringBuffer()
                        val tMap = TreeMap(map)
                        val it = tMap.entries.iterator()
                        while (it.hasNext()){
                            val entry  = it.next() as Map.Entry<String,String>
                            str.append("${entry.key}=${entry.value}&")
                        }
                        val s = str.toString().subSequence(0,str.toString().length-1)
                        val sign = Md5Security.getMD5(s.toString()+"yzy@#key$*%^&salt~")
                        bodyBuilder.addEncoded("sign",sign)
                        bodyBuilder.build()

                    }
                }
                return chain.proceed(oldRequest.newBuilder().url(url).post(body).build())
            }
            else if(oldRequest.body is MultipartBody){
                val bodyBuilder = MultipartBody.Builder()
                var body = oldRequest.body as MultipartBody
                for(i in 0 until body.size){
                    bodyBuilder.addPart(body.part(i))
                }
                body = bodyBuilder
                    .addFormDataPart("app","android")
                    .addFormDataPart("overseas","false")
                    .addFormDataPart("token",SpManager.getUserEntity()?.datas?.token ?: "")
                    .addFormDataPart("usKey",SpManager.getUserEntity()?.datas?.usKey ?: "")
                    .addFormDataPart("version", PhoneUtil.getAppVersionName())
                    .build()
                return chain.proceed(oldRequest.newBuilder().post(body).build())
            }
            else{

                val str = StringBuffer()
                str.append("app=android&")
                str.append("overseas=$isOverseas&")
                str.append("token=${SpManager.getUserEntity()?.datas?.token ?: ""}&")
                str.append("usKey=${SpManager.getUserEntity()?.datas?.usKey ?: ""}&")
                str.append("version=${PhoneUtil.getAppVersionName()}")
                val sign = Md5Security.getMD5(str.toString()+"yzy@#key$*%^&salt~")

                val bodyBuilder = MultipartBody.Builder()
                val body = bodyBuilder
                    .addFormDataPart("app","android")
                    .addFormDataPart("overseas","$isOverseas")
                    .addFormDataPart("token",SpManager.getUserEntity()?.datas?.token ?: "")
                    .addFormDataPart("usKey",SpManager.getUserEntity()?.datas?.usKey ?: "")
                    .addFormDataPart("version", PhoneUtil.getAppVersionName())
                    .addFormDataPart("sign",sign)
                    .build()

                return chain.proceed(oldRequest.newBuilder().url(url).post(body).build())
            }
        }

        return chain.proceed(chain.request())
    }
}