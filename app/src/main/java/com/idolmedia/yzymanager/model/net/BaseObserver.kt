package com.idolmedia.yzymanager.model.net

import com.idolmedia.yzymanager.base.BaseEntity
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 *  时间：2019/5/16-16:33
 *  @auther yuLook
 *  描述：
 */
abstract class BaseObserver<T : BaseEntity>:Observer<T> {

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: T) {
        onSuccess(t)
//        if (t.resultCode == 4){
//            SpManager.clearUserEntity()
//            ToastUtil.show("当前登录已失效，请重新登录")
//        }else
        if (t.resultCode != 1){
            onError(t.msg)
        }
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()

        if (!NetWorkUtil.isNetConnected()) {
            onError("网络不可用,请检查你的网络")
        } else if (e is SocketTimeoutException) {
            onError("连接超时，请稍后再试")
        } else if (e is JSONException) {
            onError("数据解析错误，请稍后再试")
        } else if (e is NumberFormatException) {
            onError("数据解析错误，请稍后再试")
        } else if (e is HttpException || e is ConnectException) {
            onError("未链接到服务器，请稍后再试")
        } else {
            onError("未知错误，请稍后再试")
        }
        onComplete()
    }

    abstract fun onSuccess(t:T)
    abstract fun onError(msg:String)

}