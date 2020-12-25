package com.idolmedia.yzymanager.viewmodel.commodity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.google.gson.Gson
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.base.*
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.interfaces.OnResultListener
import com.idolmedia.yzymanager.model.entity.CommodityLinkEntity
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.CopyUtil
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.view.commodity.*

/**
 *  时间：2020/10/22-11:24
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity ItemCommodityBean
 *  描述：
 */
abstract class ItemBaseManageBean(val commodityItem : CommodityListEntity.Data, val onItemDeleteListener: OnItemDeleteListener?) : BaseBean() {

    var layoutId = -1
    override fun getViewType(): Int {
        return layoutId
    }

    val strCommodityId = ObservableField<String>()
    val strCommodityStatus = ObservableField<String>()
    val strCommodityName = ObservableField<String>()
    val strCommodityLimit = ObservableField<String>()
    val strCommodityPrice = ObservableField<String>()
    val strCommoditySales = ObservableField<String>()
    val strSubjectId = ObservableField<String>()
    val strCommodityImg = ObservableField<String>()
    val strShopName = ObservableField<String>()
    val strShopId = ObservableField<String>()

    val strLabel1 = ObservableField<String>()
    val strLabel2 = ObservableField<String>()
    val visibleLabel1 = ObservableInt()

    val strAuditStatus = ObservableField<String>()
    val strAuditButton = ObservableField<String>()
    val visibleAuditButton = ObservableInt(View.GONE)

    val visibleAuditRefuse = ObservableInt(View.GONE)
    val visibleRefuse = ObservableInt(View.GONE)
    /**是否展示更多*/
    val isMore = ObservableBoolean(false)
    /**当前状态是否上架*/
    val isShelves = ObservableBoolean(false)
    /**当前状态是否隐藏销量*/
    val isHiddenSale = ObservableBoolean(false)
    /**当前状态是否隐藏商品*/
    val isHiddenCommodity = ObservableBoolean(false)

    val manageList = ArrayList<BaseBean>()
    var adapter : BaseAdapter? = null
    /**是否是审核  审核与上架的操作接口不一样（数据在不同的表里）*/
    var isAudit = false
    var onAuditPassedResultListener:OnResultListener? = null
    var onAuditRefuseResultListener:OnResultListener? = null

    init {

        strCommodityImg.set(commodityItem.imageUrl)

        strCommodityId.set("商品ID:${commodityItem.shopcommonId}")

        strCommodityName.set(commodityItem.shopName)
        if (commodityItem.limitCount.isNullOrEmpty() || commodityItem.limitCount.toInt()==0){
            strCommodityLimit.set("限购:不限购")
        }else{
            strCommodityLimit.set("限购:${commodityItem.limitCount}")
        }
        if (!commodityItem.subjectId.isNullOrEmpty()){
            strSubjectId.set("合并专题ID:${commodityItem.subjectId}")
        }

        strShopId.set("商家ID:${commodityItem.virtualuserId}")
        strShopName.set("商家信息:${commodityItem.nickName}")
        strCommodityPrice.set("商品价格:¥${commodityItem.betweenPrice}")

        when(commodityItem.isVipShop){
            "0" -> {
                visibleLabel1.set(View.GONE)
            }
            "1" -> {
                visibleLabel1.set(View.VISIBLE)
                strLabel1.set("会员-仅限会员")
            }
            "2" -> {
                visibleLabel1.set(View.VISIBLE)
                strLabel1.set("会员-皆可购买")
                strCommodityPrice.set("商品价格:¥${commodityItem.vipPrice}")
            }
        }

        when(commodityItem.shopType){
            "activity" ->strCommodityStatus.set("活动商品")
            "support"  ->strCommodityStatus.set("应援商品")
            "ordinary" ->strCommodityStatus.set("普通商品")
            "discount" ->strCommodityStatus.set("特惠商品")
            "reserve2" ->strCommodityStatus.set("定金商品${if (commodityItem.reserveStatus == "2")"-尾款" else ""}")
            "crowdfunding" -> strCommodityStatus.set("众筹商品")
        }

        if (commodityItem.shopType == "reserve2"){
            //定金商品
            if (commodityItem.reserveStatus == "2"){
                //尾款状态
                strCommoditySales.set("定金销量:${commodityItem.saleNoStr} 尾款销量:${if (commodityItem.finalSaleNo.isNullOrEmpty()) "0" else commodityItem.finalSaleNo}")
            }else{
                strCommoditySales.set("定金销量:${commodityItem.saleNoStr}")
            }
        }else{
            strCommoditySales.set("销量:${commodityItem.saleNoStr}")
        }

        if (commodityItem.isMergeExpress == "0"){
            strLabel2.set("非合并运费商品")
        }else{
            strLabel2.set("合并运费商品")
        }

        isShelves.set(commodityItem.putaway == "0")

        isHiddenSale.set(commodityItem.salesSecret == "1")
        isHiddenCommodity.set(commodityItem.isHide == "1")

        if (commodityItem.checkStatus == "0"){
            strAuditStatus.set("待审核")
            strAuditButton.set("撤销")
            visibleAuditButton.set(View.VISIBLE)
            visibleAuditRefuse.set(View.VISIBLE)
        }
        else if(commodityItem.checkStatus == "1"){
            strAuditStatus.set("审核成功")
        }
        else if(commodityItem.checkStatus == "2"){
            strAuditStatus.set("审核失败")
            strAuditButton.set("查看审核被拒原因")
            visibleRefuse.set(View.VISIBLE)
            visibleAuditRefuse.set(View.GONE)
            visibleAuditButton.set(View.VISIBLE)
        }
        else if(commodityItem.checkStatus == "3"){
            strAuditStatus.set("撤销审核")
            strAuditButton.set("再次提交")
            visibleAuditButton.set(View.GONE)
            visibleAuditRefuse.set(View.VISIBLE)
        }
        else if(commodityItem.checkStatus == "4"){
            strAuditStatus.set("审核中")
            strAuditButton.set("撤销")
            visibleAuditButton.set(View.VISIBLE)
            visibleAuditRefuse.set(View.VISIBLE)
        }

    }

