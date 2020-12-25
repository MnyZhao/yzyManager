package com.idolmedia.yzymanager.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.multidex.MultiDex
import com.google.gson.Gson
import com.idolmedia.yzymanager.BuildConfig
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.model.bean.PushBean
import com.idolmedia.yzymanager.model.entity.MessageStatusEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.commodity.CommodityManageSingleActivity
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.*
import com.umeng.message.entity.UMessage
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareConfig
import com.zhy.autolayout.config.AutoLayoutConifg


/**
 *  时间：2019/6/25-16:14
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.app BaseApplication
 *  描述：
 */
class BaseApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        instance = this

        //布局适配
        AutoLayoutConifg.getInstance().useDeviceSize()
        initSmartRefresh()

        initUMeng()
    }


    companion object{
        //baseApplication的单例
        @SuppressLint("StaticFieldLeak")
        lateinit var instance : BaseApplication

    }

    /**Dex分包*/
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initSmartRefresh(){

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater { context, layout ->
            layout.setPrimaryColorsId(R.color.white, R.color.black)//全局设置主题颜色
            ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate)//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater { context, layout ->
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate)
        }

    }

    private fun initUMeng(){

        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "65af509a487cdf94bee5dea0bf6e01b0")

        PlatformConfig.setWeixin("wx5dd86cacd2d506c9", "b279c12fed9a633c0e6d09aa1d865ce5")
        PlatformConfig.setQQZone("1108066958", "ImScRD9m4vD8yX65")
        PlatformConfig.setSinaWeibo("393870748", "1b7203b2f4e64601687935b3220ebc8f", "https://sns.whalecloud.com/sina2/callback")

        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        val config = UMShareConfig()
        config.isNeedAuthOnGetUserInfo(true)
        UMShareAPI.get(this).setShareConfig(config)

        //Push
        val mPushAgent = PushAgent.getInstance(this)
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.setNotificaitonOnForeground(true)
        mPushAgent.displayNotificationNumber = 0//参数number可以设置为0~10之间任意整数。当参数为0时，表示不合并通知。
        mPushAgent.notificationPlaySound = MsgConstant.NOTIFICATION_PLAY_SERVER //声音
        mPushAgent.notificationPlayLights = MsgConstant.NOTIFICATION_PLAY_SERVER//呼吸灯
        mPushAgent.notificationPlayVibrate = MsgConstant.NOTIFICATION_PLAY_SERVER//振动

        /**收到推送*/
        val messageHandler = object : UmengMessageHandler() {
            override fun handleMessage(context: Context?, msg: UMessage?) {
                super.handleMessage(context, msg)
            }
        }
        mPushAgent.messageHandler = messageHandler

        /**
         * 推送点击
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故c
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         */
        val notificationClickHandler = object : UmengNotificationClickHandler(){
            override fun dealWithCustomAction(context: Context?, message: UMessage?) {
                message?.let { msg->

                    Log.d("====Push",msg.toString())
                    val p = msg.extra["androidPush"]
                    val bean = Gson().fromJson(p, PushBean::class.java)
                    bean?.let {
                        if (SpManager.userIsManage() && it.type == "checkResult"){
                            ToastUtil.show("此消息与当前账号角色不匹配,请切换账号进入消息查看",3000)
                            return
                        }
                        if ((it.param.shoppingTo == "1" && SpManager.isOverseasSystem()) ||
                            it.param.shoppingTo == "2" && !SpManager.isOverseasSystem()){
                            ToastUtil.show("当前商品需切换系统操作,请切换系统进入消息查看",3000)
                            return
                        }

                        RetrofitHelper.instance().queryMessageStatus("",it.param.timeBatch ?: "",object : BaseObserver<MessageStatusEntity>(){
                            override fun onSuccess(t: MessageStatusEntity) {
                                if (t.resultCode==1){
                                    if (t.datas.clickType == "3"){
                                        ToastUtil.show("消息已失效")
                                    }else{
                                        val intent = Intent(context, CommodityManageSingleActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        intent.putExtra("commodityId",it.param.shopcommon_id)
                                        intent.putExtra("isPassed",it.param.clickType=="1")
                                        startActivity(intent)
                                    }
                                }
                            }

                            override fun onError(msg: String) {
                                ToastUtil.show(msg)
                            }
                        })

                    }

                }
            }
        }
        mPushAgent.notificationClickHandler = notificationClickHandler

        mPushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(deviceToken: String?) {
                SpManager.saveDeviceToken(deviceToken ?: "")
            }

            override fun onFailure(s: String?, s1: String?) {
            }

        })
    }

}