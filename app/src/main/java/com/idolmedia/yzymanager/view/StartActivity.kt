package com.idolmedia.yzymanager.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ktx.immersionBar
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.model.entity.CurrentTimeEntity
import com.idolmedia.yzymanager.model.entity.LoginEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 *  时间：2020/10/19-15:25
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager StartActivity
 *  描述：
 */
class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        immersionBar{
            fitsSystemWindows(true)
            statusBarColor(R.color.white)
            navigationBarColor(R.color.white)
            statusBarDarkFont(true)
        }

        val user = SpManager.getUserEntity()
        if (user == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }else{
            getCurrentTime()
        }

    }

    private fun getCurrentTime(){
        RetrofitHelper.instance().getCurrentTime(object : BaseObserver<CurrentTimeEntity>(){
            override fun onSuccess(t: CurrentTimeEntity) {
                if (t.resultCode==1){
                    autoLogin(t.datas.currentTime)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
                startActivity(Intent(this@StartActivity, LoginActivity::class.java))
                finish()
            }

        })
    }

    private fun autoLogin(time:String){

        RetrofitHelper.instance().loginAuto(time,object:BaseObserver<LoginEntity>(){
            override fun onSuccess(t: LoginEntity) {
                if (t.resultCode==1){
                    SpManager.refreshUserEntity(t)
                    startActivity(Intent(this@StartActivity, MainActivity::class.java))
                    finish()
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
                startActivity(Intent(this@StartActivity, LoginActivity::class.java))
                finish()
            }

        } )
    }

}