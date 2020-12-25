package com.idolmedia.yzymanager.view.commodity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityPurchaseNotesListBinding
import com.idolmedia.yzymanager.model.entity.CommodityPreconditionEntity
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.ItemCommodityAddSelectBean
import com.idolmedia.yzymanager.viewmodel.commodity.PurchaseNotesListViewModel

/**
 *  时间：2020/11/4-16:11
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity PurchaseNotesListActivity
 *  描述：
 */
class PurchaseNotesListActivity : BaseStateActivity<ActivityPurchaseNotesListBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_purchase_notes_list
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(PurchaseNotesListViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.titleBar?.set(TitleBarViewModel("选择购买须知分类"))
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val json = intent.getStringExtra("json")
        val type = object : TypeToken<ArrayList<CommodityPreconditionEntity.Note>>(){}.type
        val notesList = Gson().fromJson<ArrayList<CommodityPreconditionEntity.Note>>(json,type)

        val ids = intent.getStringExtra("ids") ?: ""

        val list = ArrayList<BaseBean>()
        for(note in notesList){
            list.add(ItemCommodityAddSelectBean(note.title,note.id,ids.contains("[${note.id}]")))
        }

        binding.viewModel?.adapter?.set(BaseAdapter(this,list))

        binding.viewModel?.adapter?.get()?.setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
            override fun itemClick(position: Int, item: BaseBean, parent: View) {
                item as ItemCommodityAddSelectBean
                if (!item.isSelect.get()){
                    val intent = Intent()
                    intent.putExtra("id",item.id)
                    intent.putExtra("title",item.strTitle.get())
                    setResult(1,intent)
                    finish()
                }
            }
        })

    }

}