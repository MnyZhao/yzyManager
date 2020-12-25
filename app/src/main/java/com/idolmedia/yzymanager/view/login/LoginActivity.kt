package com.idolmedia.yzymanager.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityLoginBinding
import com.idolmedia.yzymanager.model.entity.CurrentTimeEntity
import com.idolmedia.yzymanager.model.entity.LoginEntity
import com.idolmedia.yzymanager.model.net.*
import com.idolmedia.yzymanager.utils.Md5Security
import com.idolmedia.yzymanager.utils.PhoneUtil
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.MainActivity
import com.idolmedia.yzymanager.viewmodel.login.LoginViewModel
import com.idolmedia.yzymanager.widget.CustomLoading
import com.umeng.message.PushAgent
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  时间：2020/10/16-15:07
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.login LoginActivity
 *  描述：
 */
class LoginActivity : BaseStateActivity<ActivityLoginBinding>(), UMAuthListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun getViewModel(): BaseViewModel {
        return ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.ivBack.setOnClickListener { finish() }
    }

    override fun initView(savedInstanceState: Bundle?) {

        val listener = object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                binding.viewModel?.let {
                    if (!it.strAccount.get().isNullOrEmpty() &&
                        !it.strPassword.get().isNullOrEmpty() &&
                        !it.strCode.get().isNullOrEmpty()){

                        if (it.strAccount.get()!!.trim().isNotEmpty() &&
                            it.strPassword.get()!!.trim().isNotEmpty() &&
                            it.strCode.get()!!.trim().isNotEmpty()){
                            it.isCanLogin.set(true)
                            return
                        }
                    }
                    it.isCanLogin.set(false)
                }
            }
        }

        binding.viewModel?.strAccount?.addOnPropertyChangedCallback(listener)
        binding.viewModel?.strPassword?.addOnPropertyChangedCallback(listener)
        binding.viewModel?.strCode?.addOnPropertyChangedCallback(listener)

        Glide.with(this)
            .load(binding.viewModel?.imgCode?.get())
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.ivCode)

        binding.ivCode.setOnClickListener {
            refreshCode()
        }

        binding.ivLogin.setOnClickListener {
            if (binding.viewModel!!.isCanLogin.get()){
                CustomLoading.getInstance().showLoad(this)

                binding.viewModel?.viewModelScope?.launch {
                    withContext(Dispatchers.IO){
                        try {
                            //获取当前服务器时间
                            val timeEntity = CoroutinesUtils.getData( RetrofitHttp.api.getCoroutinesCurrentTime() )

                            val time = timeEntity.datas.currentTime
                            val map = binding.viewModel!!.getLoginData(time)
                            //请求登录
                            val loginEntity = CoroutinesUtils.getData(RetrofitHttp.api.loginPasswordCoroutines(map))
                            //保存用户信息
                            SpManager.saveUserEntity(loginEntity)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()

                        }catch (e:Exception){
                            CoroutinesUtils.onError(e)
                            if (e is ResultException){
                                //返回结果错误刷新验证码
                                refreshCode()
                            }
                        }finally {
                            //结束loading
                            CustomLoading.getInstance().closeLoad()
                        }
                    }
                }
            }
        }

        binding.ivWx.setOnClickListener {

            if (UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.WEIXIN)){
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, this)
            }else{
                ToastUtil.show("未安装微信")
            }

        }

        binding.ivQq.setOnClickListener {

            if (UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.QQ)){
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, this)
            }else{
                ToastUtil.show("未安装QQ")
            }

        }

        binding.ivWb.setOnClickListener {

            if (UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.SINA)){
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, this)
            }else{
                ToastUtil.show("未安装微博")
            }

        }

    }

    private fun refreshCode(){
        Glide.with(this).clear(binding.ivCode)
        Glide.with(this)
            .load(binding.viewModel?.imgCode?.get())
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.ivCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart(media: SHARE_MEDIA?) {
        CustomLoading.getInstance().showLoad(this)
    }

    override fun onComplete(media: SHARE_MEDIA?, code: Int, map: MutableMap<String, String>?) {
        binding.viewModel?.let { model ->
            if (map == null || media == null){
                ToastUtil.show("三方登录数据异常，请稍后再试")
                CustomLoading.getInstance().closeLoad()
                return
            }

            Log.d("====map:", Gson().toJson(map))

            val loginMap = HashMap<String,Any?>()
            loginMap["deviceToken"] = PushAgent.getInstance(this).registrationId

            var loginType = 0

            if (media.toString() == "QQ"){
                loginMap["unionid"] = map["unionid"]
            }
            else if (media.toString() == "WEIXIN"){
                loginType = 1
                loginMap["unionid"] = map["uid"]
            }
            else if (media.toString() == "SINA"){
                loginType = 2
                loginMap["uid"] = map["uid"]
            }

            RetrofitHelper.instance().getCurrentTime(object : BaseObserver<CurrentTimeEntity>(){
                override fun onSuccess(t: CurrentTimeEntity) {
                    if (t.resultCode==1){

                        loginMap["signTime"] = t.datas.currentTime

                        val str = StringBuffer()
                        str.append("app=android&")
                        str.append("deviceToken=${loginMap["deviceToken"]}&")
                        str.append("overseas=${SpManager.getValueBoolean("isOverseas")}&")
                        str.append("signTime=${loginMap["signTime"]}&")
                        if (loginType == 2){
                            str.append("uid=${loginMap["uid"]}&")
                        }else{
                            str.append("unionid=${loginMap["unionid"]}&")
                        }
                        str.append("version=${PhoneUtil.getAppVersionName()}")
                        loginMap["sign"] = Md5Security.getMD5(str.toString()+"yzy@#key$*%^&salt~")

                        RetrofitHelper.instance().loginThree(loginType, loginMap, object : BaseObserver<LoginEntity>() {
                            override fun onSuccess(t: LoginEntity) {
                                if (t.resultCode==1){
                                    SpManager.saveUserEntity(t)
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    finish()
                                }
                            }

                            override fun onError(msg: String) {
                                ToastUtil.show(msg,2000)
                            }

                            override fun onComplete() {
                                super.onComplete()
                                CustomLoading.getInstance().closeLoad()
                            }
                        })
                    }
                }

                override fun onError(msg: String) {
                    ToastUtil.show(msg)
                    CustomLoading.getInstance().closeLoad()
                }
            })
        }
    }

    override fun onError(media: SHARE_MEDIA?, code: Int, e: Throwable?) {
        ToastUtil.show("取消三方登录")
        CustomLoading.getInstance().closeLoad()
    }

    override fun onCancel(media: SHARE_MEDIA?, code: Int) {
        ToastUtil.show("唤起第三方异常 error$code")
        CustomLoading.getInstance().closeLoad()
    }

}