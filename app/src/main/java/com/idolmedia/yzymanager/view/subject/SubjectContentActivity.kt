package com.idolmedia.yzymanager.view.subject

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivitySubjectContentBinding
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.subject.SubjectAddViewModel

/**
 *  时间：2020/11/19-18:08
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.subject SubjectContentActivity
 *  描述：专题内容页Activity
 */
class SubjectContentActivity : BaseStateActivity<ActivitySubjectContentBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_subject_content
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(SubjectAddViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("专题内容"))
    }

    override fun initView(savedInstanceState: Bundle?) {

        val content = intent.getStringExtra("json") ?: ""
        binding.viewModel?.strContent?.set(content)

        binding.tvSave.setOnClickListener {

            if (binding.viewModel?.strContent?.get().isNullOrEmpty()){
                ToastUtil.show("请编辑专题内容")
                return@setOnClickListener
            }

            val intent = Intent()
            intent.putExtra("json",binding.viewModel?.strContent?.get()?: "")
            setResult(1,intent)
            finish()
        }

    }
}