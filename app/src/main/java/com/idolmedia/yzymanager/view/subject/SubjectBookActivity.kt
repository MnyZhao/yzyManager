package com.idolmedia.yzymanager.view.subject

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivitySubjectBookBinding
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.subject.SubjectBookViewModel

/**
 *  时间：2020/10/19-13:39
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.subject SubjectManagerActivity
 *  描述：专题电子刊选择页Activity
 */
class SubjectBookActivity : BaseStateActivity<ActivitySubjectBookBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_subject_book
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(SubjectBookViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("添加电子刊","保存",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                val intent = Intent()
                binding.viewModel?.let {
                    if (it.selectList.isNotEmpty()){
                        intent.putExtra("json",Gson().toJson(it.selectList))
                    }
                }
                setResult(1,intent)
                finish()
            }

        }))

        val json = intent.getStringExtra("json") ?: ""
        val type = object: TypeToken<ArrayList<CommodityListEntity.Data>>(){}.type
        val list = Gson().fromJson<ArrayList<CommodityListEntity.Data>>(json,type)
        list?.let{
            binding.viewModel?.selectList?.addAll(it)
            binding.viewModel?.strCountTip?.set("已选${it.size}件电子书")
            for(item in it){
                binding.viewModel?.bookIds?.append(item.shopcommonId)
            }
        }

        binding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                view?.let {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH){
                        binding.viewModel?.let {model->
                            model.pageNo = 1
                            model.queryBook()
                        }
                        hideKeyboard(view)
                        return true
                    }
                }
                return false
            }
        })

    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setItemViewCacheSize(30)
        binding.smartLayout.setOnRefreshListener {
            binding.viewModel?.pageNo = 1
            binding.viewModel?.queryBook()
        }
        binding.smartLayout.setOnLoadmoreListener {
            binding.viewModel?.let {
                it.pageNo += 1
                it.queryBook()
            }
        }
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.smartLayout.post {
            binding.smartLayout.autoRefresh()
        }

    }
}