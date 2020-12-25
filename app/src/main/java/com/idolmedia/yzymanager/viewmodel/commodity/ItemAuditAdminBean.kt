package com.idolmedia.yzymanager.viewmodel.commodity

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.databinding.ItemAuditAdminBinding
import com.idolmedia.yzymanager.interfaces.OnItemDeleteListener
import com.idolmedia.yzymanager.model.entity.CommodityListEntity
import com.idolmedia.yzymanager.utils.Dp2PxUtils
import com.idolmedia.yzymanager.utils.TimeUtils

/**
 *  时间：2020/10/22-11:24
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity ItemCommodityBean
 *  描述：
 */
class ItemAuditAdminBean( commodityItem : CommodityListEntity.Data,  onItemDeleteListener: OnItemDeleteListener?) : ItemBaseManageBean(commodityItem,onItemDeleteListener) {

    init {
        layoutId = R.layout.item_audit_admin
        isAudit = true
    }

    override fun refreshButton() {

        manageList.clear()
        manageList.add(ItemManageButtonBean(commodityItem,this,"更多"))
        manageList.add(ItemManageButtonBean(commodityItem,this,"复制链接"))

        if(commodityItem.shopType == "reserve2"){
            if(commodityItem.reserveStatus == "1"){
                if (TimeUtils.compareOvertime(commodityItem.currentTime,commodityItem.endTime)){
                    manageList.add(ItemManageButtonBean(commodityItem,this,"开启尾款"))
                }else{
                    manageList.add(ItemManageButtonBean(commodityItem,this,"结束定金"))
                }
            }else{
                manageList.add(ItemManageButtonBean(commodityItem,this,"限购数量"))
            }
        }
        else if(commodityItem.shopType == "crowdfunding"){
            manageList.add(ItemManageButtonBean(commodityItem,this,"生产发货"))
        }
        else{
            manageList.add(ItemManageButtonBean(commodityItem,this,"限购数量"))
        }

        manageList.add(ItemManageButtonBean(commodityItem,this,"编辑"))
        manageList.add(ItemManageButtonBean(commodityItem,this,"删除"))

        if (isMore.get()){

            if(commodityItem.shopType == "support"){
                manageList.add(ItemManageButtonBean(commodityItem,this,if (isHiddenSale.get()) "显示销量" else "隐藏销量"))
                manageList.add(ItemManageButtonBean(commodityItem,this,if (isHiddenCommodity.get()) "显示该商品" else "隐藏该商品"))
                manageList.add(ItemManageButtonBean(commodityItem,this,"设置退款"))
                manageList.add(ItemManageButtonBean(commodityItem,this,"设置排行榜数量"))
                manageList.add(ItemManageButtonBean(commodityItem,this,"复制测试链接"))
            }
            else{

                if(commodityItem.shopType == "reserve2" && commodityItem.reserveStatus == "1"){
                    manageList.add(ItemManageButtonBean(commodityItem,this,"限购数量"))
                }
                manageList.add(ItemManageButtonBean(commodityItem,this,if (isHiddenSale.get()) "显示销量" else "隐藏销量"))
                manageList.add(ItemManageButtonBean(commodityItem,this,if (isHiddenCommodity.get()) "显示该商品" else "隐藏该商品"))
                manageList.add(ItemManageButtonBean(commodityItem,this,"设置退款"))
                manageList.add(ItemManageButtonBean(commodityItem,this,"设置排行榜数量"))
                manageList.add(ItemManageButtonBean(commodityItem,this,"复制测试链接"))
                if (commodityItem.lockOrderAddition == "0" && commodityItem.lockOrderAddress == "0"){
                    manageList.add(ItemManageButtonBean(commodityItem,this,"锁定附加信息和收货地址"))
                }
                else if (commodityItem.lockOrderAddition == "1" && commodityItem.lockOrderAddress == "1"){
                    manageList.add(ItemManageButtonBean(commodityItem,this,"解锁附加信息"))
                    manageList.add(ItemManageButtonBean(commodityItem,this,"解锁收货地址"))
                }
                else{
                    if (commodityItem.lockOrderAddition == "0"){
                        manageList.add(ItemManageButtonBean(commodityItem,this,"锁定附加信息"))
                    }else{
                        manageList.add(ItemManageButtonBean(commodityItem,this,"解锁附加信息"))
                    }
                    if (commodityItem.lockOrderAddress == "0"){
                        manageList.add(ItemManageButtonBean(commodityItem,this,"锁定收货地址"))
                    }else{
                        manageList.add(ItemManageButtonBean(commodityItem,this,"解锁收货地址"))
                    }
                }
            }
        }

    }

    override fun onBindViewModel(context: Context, binding: ViewDataBinding?, position: Int) {
        super.onBindViewModel(context, binding, position)
        if(binding is ItemAuditAdminBinding){
            if (binding.recyclerView.itemDecorationCount<=0){
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.flexWrap = FlexWrap.WRAP
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.alignItems = AlignItems.STRETCH
                layoutManager.justifyContent = JustifyContent.FLEX_START
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        super.getItemOffsets(outRect, view, parent, state)
                        outRect.right = Dp2PxUtils.dip2px(16).toInt()
                        outRect.top = Dp2PxUtils.dip2px(15).toInt()
                    }
                })
            }
            refreshButton()
            adapter = BaseAdapter(context,manageList)
            binding.recyclerView.adapter = adapter
        }

    }


}