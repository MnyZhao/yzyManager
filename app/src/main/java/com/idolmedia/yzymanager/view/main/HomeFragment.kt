package com.idolmedia.yzymanager.view.main

import android.content.Intent
import android.graphics.Rect
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.*
import com.idolmedia.yzymanager.databinding.FragmentMainHomeBinding
import com.idolmedia.yzymanager.interfaces.OnItemSelectListener
import com.idolmedia.yzymanager.model.entity.CurrentTimeEntity
import com.idolmedia.yzymanager.model.entity.HomeMessageEntity
import com.idolmedia.yzymanager.model.entity.LoginEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.Dp2PxUtils
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.MainActivity
import com.idolmedia.yzymanager.view.login.LoginActivity
import com.idolmedia.yzymanager.viewmodel.main.ItemHomeBean
import com.idolmedia.yzymanager.viewmodel.main.ItemHomeOrderBean
import com.idolmedia.yzymanager.viewmodel.main.MainHomeViewModel
import com.idolmedia.yzymanager.widget.CustomLoading

/**
 *  时间：2020/10/16-14:57
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.main HomeFragment
 *  描述：
 */
class HomeFragment : BaseFragment<FragmentMainHomeBinding>(),OnItemSelectListener {
    override fun getLayoutId(): Int {
        return R.layout.fragment_main_home
    }

    override fun getViewModel(): BaseViewModel {
        return ViewModelProvider(this).get(MainHomeViewModel::class.java)
    }

    override fun initViewOrData() {
        binding?.viewModel?.onItemSelectListener = this
        binding?.viewModel?.adapter?.set(BaseAdapter(context!!))

        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerView?.adapter = binding?.viewModel?.adapter?.get()
        binding?.recyclerView?.setItemViewCacheSize(10)
        binding?.recyclerView?.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = Dp2PxUtils.dip2px(1).toInt()
                outRect.bottom = Dp2PxUtils.dip2px(14).toInt()
            }
        })

        binding?.viewModel?.adapter?.get()?.let {
            it.add(ItemHomeOrderBean())
            if (SpManager.getUserIdentity() == "COMMON_PRODUCT_MERCHANT"){
                it.add(ItemHomeBean(R.layout.item_home_commodity_manager).apply {
                    strCommodityAdd.set("添加商品")
                    strAudit.set("审核中")
                })
                it.add(ItemHomeBean(R.layout.item_home_freight))
            }
            else if(SpManager.getUserIdentity() == "VIP_PRODUCT_MERCHANT"){
                it.add(ItemHomeBean(R.layout.item_home_commodity_manager).apply {
                    strCommodityAdd.set("添加VIP商品")
                    strAudit.set("审核中")
                })
                it.add(ItemHomeBean(R.layout.item_home_freight))
            }
            else if(SpManager.getUserIdentity() == "OPERATIONAL_MANAGER"){
                it.add(ItemHomeBean(R.layout.item_home_commodity_manager).apply {
                    strAudit.set("待审核")
                    visibleCommodityAdd.set(View.INVISIBLE)
                })

                if (SpManager.isOverseasSystem()){
                    it.add(ItemHomeBean(R.layout.item_home_other_manager_overseas))
                }else{
                    it.add(ItemHomeBean(R.layout.item_home_other_manager))
                }

                it.add(ItemHomeBean(R.layout.item_home_about_commodity_sort))
            }
            it.notifyDataSetChanged()
        }

        binding?.tvSystem?.setOnClickListener {
            val pop = BasePopWindow(context,R.layout.pop_common_content)
            val popModel = BasePopViewModel()
            popModel.content.set("确定切换至${ if (binding?.viewModel?.systemType?.get()==0) "海外" else "国内" }系统吗？")
            popModel.listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                    pop.dismiss()
                }

                override fun onPopRight(model: BaseViewModel) {
                    pop.dismiss()
                    binding?.viewModel?.onClickSystemSwitch(it)
                    queryNote()
                }
            }
            pop.popModel = popModel
            pop.showAtCenter(view)
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.viewModel?.showData()
        queryNote()
    }

    override fun onItemSelect(position: Int, bean: BaseBean) {
        CustomLoading.getInstance().showLoadCancelable(context,"切换中......")
        val current = SpManager.getUserIndex()
        SpManager.getUserEntity(position)
        RetrofitHelper.instance().getCurrentTime(object : BaseObserver<CurrentTimeEntity>(){
            override fun onSuccess(t: CurrentTimeEntity) {
                if (t.resultCode==1){
                    RetrofitHelper.instance().loginAuto(t.datas.currentTime,object:BaseObserver<LoginEntity>(){
                        override fun onSuccess(t: LoginEntity) {
                            if (t.resultCode==1){
                                ToastUtil.show("账号切换成功")
                                binding?.viewModel?.showData()
                                queryNote()
                            }
                        }

                        override fun onError(msg: String) {
                            SpManager.getUserEntity(current)
                            ToastUtil.show(msg)
                            startActivity(Intent(context,LoginActivity::class.java))
                        }

                        override fun onComplete() {
                            super.onComplete()
                            CustomLoading.getInstance().closeLoad()
                        }

                    } )
                }
            }

            override fun onError(msg: String) {
                SpManager.getUserEntity(current)
                ToastUtil.show(msg)
                CustomLoading.getInstance().closeLoad()
            }

        })
    }

    private fun queryNote(){
        (activity as MainActivity).binding.viewModel?.queryHomeNote(object : BaseObserver<HomeMessageEntity>(){
            override fun onSuccess(t: HomeMessageEntity) {
                if (t.resultCode==1){
                    binding?.viewModel?.adapter?.get()?.let {
                        val bean =  it.getDate()[0] as ItemHomeOrderBean
                        for(o in t.orderStatusNum){
                            if (o.orderStatus == "toPay"){
                                bean.strCountPay.set("${o.num}")
                            }
                            else if(o.orderStatus == "waitingDelivery"){
                                bean.strCountSend.set("${o.num}")
                            }
                            else if(o.orderStatus == "waitingFinalPay"){
                                bean.strCountWK.set("${o.num}")
                            }
                            else if(o.orderStatus == "waitingRefunded"){
                                bean.strCountRefund.set("${o.num}")
                            }
                        }

                        val manage =  it.getDate()[1] as ItemHomeBean
                        if (t.checkProductNum>0){
                            manage.visibleAudit.set(View.VISIBLE)
                            if (t.checkProductNum>99){
                                manage.strAuditCount.set("99+")
                            }else{
                                manage.strAuditCount.set("${t.checkProductNum}")
                            }
                        }else{
                            manage.visibleAudit.set(View.GONE)
                        }
                    }
                }
            }

            override fun onError(msg: String) {
            }

        })
    }

}