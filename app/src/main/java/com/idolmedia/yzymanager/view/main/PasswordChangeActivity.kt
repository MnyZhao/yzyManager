package com.idolmedia.yzymanager.view.main

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityMineInformationBinding
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.Md5Security
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddBean
import com.idolmedia.yzymanager.viewmodel.main.MineInformationViewModel

/**
 *  时间：2020/11/26-10:06
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.main PasswordChangeActivity
 *  描述：修改密码页Activity
 */
class PasswordChangeActivity : BaseStateActivity<ActivityMineInformationBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_mine_information
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(MineInformationViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("修改密码","提交",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                binding.viewModel?.adapter?.get()?.let {
                    val map = HashMap<String,Any?>()
                    for(item in it.getDate()){
                        if (item is ItemCommodityAddBean){
                            if (item.strTitle.get()?.contains("原密码") == true){
                                if (item.strContent.get().isNullOrEmpty()){
                                    ToastUtil.show("请输入原密码")
                                    return
                                }else{
                                    map["oldPwd"] = Md5Security.getMD5(item.strContent.get()?: "")
                                }
                            }
                            else if (item.strTitle.get()?.contains("新密码") == true){
                                if (item.strContent.get().isNullOrEmpty()){
                                    ToastUtil.show("请输入新密码")
                                    return
                                }else{
                                    map["newPwd"] = Md5Security.getMD5(item.strContent.get()?: "")
                                }
                            }
                            else if (item.strTitle.get()?.contains("确认密码") == true){
                                if (item.strContent.get().isNullOrEmpty()){
                                    ToastUtil.show("请再次输入新密码")
                                    return
                                }else{
                                    map["confirmPwd"] = Md5Security.getMD5(item.strContent.get()?: "")
                                }
                            }
                        }
                    }
                    RetrofitHelper.instance().passwordChange(map,object : BaseObserver<BaseEntity>(){
                        override fun onSuccess(t: BaseEntity) {
                            if (t.resultCode==1){
                                ToastUtil.show("修改成功")
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

        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.viewModel?.adapter?.get()?.let {
            it.add(ItemCommodityAddBean("原密码：","","请输入原密码"))
            it.add(ItemCommodityAddBean("新密码：","","请输入6~20位新密码","", InputType.TYPE_CLASS_TEXT  or InputType.TYPE_TEXT_VARIATION_PASSWORD))
            it.add(ItemCommodityAddBean("确认密码：","","请再次输入新密码","",InputType.TYPE_CLASS_TEXT  or InputType.TYPE_TEXT_VARIATION_PASSWORD))
            it.notifyDataSetChanged()
        }

    }

}