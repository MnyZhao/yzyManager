package com.idolmedia.yzymanager.viewmodel.commodity

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.model.entity.CommodityListEntity

/**
 *  时间：2020/11/11-11:39
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.viewmodel.commodity ItemManageButtomBean
 *  描述：
 */
class ItemManageButtonBean(val commodityItem : CommodityListEntity.Data,val parentBean:ItemBaseManageBean,var name:String) : BaseBean(){

    override fun getViewType(): Int {
        return R.layout.item_manage_button
    }

    val strName = ObservableField<String>()
    val isMore = ObservableBoolean()

    init {
        strName.set(name)
    }

    fun onItemClick(view:View){

        when(name){
            "更多" ->{
                parentBean.onClickMore(view)
                isMore.set(!isMore.get())
            }
            "上架","下架" ->{
                parentBean.onClickShelves(view)
            }
            "调整库存" ->{
                parentBean.onClickInventory(view)
            }
            "限购数量" ->{
                parentBean.onClickLimit(view)
            }
            "编辑" ->{
                parentBean.onClickEdit(view)
            }
            "隐藏销量","显示销量" ->{
                parentBean.onClickHiddenSale(view)
            }
            "隐藏该商品","显示该商品" ->{
                parentBean.onClickHidden(view)
            }
            "结束定金" ->{
                parentBean.onClickOverReserve(view)
            }
            "开启尾款" ->{
                parentBean.onClickOpenFinal(view)
            }
            "设置退款" ->{
                parentBean.onClickRefundSetting(view)
            }
            "设置退款" ->{
                parentBean.onClickRefundSetting(view)
            }
            "确认退款" ->{
                parentBean.onClickRefund(view)
            }
            "复制链接" ->{
                parentBean.onClickCopyLink(view)
            }
            "复制测试链接" ->{
                parentBean.onClickCopyLinkTest(view)
            }
            "设置排行榜数量" ->{
                parentBean.onClickRank(view)
            }
            "锁定附加信息和收货地址" ->{
                parentBean.onClickLock(2)
            }
            "解锁附加信息" ->{
                parentBean.onClickLock(1)
            }
            "解锁收货地址" ->{
                parentBean.onClickLock(0)
            }
            "锁定附加信息","锁定收货地址" ->{
                parentBean.onClickLock(2)
            }
            "删除" ->{
                parentBean.onClickDelete(view)
            }
        }

    }

}