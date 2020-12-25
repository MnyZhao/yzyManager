package com.idolmedia.yzymanager.view.ido

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
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityIdoAssociatedBinding
import com.idolmedia.yzymanager.databinding.PopIdoAssociatedBinding
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.model.entity.IdoAssociatedEntity
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.ido.IdoAssociatedViewModel
import com.idolmedia.yzymanager.viewmodel.ido.ItemIdoBean
import com.idolmedia.yzymanager.viewmodel.pop.PopIdoAssociatedViewModel

/**
 *  时间：2020/10/29-11:36
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.ido IdoAssociatedActivity
 *  描述：关联爱豆页Activity
 */
class IdoAssociatedActivity : BaseStateActivity<ActivityIdoAssociatedBinding>(),OnItemDeleteListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_ido_associated
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(IdoAssociatedViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("关联爱豆","保存",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                setResult(1,binding.viewModel?.getIdoIntent())
                finish()
            }

        }))

        binding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                view?.let {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH){
                        binding.smartLayout.autoRefresh()
                        hideKeyboard(view)
                        return true
                    }
                }
                return false
            }
        })

    }

    override fun initView(savedInstanceState: Bundle?) {

        val aidouIds = intent.getStringExtra("aidouIds") ?: ""
        val idoList = intent.getStringExtra("idoList") ?: ""

        if (idoList.isNotEmpty()){

            val type =object : TypeToken<ArrayList<IdoAssociatedEntity.Ido>>(){}.type
            val list = Gson().fromJson<ArrayList<IdoAssociatedEntity.Ido>>(idoList,type)
            for(item in list){
                val bean = ItemIdoBean(this,item)
                binding.viewModel?.selectList?.add(bean)
            }
            binding.viewModel?.strIdoCountTip?.set("已选中${list.size}个爱豆")

        }

        binding.viewModel?.idoIds = aidouIds
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setItemViewCacheSize(50)
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.tvSelected.setOnClickListener {view->
            binding.viewModel?.let {
                if (it.selectList.size>0){
                    val pop = BasePopWindow(this,R.layout.pop_ido_associated)
                    (pop.binding as PopIdoAssociatedBinding).recyclerView.layoutManager = LinearLayoutManager(this)
                    pop.binding.tvConfirm.setOnClickListener { pop.dismiss() }
                    ItemDecorationCommon.addItemDecorationTop(1,R.color.line_color,pop.binding.recyclerView)
                    pop.popModel = PopIdoAssociatedViewModel(this,it.selectList,BaseAdapter(this))
                    pop.showAtBottom(view)
                }
            }
        }

        binding.smartLayout.setOnRefreshListener {
            binding.viewModel?.pageNo = 1
            binding.viewModel?.queryIdo()
        }
        binding.smartLayout.setOnLoadmoreListener {
            binding.viewModel!!.pageNo += 1
            binding.viewModel!!.queryIdo()
        }
        binding.smartLayout.autoRefresh()

    }

    override fun onItemDelete(position: Int, bean: BaseBean) {
        bean as ItemIdoBean
        binding.viewModel?.selectList?.removeAt(position)
        binding.viewModel?.adapter?.get()?.let {
            for(item in it.getDate()){
                if (item is ItemIdoBean){
                    if (item.ido.idolId == bean.ido.idolId){
                        item.isSelect.set(false)
                        break
                    }
                }
            }
        }
        binding.viewModel?.strIdoCountTip?.set("已选中${binding.viewModel?.selectList?.size}个爱豆")
    }

}