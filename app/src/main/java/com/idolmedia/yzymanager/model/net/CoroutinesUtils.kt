package com.idolmedia.yzymanager.model.net

import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.utils.ToastUtil
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 *  时间：2020/12/25-11:53
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.net CoroutinesError
 *  描述：
 */
object CoroutinesUtils {

    fun <T:BaseEntity>getData(entity:T?):T{
        return getData(entity,true)
    }

    fun <T:BaseEntity>getData(entity:T?,showToast:Boolean):T{
        if (entity!=null){
            if (entity.resultCode==1){
                return entity
            }else{
                throw ResultException(entity.msg,showToast)
            }
        }else{
            throw RuntimeException()
        }
    }

    fun onError(e: Throwable) {
        e.printStackTrace()
        if (!NetWorkUtil.isNetConnected()) {
            ToastUtil.show("网络不可用,请检查你的网络")
        } else if (e is SocketTimeoutException) {
            ToastUtil.show("连接超时，请稍后再试")
        } else if (e is JSONException) {
            ToastUtil.show("数据解析错误，请稍后再试")
        } else if (e is NumberFormatException) {
            ToastUtil.show("数据解析错误，请稍后再试")
        } else if (e is HttpException || e is ConnectException) {
            ToastUtil.show("未链接到服务器，请稍后再试")
        } else if (e is ResultException){
            if (e.showToast){
                ToastUtil.show(e.message)
            }
        } else {
            ToastUtil.show("未知错误，请稍后再试")
        }
    }

}