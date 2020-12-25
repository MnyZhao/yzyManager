package com.idolmedia.yzymanager.view.subject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivitySubjectLabelBinding
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.subject.SubjectLabelViewModel
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import java.lang.StringBuilder

/**
 *  时间：2020/10/19-16:38
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.subject SubjectTitleActivity
 *  描述：专题标签页Activity
 */
class SubjectLabelActivity : BaseStateActivity<ActivitySubjectLabelBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_subject_label
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(SubjectLabelViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("专题标签","保存",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                val intent = Intent()
                intent.putExtra("json",Gson().toJson(list))
                val label = StringBuilder()
                for(s in list){
                    label.append("$s、")
                }
                intent.putExtra("title",label.toString())
                setResult(1,intent)
                finish()
            }

        }))
    }

    val list = ArrayList<String>()
    override fun initView(savedInstanceState: Bundle?) {

        val json = intent.getStringExtra("json") ?: ""
        val type =object :TypeToken<ArrayList<String>>(){}.type
        val l = Gson().fromJson<ArrayList<String>>(json,type)
        l?.let {
            list.addAll(it)
        }

        binding.flowLayout.adapter =object : TagAdapter<String>(list){
            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                val view = LayoutInflater.from(this@SubjectLabelActivity).inflate(R.layout.item_subject_label,null,false)
                val text =view.findViewById<TextView>(R.id.tv_label)
                text.text = t
                text.setOnClickListener {
                    list.removeAt(position)
                    binding.flowLayout.adapter.notifyDataChanged()
                }
                return view
            }
        }

        binding.tvSave.setOnClickListener {

            binding.viewModel?.let {

                if (!it.strLabel.get().isNullOrEmpty()){
                    if (it.strLabel.get()!!.trim().isNotEmpty()){
                        list.add(it.strLabel.get()!!.trim())
                        binding.flowLayout.adapter.notifyDataChanged()
                    }
                }

            }

        }

    }

}