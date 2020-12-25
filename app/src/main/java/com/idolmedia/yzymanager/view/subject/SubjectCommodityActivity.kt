package com.idolmedia.yzymanager.view.subject

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivitySubjectCommodityBinding
import com.idolmedia.yzymanager.databinding.ItemSubjectCommodityBinding
import com.idolmedia.yzymanager.utils.recyclerview.ItemDecorationCommon
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.subject.SubjectCommodityViewModel
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener
import com.yanzhenjie.recyclerview.touch.OnItemStateChangedListener
import java.util.*

/**
 *  时间：2020/10/19-16:38
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.subject SubjectTitleActivity
 *  描述：专题商品编辑页Activity
 */
class SubjectCommodityActivity : BaseStateActivity<ActivitySubjectCommodityBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_subject_commodity
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(SubjectCommodityViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.viewModel?.subjectId = intent.getStringExtra("subjectId") ?: ""
        binding.viewModel?.titleBar?.set(TitleBarViewModel("设置商品排序","保存",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                binding.viewModel?.saveSubjectCommoditySort()
            }
        }))
    }

    override fun initView(savedInstanceState: Bundle?) {

        ItemDecorationCommon.addItemDecoration(1,binding.recyclerView)
        binding.recyclerView.isLongPressDragEnabled = true
//        binding.recyclerView.setSwipeMenuCreator { leftMenu, rightMenu, position ->
//            rightMenu?.addMenuItem(SwipeMenuItem(this@SubjectCommodityActivity).apply {
//                this.text = "删除"
//                this.setTextColor(ContextCompat.getColor(this@SubjectCommodityActivity,R.color.white))
//                this.width = Dp2PxUtils.dip2px(62).toInt()
//                this.height = ViewGroup.LayoutParams.MATCH_PARENT
//                this.background = ContextCompat.getDrawable(this@SubjectCommodityActivity, R.color.red)
//            })
//        }
//        binding.recyclerView.setOnItemMenuClickListener { menuBridge, adapterPosition ->
//            binding.viewModel?.deleteCommodity(adapterPosition)
//        }
        binding.recyclerView.setOnItemMoveListener(object : OnItemMoveListener{
            override fun onItemMove(srcHolder: RecyclerView.ViewHolder?, targetHolder: RecyclerView.ViewHolder?): Boolean {
                val fromPosition = srcHolder?.adapterPosition ?: 0
                val targetPosition = targetHolder?.adapterPosition ?: 0
                Collections.swap(binding.viewModel?.adapter?.get()?.getDate()!!, fromPosition, targetPosition)
                binding.viewModel?.adapter?.get()?.notifyItemMoved(fromPosition, targetPosition)
                binding.viewModel?.sortCommodity()
                return true
            }

            override fun onItemDismiss(srcHolder: RecyclerView.ViewHolder?) {
            }

        })
        binding.recyclerView.setOnItemStateChangedListener { viewHolder, actionState ->
            if (actionState == OnItemStateChangedListener.ACTION_STATE_DRAG) {
                // 状态：正在拖拽。
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY)
                if (viewHolder is BaseAdapter.BaseViewHolder){
                    (viewHolder.binding as ItemSubjectCommodityBinding).consParent.setBackgroundColor(Color.LTGRAY)
                }
                binding.smartLayout.isEnableRefresh = false
                binding.smartLayout.isEnableLoadmore = false
            } else if (actionState == OnItemStateChangedListener.ACTION_STATE_SWIPE) {
                // 状态：滑动删除。
            } else if (actionState == OnItemStateChangedListener.ACTION_STATE_IDLE) {
                // 状态：手指松开。
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY)
                if (viewHolder is BaseAdapter.BaseViewHolder){
                    (viewHolder.binding as ItemSubjectCommodityBinding).consParent.setBackgroundColor(Color.WHITE)
                }
                binding.smartLayout.isEnableRefresh = true
                binding.smartLayout.isEnableLoadmore = true
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.viewModel?.adapter?.set(BaseAdapter(this))

        binding.smartLayout.setOnRefreshListener {
            binding.viewModel?.querySubjectCommodity()
        }
        binding.smartLayout.isEnableLoadmore = false
        binding.smartLayout.autoRefresh()

    }

}