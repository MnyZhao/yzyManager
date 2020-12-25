package com.idolmedia.yzymanager.view.order

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.base.BaseFragment
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.FragmentOrderManagerBinding
import com.idolmedia.yzymanager.interfaces.OnResultListener
import com.idolmedia.yzymanager.model.entity.OrderListEntity
import com.idolmedia.yzymanager.utils.animation.OrderTipAnimation
import com.idolmedia.yzymanager.viewmodel.order.OrderManagerViewModel

/**
 *  时间：2020/10/20-15:00
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.order OrderManagerFragment
 *  描述：
 */
class OrderManagerFragment : BaseFragment<FragmentOrderManagerBinding>(), OnResultListener,OrderTipAnimation.AnimationEndListener {
    override fun getLayoutId(): Int {
        return R.layout.fragment_order_manager
    }

    companion object{
        fun newInstance(type:String): OrderManagerFragment{
            val args = Bundle()
            args.putString("type",type)
            val fragment = OrderManagerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewModel(): BaseViewModel {
        return ViewModelProvider(activity!!).get(OrderManagerViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        orderStatus = arguments?.getString("type") ?: ""
    }


    lateinit var adapter : BaseAdapter
    var pageNo = 1
    var orderStatus = ""
    var animationPlaying = false

    override fun initViewOrData() {
        adapter = BaseAdapter(context!!)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerView?.setItemViewCacheSize(30)
        binding?.recyclerView?.adapter = adapter

        binding?.smartLayout?.setOnRefreshListener {
            isFirstVisibleHint = false
            pageNo = 1
            binding?.viewModel?.queryOrder(pageNo,orderStatus,adapter,this)
        }
        binding?.smartLayout?.setOnLoadmoreListener {
            pageNo += 1
            binding?.viewModel?.queryOrder(pageNo,orderStatus,adapter,this)
        }

        binding?.recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!animationPlaying){
                    animationPlaying = true
                    if (dy>0){
                        //上滑 隐藏
                        OrderTipAnimation.hide(binding?.tvCount,this@OrderManagerFragment)
                    }else{
                        //下滑 显示
                        OrderTipAnimation.show(binding?.tvCount,this@OrderManagerFragment)
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onResultSuccess(entity: BaseEntity) {
        entity as OrderListEntity
        binding?.tvCount?.text = "共计：${entity.totalCount}条"
        binding?.tvCount?.visibility = View.VISIBLE
    }

    override fun animationEnd() {
        animationPlaying = false
    }

    override fun onResume() {
        super.onResume()
        if (isFirstVisible()){
            binding?.smartLayout?.autoRefresh()
        }
    }

}