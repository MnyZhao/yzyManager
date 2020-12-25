package com.idolmedia.yzymanager.viewmodel.main

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.bean.PickerItemBean
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.ToastUtil
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 *  时间：2020/11/25-17:02
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.main MineInformationVeiwModel
 *  描述：
 */
class MineInformationViewModel : BaseViewModel() {

    val adapter = ObservableField<BaseAdapter>()

    val strAccount = ObservableField<String>()
    val strAccountTip = ObservableField<String>()
    val strAccountHint = ObservableField<String>()
    val strSmsCode = ObservableField<String>()
    val strSmsTime = ObservableField<String>("发送验证码")
    val smsBg = ObservableInt(R.drawable.shape_blue_bg_50)
    val strTip = ObservableField<String>()

    var accountType = ""
    var phoneCode = ""

    private var timeDisposable : Disposable?= null
    val data = ArrayList<PickerItemBean>()

    fun shopPhonePicker(view: View){
        if (accountType=="phone"){
            val pvOptions = OptionsPickerBuilder(view.context) { options1, options2, options3, v ->
                strAccountTip.set("${data[options1].countryName}+${data[options1].id}")
                phoneCode = data[options1].id.toString()
            }.build<PickerItemBean>()
            pvOptions.setPicker(data)
            pvOptions.show()
        }
    }

    fun onSmsCodeClick(view:View){
        if (strAccount.get()==null||strAccount.get()!!.trim().isEmpty()){
            ToastUtil.show("请输入手机号")
            return
        }

        if (timeDisposable!=null){
            return
        }

        smsBg.set(R.drawable.shape_gray9_bg_50)
        timeDisposable = Flowable.intervalRange(0,60,0,1, TimeUnit.SECONDS)
            .onBackpressureDrop()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                strSmsTime.set("倒计时(${60-it})")
            }
            .doOnComplete {
                if (timeDisposable!=null){
                    timeDisposable=null
                }
                strSmsTime.set("获取验证码")
                smsBg.set(R.drawable.shape_blue_bg_50)
            }
            .subscribe()

        RetrofitHelper.instance().sendSmsCode(strAccount.get()?:"",phoneCode,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("验证码发送成功")
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
                onDestroy()
            }
        })
    }

    fun onDestroy() {
        timeDisposable?.let {
            timeDisposable!!.dispose()
            timeDisposable = null
            strSmsTime.set("获取验证码")
            smsBg.set(R.drawable.shape_blue_bg_50)
        }
    }

}