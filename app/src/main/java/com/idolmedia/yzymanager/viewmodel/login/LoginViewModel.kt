package com.idolmedia.yzymanager.viewmodel.login

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.model.ApiConstant
import com.idolmedia.yzymanager.utils.*

/**
 *  时间：2020/10/16-15:07
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.login LoginViewModel
 *  描述：
 */
class LoginViewModel : BaseViewModel() {

    val isCanLogin = ObservableBoolean()

    val imgCode = ObservableField("${ApiConstant.BASEURL}/${ApiConstant.IMAGE_CODE}?app=android&overseas=false&version=${PhoneUtil.getAppVersionName()}")

    val strAccount = ObservableField("")
    val strPassword = ObservableField("")
    val strCode = ObservableField<String>()

    fun getLoginData(currentTime:String):HashMap<String,Any?>{

        val map = HashMap<String,Any?>()
        map["accountNo"] = strAccount.get()
        map["password"] = RSAUtils.encrypt(Md5Security.getMD5(strPassword.get()))
        map["signTime"] = currentTime
        map["vfCode"] = strCode.get()
        map["deviceToken"] = SpManager.getDeviceToken()

        val str = StringBuffer()
        str.append("accountNo=${map["accountNo"]}&")
        str.append("app=android&")
        str.append("deviceToken=${SpManager.getDeviceToken()}&")
        str.append("overseas=${SpManager.getValueBoolean("isOverseas")}&")
        str.append("password=${map["password"]}&")
        str.append("signTime=${map["signTime"]}&")
        str.append("version=${PhoneUtil.getAppVersionName()}&")
        str.append("vfCode=${map["vfCode"]}")
        map["sign"] = Md5Security.getMD5(str.toString()+"yzy@#key$*%^&salt~")

        return map

    }

    fun onClickPassword(view:View){

        if ((view as EditText).transformationMethod == HideReturnsTransformationMethod.getInstance()){
            view.transformationMethod = PasswordTransformationMethod.getInstance()
        }else{
            view.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }

    }

}