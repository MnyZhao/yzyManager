package com.idolmedia.yzymanager.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityMineAccountBinding
import com.idolmedia.yzymanager.model.bean.PickerItemBean
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.main.MineInformationViewModel

/**
 *  时间：2020/11/26-10:06
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.main PasswordChangeActivity
 *  描述：修改主账号页Activity
 */
class AccountChangeActivity : BaseStateActivity<ActivityMineAccountBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_mine_account
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(MineInformationViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        val accountType = intent.getStringExtra("accountType") ?: ""
        binding.viewModel?.accountType = accountType
        binding.viewModel?.titleBar?.set(TitleBarViewModel(if (accountType == "phone") "修改手机号码" else "修改邮箱号码","提交",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                binding.viewModel?.let {
                    if (it.strAccount.get().isNullOrEmpty()){
                        ToastUtil.show("请填写账号")
                        return
                    }

                    if (it.strSmsCode.get().isNullOrEmpty()){
                        ToastUtil.show("请填写验证码")
                        return
                    }

                    RetrofitHelper.instance().changeAccount(
                        it.strAccount.get()?:"" ,
                        it.phoneCode,
                        it.strSmsCode.get()?: "",object:BaseObserver<BaseEntity>(){
                            override fun onSuccess(t: BaseEntity) {
                                if (t.resultCode==1){
                                    ToastUtil.show("更换成功")

                                    val user = SpManager.getUserEntity()
                                    user?.datas?.username = it.strAccount.get()?: ""
                                    SpManager.refreshUserEntity(user)

                                    val intent = Intent()
                                    intent.putExtra("account",it.strAccount.get()?: "")
                                    setResult(1,intent)
                                    finish()
                                }
                            }

                            override fun onError(msg: String) {
                                ToastUtil.show(msg)
                            }
                        })

                }
            }

        }))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val account = intent.getStringExtra("account") ?: ""

        binding.viewModel?.let {
            if (it.accountType == "phone"){
                it.phoneCode = "86"
                it.strAccountTip.set("中国大陆+86")
                it.strAccountHint.set("请输入您的手机号")
                it.strTip.set("您当前绑定的手机号为：${account}")

                it.data.add(PickerItemBean(86, "中国大陆"))
                it.data.add(PickerItemBean(886, "中国台湾"))
                it.data.add(PickerItemBean(852, "中国香港"))
                it.data.add(PickerItemBean(853, "中国澳门"))
                it.data.add(PickerItemBean(60, "马来西亚"))
                it.data.add(PickerItemBean(82, "韩国"))
                it.data.add(PickerItemBean(61, "澳大利亚"))
                it.data.add(PickerItemBean(55, "巴西"))
                it.data.add(PickerItemBean(1, "加拿大"))
                it.data.add(PickerItemBean(1, "美国"))
                it.data.add(PickerItemBean(81, "日本"))
            }else{
                it.strAccountTip.set("邮箱账号")
                it.strAccountHint.set("请输入您的邮箱号")
                it.strTip.set("您当前绑定的邮箱号为：${account}")
            }
        }

    }

    override fun onDestroy() {
        binding.viewModel?.onDestroy()
        super.onDestroy()
    }

}