    fun onClickMore(view: View){
        isMore.set(!isMore.get())
        refreshButton()

        if (isMore.get()){
            for(index in 5 until manageList.size){
                adapter?.add(manageList[index])
            }
            adapter?.notifyInserted(5)
        }else{
            adapter?.removeAllDateFromIndex(5)
        }

    }

    /**撤销审核*/
    fun onClickRevocation(view:View){
        if (strAuditButton.get() == "撤销"){
            val pop = BasePopWindow(view.context,R.layout.pop_common_content)
            val popModel = BasePopViewModel()
            popModel.content.set("确定撤销该商品吗？")
            popModel.listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                    pop.dismiss()
                }

                override fun onPopRight(model: BaseViewModel) {
                    pop.dismiss()
                    revocationAudit()
                }

            }
            pop.popModel = popModel
            pop.showAtCenter(view)
        }
        else if(strAuditButton.get() == "查看审核被拒原因"){
            val pop = BasePopWindow(view.context,R.layout.pop_common_content_check)
            val popModel = BasePopViewModel()
            popModel.content.set(commodityItem.checkReason)
            popModel.listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                    pop.dismiss()
                }

                override fun onPopRight(model: BaseViewModel) {
                    pop.dismiss()
                }

            }
            pop.popModel = popModel
            pop.showAtCenter(view)
        }

    }

    /**上下架操作*/
    fun onClickShelves(view: View){
        val pop = BasePopWindow(view.context,R.layout.pop_common_content)
        val popModel = BasePopViewModel()
        if (isShelves.get()){
            popModel.content.set("确定下架该商品吗？")
        }else{
            popModel.content.set("确定上架该商品吗？")
        }
        popModel.listener = object : BasePopWindow.OnPopClickListener{
            override fun onPopLeft(model: BaseViewModel) {
                pop.dismiss()
            }

            override fun onPopRight(model: BaseViewModel) {
                pop.dismiss()
                putawayCommodity(if (isShelves.get()) 1 else 0)
            }

        }
        pop.popModel = popModel
        pop.showAtCenter(view)
    }

    /**调成库存*/
    fun onClickInventory(view: View){
        val intent = Intent(view.context,CommodityAdjustInventoryActivity::class.java)
        intent.putExtra("id",commodityItem.shopcommonId)
        view.context.startActivity(intent)
    }

    /**限购数量*/
    fun onClickLimit(view: View){
        val intent = Intent(view.context, CommodityAdjustLimitActivity::class.java)
        intent.putExtra("isAudit",isAudit)
        intent.putExtra("commodityId",commodityItem.shopcommonId)
        intent.putExtra("commodityName",commodityItem.shopName)
        intent.putExtra("limitCount",commodityItem.limitCount)
        intent.putExtra("limitTimes",commodityItem.limitTimes)
        (view.context as Activity).startActivityForResult(intent,3)
    }

    fun onClickEdit(view: View){
        if (isAudit){
            //管理员查看审理商品进行编辑时
            if (commodityItem.toViewType == "1"){
                //审核尾款
                val intent = Intent(view.context,CommodityFinalPaymentActivity::class.java)
                intent.putExtra("id",commodityItem.shopcommonId)
                intent.putExtra("isVipShop",commodityItem.isVipShop)
                intent.putExtra("position",position)
                intent.putExtra("isAudit",isAudit)
                intent.putExtra("shopId",commodityItem.virtualuserId)
                intent.putExtra("shopName",commodityItem.nickName)
                intent.putExtra("json1",Gson().toJson(commodityItem))
                intent.putExtra("fromId",commodityItem.virtualuserId)
                (view.context as Activity).startActivityForResult(intent,2)
            }else{
                //审核详情
                val intent = Intent(view.context,CommodityEditActivity::class.java)
                intent.putExtra("commodityId",commodityItem.shopcommonId)
                intent.putExtra("isAudit",isAudit)
                intent.putExtra("vId",commodityItem.virtualuserId)
                intent.putExtra("vName",commodityItem.nickName)
                (view.context as Activity).startActivityForResult(intent,4)
            }
        }else{
            if (commodityItem.isEdit){
                ToastUtil.show("请先等待此商品审核后再行处理")
            }else{
                val intent = Intent(view.context,CommodityEditActivity::class.java)
                intent.putExtra("commodityId",commodityItem.shopcommonId)
                intent.putExtra("isAudit",isAudit)
                intent.putExtra("vId",commodityItem.virtualuserId)
                intent.putExtra("vName",commodityItem.nickName)
                (view.context as Activity).startActivityForResult(intent,4)
            }
        }
    }

    fun onClickCopyCommodity(view: View){
        CopyUtil.copy(view.context,commodityItem.shopcommonId)
    }

    fun onClickCopyShop(view: View){
        CopyUtil.copy(view.context,commodityItem.virtualuserId)
    }

    fun onClickCopyLink(view: View){
        RetrofitHelper.instance().getLink(commodityItem.shopcommonId,object : BaseObserver<CommodityLinkEntity>(){
            override fun onSuccess(t: CommodityLinkEntity) {
                if (t.url.isNullOrEmpty()){
                    ToastUtil.show("复制失败")
                }else{
                    CopyUtil.copy(view.context,t.url)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }
        })
    }

    fun onClickCopyLinkTest(view: View){
        RetrofitHelper.instance().getLinkTest(commodityItem.shopcommonId,object : BaseObserver<CommodityLinkEntity>(){
            override fun onSuccess(t: CommodityLinkEntity) {
                if (t.url.isNullOrEmpty()){
                    ToastUtil.show("复制失败")
                }else{
                    CopyUtil.copy(view.context,t.url)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }
        })
    }

    /**点击事件-结束定金*/
    fun onClickOverReserve(view:View){
        val pop = BasePopWindow(view.context,R.layout.pop_common_content)
        val popModel = BasePopViewModel()
        popModel.content.set("确定结束定金吗？")
        popModel.listener = object : BasePopWindow.OnPopClickListener{
            override fun onPopLeft(model: BaseViewModel) {
                pop.dismiss()
            }

            override fun onPopRight(model: BaseViewModel) {
                pop.dismiss()
                RetrofitHelper.instance().overReserve(isAudit,commodityItem.shopcommonId,commodityItem.shopType,object : BaseObserver<BaseEntity>(){
                    override fun onSuccess(t: BaseEntity) {
                        if (t.resultCode==1){
                            ToastUtil.show("操作成功，商品已结束定金")

                            adapter?.let {

                                for(item in it.getDate()){
                                    if (item is ItemManageButtonBean && item.name == "结束定金"){
                                        item.name = "开启尾款"
                                        item.strName.set(item.name)
                                        break
                                    }
                                }
                            }

                        }
                    }

                    override fun onError(msg: String) {
                        ToastUtil.show(msg)
                    }

                })
            }

        }
        pop.popModel = popModel
        pop.showAtCenter(view)
    }

    /**点击事件-开启尾款*/
    fun onClickOpenFinal(view:View){
        if (commodityItem.isEdit){
            ToastUtil.show("请先等待此商品审核后再行处理")
        }else{
            val intent = Intent(view.context,CommodityFinalPaymentActivity::class.java)
            intent.putExtra("id",commodityItem.shopcommonId)
            intent.putExtra("isVipShop",commodityItem.isVipShop)
            intent.putExtra("position",position)
            intent.putExtra("isAudit",isAudit)
            intent.putExtra("fromId",commodityItem.virtualuserId)
            intent.putExtra("shopId",commodityItem.virtualuserId)
            intent.putExtra("shopName",commodityItem.nickName)
            (view.context as Activity).startActivityForResult(intent,2)
        }
    }

    /**点击事件-设置退款*/
    fun onClickRefundSetting(view:View){
        val intent = Intent(view.context,CommodityRefundActivity::class.java)
        intent.putExtra("isAudit",isAudit)
        intent.putExtra("refunds",commodityItem.refunds)
        intent.putExtra("refundsTips",commodityItem.refundsTips)
        intent.putExtra("refundsReason",commodityItem.refundsReason)
        intent.putExtra("shopcommonId",commodityItem.shopcommonId)
        intent.putExtra("position",position)
        (view.context as Activity).startActivityForResult(intent,1)
    }
    /**点击事件-确认退款*/
    fun onClickRefund(view:View){

    }

    /**点击事件-隐藏销量*/
    fun onClickHiddenSale(view: View){
        val pop = BasePopWindow(view.context,R.layout.pop_common_content)
        pop.popModel = BasePopViewModel().apply {
            if (isHiddenSale.get()){
                content.set("是否显示销量？")
            }else{
                content.set("是否隐藏销量？")
            }
            listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                    pop.dismiss()
                }

                override fun onPopRight(model: BaseViewModel) {
                    pop.dismiss()
                    hiddenSale(if (isHiddenSale.get()) 0 else 1)
                }
            }
        }
        pop.showAtCenter(view)
    }

    /**点击事件-隐藏商品*/
    fun onClickHidden(view: View){
        val pop = BasePopWindow(view.context,R.layout.pop_common_content)
        pop.popModel = BasePopViewModel().apply {
            if (isHiddenCommodity.get()){
                content.set("确定显示该商品吗？")
            }else{
                content.set("确定隐藏该商品吗？")
            }
            listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                    pop.dismiss()
                }

                override fun onPopRight(model: BaseViewModel) {
                    pop.dismiss()
                    hiddenCommodity(if (isHiddenCommodity.get()) 0 else 1)
                }
            }
        }
        pop.showAtCenter(view)
    }

    /**点击事件-设置排行榜数量*/
    fun onClickRank(view:View){
        val pop = BasePopWindow(view.context,R.layout.pop_commodity_rank_count)
        pop.popModel = BasePopViewModel().apply {
            content.set(if (commodityItem.showBuyhistory.isNullOrEmpty()) "-1" else commodityItem.showBuyhistory)
            listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                    pop.dismiss()
                }

                override fun onPopRight(model: BaseViewModel) {
                    if (content.get().isNullOrEmpty()){
                        ToastUtil.show("请设置数量")
                    }else{
                        pop.dismiss()
                        settingRankCount(content.get()?: "")
                    }
                }
            }
        }
        pop.showAtCenter(view)
    }

    /**点击事件-锁定附加信息与收获地址*/
    fun onClickLock(lock: Int){
        RetrofitHelper.instance().setCommodityLock(lock,commodityItem.shopcommonId,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("设置成功")
                    if (lock==2){
                        commodityItem.lockOrderAddition = "1"
                        commodityItem.lockOrderAddress = "1"
                    }
                    else if(lock==1){
                        commodityItem.lockOrderAddition = "0"
                    }else if(lock==0){
                        commodityItem.lockOrderAddress = "0"
                    }

                    refreshButton()

                    adapter?.notifyData(manageList)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    fun onClickDelete(view: View){
        val pop = BasePopWindow(view.context,R.layout.pop_common_content)
        pop.popModel = BasePopViewModel().apply {
            content.set("确定删除该商品吗？")
            listener = object : BasePopWindow.OnPopClickListener{
                override fun onPopLeft(model: BaseViewModel) {
                    pop.dismiss()
                }

                override fun onPopRight(model: BaseViewModel) {
                    pop.dismiss()
                    deleteCommodity()
                }
            }
        }
        pop.showAtCenter(view)
    }

    /**点击事件-拒绝/通过审核*/
    fun onClickAudit(view:View,status:Int){
        if (status==0){
            val pop = BasePopWindow(view.context,R.layout.pop_common_content)
            pop.popModel = BasePopViewModel().apply {
                content.set("确定通过审核吗？")
                listener = object : BasePopWindow.OnPopClickListener{
                    override fun onPopLeft(model: BaseViewModel) {
                        pop.dismiss()
                    }

                    override fun onPopRight(model: BaseViewModel) {
                        pop.dismiss()
                        passedAudit()
                    }
                }
            }
            pop.showAtCenter(view)
        }else{
            val pop = BasePopWindow(view.context,R.layout.pop_common_content_edit)
            pop.popModel = BasePopViewModel().apply {
                title.set("确定拒绝审核吗？")
                listener = object : BasePopWindow.OnPopClickListener{
                    override fun onPopLeft(model: BaseViewModel) {
                        pop.dismiss()
                    }

                    override fun onPopRight(model: BaseViewModel) {
                        if (content.get().isNullOrEmpty()){
                            ToastUtil.show("请务必填写拒绝审核的理由")
                        }else{
                            pop.dismiss()
                            refuseAudit(content.get()!!)
                        }
                    }
                }
            }
            pop.showAtCenter(view)
        }
    }

    private fun putawayCommodity(putaway:Int){

        RetrofitHelper.instance().commodityPutaway(putaway,commodityItem.shopcommonId,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("操作成功")
                    onItemDeleteListener?.onItemDelete(position,this@ItemBaseManageBean)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })

    }

    private fun hiddenSale(salesSecret:Int){
        RetrofitHelper.instance().setCommodityHiddenSale(isAudit,salesSecret,commodityItem.shopcommonId,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    isHiddenSale.set(!isHiddenSale.get())
                    ToastUtil.show("操作成功")
                    adapter?.getDate()?.let {
                        for(item in it){
                            item as ItemManageButtonBean
                            if (item.name == "隐藏销量" || item.name == "显示销量"){
                                item.strName.set(if (isHiddenSale.get()) "显示销量" else "隐藏销量")
                                break
                            }
                        }
                    }
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    private fun hiddenCommodity(isHide:Int){
        RetrofitHelper.instance().setCommodityHiddenCommodity(isAudit,isHide,commodityItem.shopcommonId,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    isHiddenCommodity.set(!isHiddenCommodity.get())
                    ToastUtil.show("操作成功")

                    adapter?.getDate()?.let {
                        for(item in it){
                            item as ItemManageButtonBean
                            if (item.name == "隐藏该商品" || item.name == "显示该商品"){
                                item.strName.set(if (isHiddenCommodity.get()) "显示该商品" else "隐藏该商品")
                                break
                            }
                        }
                    }

                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    private fun settingRankCount(count:String){
        RetrofitHelper.instance().setCommodityRankCount(count,commodityItem.shopcommonId,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("设置成功")
                    commodityItem.showBuyhistory = count
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    private fun deleteCommodity(){
        RetrofitHelper.instance().commodityDelete(commodityItem.shopcommonId,isAudit,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("商品删除成功")
                    onItemDeleteListener?.onItemDelete(position,this@ItemBaseManageBean)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    private fun revocationAudit(){
        RetrofitHelper.instance().commodityRevocation(commodityItem.shopcommonId,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("操作成功")
                    strAuditStatus.set("撤销审核")
                    visibleAuditButton.set(View.GONE)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }

        })
    }

    /**通过审核*/
    private fun passedAudit(){
        RetrofitHelper.instance().passedAudit(commodityItem.shopcommonId,commodityItem.timesTamp,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("操作成功，商品已通过审核")
                    onItemDeleteListener?.onItemDelete(position,this@ItemBaseManageBean)
                    onAuditPassedResultListener?.onResultSuccess(t)
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }
        })
    }

    /**拒绝审核*/
    private fun refuseAudit(reason:String){
        RetrofitHelper.instance().refuseAudit(commodityItem.shopcommonId,reason,commodityItem.timesTamp,object : BaseObserver<BaseEntity>(){
            override fun onSuccess(t: BaseEntity) {
                if (t.resultCode==1){
                    ToastUtil.show("操作成功，商品已拒绝审核")
                    if (onAuditRefuseResultListener == null){
                        onItemDeleteListener?.onItemDelete(position,this@ItemBaseManageBean)
                    }else{
                        onAuditRefuseResultListener?.onResultSuccess(t)
                    }
                }
            }

            override fun onError(msg: String) {
                ToastUtil.show(msg)
            }
        })
    }

    abstract fun refreshButton()

}