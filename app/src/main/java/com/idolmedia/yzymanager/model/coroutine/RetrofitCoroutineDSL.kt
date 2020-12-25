package com.idolmedia.yzymanager.model.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import java.io.IOException
import java.net.ConnectException

/**
 *  时间：2020/12/8-10:43
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.model.net RetrofitCoroutineDSL
 *  描述：
 */
class RetrofitCoroutineDSL <T> {

    var api:(Call<Result<T>>)? = null

    internal var onSuccess: ((T) -> Unit)? = null
        private set
    internal var onFail: ((msg: String, errorCode: Int) -> Unit)? = null
        private set
    internal var onComplete: (() -> Unit)? = null
        private set

    /**
     * 获取数据成功
     * @param block (T) -> Unit
     */
    fun onSuccess(block: (T) -> Unit) {
        this.onSuccess = block
    }

    /**
     * 获取数据失败
     * @param block (msg: String, errorCode: Int) -> Unit
     */
    fun onFail(block: (msg: String, errorCode: Int) -> Unit) {
        this.onFail = block
    }

    /**
     * 访问完成
     * @param block () -> Unit
     */
    fun onComplete(block: () -> Unit) {
        this.onComplete = block
    }

    internal fun clean() {
        onSuccess = null
        onComplete = null
        onFail = null
    }


    fun <T> CoroutineScope.retrofit(dsl: RetrofitCoroutineDSL<T>.() -> Unit){
        this.launch(Dispatchers.Main){
            val coroutine = RetrofitCoroutineDSL<T>().apply(dsl)
            coroutine.api?.let { call ->
                //async 并发执行 在IO线程中
                val deferred = async(Dispatchers.IO) {
                    try {
                        call.execute()
                    }catch (e : ConnectException){
                        coroutine.onFail?.invoke("网络连接错误",-1)
                        null
                    }catch (e: IOException){
                        coroutine.onFail?.invoke("位置连接错误",-2)
                        null
                    }
                }

                //当协程取消的时候，取消网络请求
                deferred.invokeOnCompletion {
                    if (deferred.isCancelled){
                        call.cancel()
                        coroutine.clean()
                    }
                }

                //await 等待异步执行的结果
                val response = deferred.await()
                if (response == null){
                    coroutine.onFail?.invoke("未收到返回结果",-3)
                }else{
                    response.let {
                        if (response.isSuccessful){
                            response.body()?.getOrNull()?.let {
                                coroutine.onSuccess?.invoke(it)
                            }
                        }
                    }
                }
                coroutine.onComplete?.invoke()
            }
        }
    }

}