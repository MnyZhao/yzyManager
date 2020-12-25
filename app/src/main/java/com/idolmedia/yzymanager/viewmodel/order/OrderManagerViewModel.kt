package com.idolmedia.yzymanager.viewmodel.order

import android.view.View
import androidx.databinding.ObservableField
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.BasePopWindow
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.interfaces.OnResultListener
import com.idolmedia.yzymanager.model.entity.OrderListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.SpManager
import com.idolmedia.yzymanager.utils.TimeUtils
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.order.OrderManagerFragment
import com.idolmedia.yzymanager.viewmodel.ItemEmptyBean
import com.idolmedia.yzymanager.viewmodel.pop.PopOrderFilterTypeViewModel
import kotlinx.android.synthetic.main.fragment_order_manager.*


/**
 *  时间：2020/10/20-11:36
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.order OrderManagerViewModel
 *  描述：
 */
class OrderManagerViewModel : BaseViewModel() {

    val strSearchWord = ObservableField<String>()
    val strFilterType = ObservableField("商品ID")
    val strOrderStartTime = ObservableField("下单开始时间")
    val strOrderEndTime = ObservableField("下单结束时间")
    val strPayStartTime = ObservableField("支付开始时间")
    val strPayEndTime = ObservableField("支付开始时间")
    val strCommodityName = ObservableField<String>()
    val strCommodityId = ObservableField<String>()
    val strUserId = ObservableField<String>()
    val strUserName = ObservableField<String>()
    val strConsigneeName = ObservableField<String>()
    val strConsigneePhone = ObservableField<String>()
    val strOrderNumber = ObservableField<String>()
    val strExpressNumber = ObservableField<String>()

    var fragmentList = ArrayList<OrderManagerFragment>()
    var viewPagerCurrentIndex = 0

    /**下拉弹窗的搜索类型
     * 0商品ID 1商品名称 2用户ID 3用户昵称
     * 4用户账户 5收货人电话 6收货人姓名 7订单编号
     * 9快递编号 9合并邮费专题ID 10付款订单编号
     */
    var filterType = 0
    /** 0侧滑搜索 1输入框搜索*/
    var searchType = 0
    var startTime = ""
    var endTime = ""
    var payStartTime = ""
    var payEndTime = ""

    fun onClickFilterType(view: View){
        val fragment = fragmentList[viewPagerCurrentIndex]
        if (fragment.binding?.smartLayout?.isRefreshing == true){
            return
        }
        val pop = BasePopWindow(view.context, R.layout.pop_order_filter_type)
        pop.popModel = PopOrderFilterTypeViewModel(pop,this)
        pop.showAtViewBottomDarken(view)
    }

    fun onClickShowPicker(view: View, type: Int){
        //时间选择器
        val pvTime = TimePickerBuilder(view.context) { date, view ->
            val str = TimeUtils.getTime(date)
            when(type){
                0 -> {
                    strOrderStartTime.set(str)
                    startTime = str
                }
                1 -> {
                    strOrderEndTime.set(str)
                    endTime = str
                }
                2 -> {
                    strPayStartTime.set(str)
                    payStartTime = str
                }
                3 -> {
                    strPayEndTime.set(str)
                    payEndTime = str
                }
            }
        }
            .setType(booleanArrayOf(true, true, true, true, true, true))
            .setLineSpacingMultiplier(1.5f)
            .build()
        pvTime.show()
    }

    /**重置*/
    fun onClickReset(view:View){
        startTime = ""
        endTime = ""
        payStartTime = ""
        payEndTime = ""
        strOrderStartTime.set("下单开始时间")
        strOrderEndTime.set("下单结束时间")
        strPayStartTime.set("支付开始时间")
        strPayEndTime.set("支付开始时间")
        strCommodityName.set("")
        strCommodityId.set("")
        strUserId.set("")
        strUserName.set("")
        strConsigneeName.set("")
        strConsigneePhone.set("")
        strOrderNumber.set("")
        strExpressNumber.set("")
    }

    fun onClickConfirm(view:View){
        searchType = 0
        strSearchWord.set("")
        for(fragment in fragmentList){
            fragment.pageNo = 1
            fragment.isFirstVisibleHint = true
        }
        fragmentList[viewPagerCurrentIndex].smart_layout.autoRefresh()
    }

    fun queryOrder(pageNo:Int,status:String,adapter: BaseAdapter,listener: OnResultListener){

        val map = getMap()
        map["orderStatuss"] = status
        map["pageNo"] = pageNo
        RetrofitHelper.instance().getOrderList(map,object : BaseObserver<OrderListEntity>(){
            override fun onSuccess(t: OrderListEntity) {
                if (t.resultCode==1){
                    if (pageNo==1){
                        adapter.clear()
                    }

                    if (t.datas.isEmpty()&&pageNo==1){
                        //空数据
                        adapter.emptyBean = ItemEmptyBean("暂无此状态订单")
                        isLoadMore.set(false)
                    }else if (t.datas.isNotEmpty()){
                        val current = adapter.getMaxPosition()
                        for(item in t.datas){
                            adapter.add(ItemOrderBean(item))
                        }
                        adapter.notifyInserted(current)
                        isLoadMore.set(true)
                    }else{
                        isLoadMore.set(false)
                    }

                    listener.onResultSuccess(t)

                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

            override fun onComplete() {
                super.onComplete()
                finishRefresh.set(finishRefresh.get()+1)
            }
        })

    }

    private fun getMap():HashMap<String,Any?>{

        val map = HashMap<String,Any?>()

        if (searchType==0){
            map["consignee"] = strConsigneeName.get() ?: ""
            map["endPayTime"] = payEndTime
            map["endTime"] = endTime
            map["isBusiness"] = !SpManager.userIsManage()
            map["nickName"] = strUserName.get() ?: ""
            map["orderNum"] = strOrderNumber.get() ?: ""
            map["phone"] = strConsigneePhone.get() ?: ""
            map["shopName"] = strCommodityName.get() ?: ""
            map["shopcommonId"] = strCommodityId.get() ?: ""
            map["startPayTime"] = payStartTime
            map["startTime"] = startTime
            map["virtualuserId"] = strUserId.get() ?: ""
            map["waybillNo"] = strExpressNumber.get() ?: ""
        }else{
            when(filterType){
                0 -> map["shopcommonId"] = strSearchWord.get() ?: ""
                1 -> map["shopName"] =     strSearchWord.get() ?: ""
                2 -> map["virtualuserId"]= strSearchWord.get() ?: ""
                3 -> map["nickName"] =     strSearchWord.get() ?: ""
                4 -> map["primaryAccountNo"] = strSearchWord.get() ?: ""
                5 -> map["phone"] =        strSearchWord.get() ?: ""
                6 -> map["consignee"] =    strSearchWord.get() ?: ""
                7 -> map["orderNum"] =     strSearchWord.get() ?: ""
                8 -> map["waybillNo"] =    strSearchWord.get() ?: ""
                9 -> map["subjectId"] =    strSearchWord.get() ?: ""
                10 -> map["payOrderNum"] = strSearchWord.get() ?: ""
            }
        }

        return map
    }

}