package com.idolmedia.yzymanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.databinding.library.baseAdapters.BR
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.viewmodel.ItemEmptyBean

/**
 *  时间：2019/6/20-14:14
 *  @author yuLook
 *  描述：
 */
open class BaseAdapter(context: Context) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    private var mContext = context
    private var mDataList = mutableListOf<BaseBean>()
    var mHeadList = mutableListOf<BaseBean>()
    /**空数据是的布局id*/
    var emptyBean : ItemEmptyBean? = null
    /**head*/
    var headNum = 0

    private var onHeadClickListener : OnHeadClickListener ?= null
    private var onItemClickListener : OnItemClickListener ?= null
    private var onItemRecycledListener : OnItemRecycledListener ?= null

    constructor(context : Context, dataList:ArrayList<BaseBean>):this(context){
        mDataList.addAll(dataList)
    }

    constructor(context : Context, item:BaseBean):this(context){
        mDataList.add(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(mContext),viewType,parent,false)
        return BaseViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        if (mDataList.size==0&&mHeadList.size==0&&emptyBean!=null){
            return emptyBean!!.getViewType()
        } else if (headNum>0&&position<headNum){
            return mHeadList[position].getViewType()
        }
        return mDataList[position-mHeadList.size].getViewType()
    }

    override fun getItemCount(): Int {
        if (mHeadList.size<=0&&mDataList.size<=0){
            return if (emptyBean!=null){
                1
            }else{
                0
            }
        }

        return mHeadList.size+mDataList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (mDataList.size==0&&mHeadList.size==0&&emptyBean!=null){
            holder.binding?.setVariable(BR.item,emptyBean!!)
        }else if(position<mHeadList.size){
            holder.binding?.setVariable(BR.item,mHeadList[position])
            mHeadList[position].onBindViewModel(mContext,holder.binding,position)

            //设置点击事件
            onHeadClickListener?.let {
                holder.itemView.setOnClickListener {
                    onHeadClickListener!!.headClick(position,mHeadList[position])
                }
            }
        }else if(position-headNum<mDataList.size){
            holder.binding?.setVariable(BR.item,mDataList[position-headNum])
            mDataList[position-headNum].onBindViewModel(mContext,holder.binding,position)

            //设置点击事件
            onItemClickListener?.let {
                holder.itemView.setOnClickListener {
                    onItemClickListener!!.itemClick(position,mDataList[position-headNum],holder.itemView)
                }
            }
        }
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
        super.onViewRecycled(holder)
        onItemRecycledListener?.let {
            onItemRecycledListener!!.onItemRecycled(holder)
        }
    }


    fun setOnHeadClickListener(onHeadClickListener : OnHeadClickListener){
        this.onHeadClickListener = onHeadClickListener
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemRecycledListener(onItemRecycledListener: OnItemRecycledListener){
        this.onItemRecycledListener = onItemRecycledListener
    }

    fun setEmpty(emptyBean : BaseBean){
        mHeadList.add(emptyBean)
        headNum = mHeadList.size
    }

    /**添加头布局*/
    fun addHead(head: BaseBean){
        headNum+=1
        mHeadList.add(head)
    }

    fun addHead(head: BaseBean,index: Int){
        headNum+=1
        mHeadList.add(index,head)
    }

    fun addAll(dates : List<BaseBean>){
        this.mDataList.addAll(dates)
    }

    fun add(data:BaseBean){
        this.mDataList.add(data)
    }

    /**add后刷新*/
    fun add(data:BaseBean,refresh:Int){
        this.mDataList.add(data)
        notifyItemChanged(refresh)
    }

    fun add(index:Int,data:BaseBean){
        this.mDataList.add(index,data)
    }

    fun  getDate() : MutableList<BaseBean>{
        return mDataList
    }

    fun getItem(position: Int):BaseBean?{
        return when {
            position<0 -> {
                null
            }
            position<mHeadList.size -> {
                mHeadList[position]
            }
            position<mDataList.size+mHeadList.size -> {
                mDataList[position-mHeadList.size]
            }
            else -> null
        }
    }

    /**获得notifyItemInserted()方法使用时的position*/
    fun getMaxPosition():Int{
        return mDataList.size+headNum
    }

    /**从下标position的位置开始刷新后面的数据*/
    fun notifyInserted(position: Int){
        if (position>=0&&position<=getDate().size+mHeadList.size){
            notifyItemInserted(position)
            notifyItemRangeChanged(position,100)
        }else{
            notifyDataSetChanged()
        }
    }

    /**刷新最新的几条数据*/
    fun notifyInsertedBodySize(size: Int){
        if (size<=0){
            return
        }
        if (mDataList.size+mHeadList.size>=size){
            //刷新的下标
            val notifyIndex = mHeadList.size+mDataList.size-size
            if (notifyIndex<=0){
                notifyDataSetChanged()
            }else{
                notifyItemInserted(notifyIndex)
                notifyItemRangeChanged(notifyIndex,20)
            }
        }else{
            notifyDataSetChanged()
        }
    }

    /**从下标position的位置开始刷新后面的数据*/
    fun notifyInserted(pageNo:Int,position: Int){
        if (pageNo<=1){
            notifyDataSetChanged()
        }else{
            notifyInserted(position)
        }
    }

    /**从下标position的位置开始刷新后面的数据*/
    fun notifyInsertedBody(pageNo:Int,position: Int){
        if (pageNo<=1&&headNum==0){
            notifyDataSetChanged()
        }else{
            notifyInserted(position)
        }
    }

    /**删除某条数据*/
    fun notifyDelete(position: Int){
        if (position<headNum){
            mHeadList.removeAt(position)
        }
        else if(position-headNum<mDataList.size){
            mDataList.removeAt(position-headNum)
        }
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,20)
    }

    fun clear(){
        headNum=0
        mHeadList.clear()
        mDataList.clear()
        notifyDataSetChanged()
    }

    fun clearHead(){
        headNum=0
        mHeadList.clear()
    }

    fun clearBody(){
        mDataList.clear()
        if (headNum<=0){
            notifyDataSetChanged()
        }else{
            notifyInserted(getMaxPosition())
        }
    }

    fun removeAllDateFromIndex(index: Int){
        val list = ArrayList<BaseBean>()
        for(position in index until mDataList.size){
            list.add(mDataList[position])
        }
        mDataList.removeAll(list)
        notifyInserted(getMaxPosition())
    }

    fun notifyData(list:ArrayList<BaseBean>){
        mDataList.clear()
        mDataList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        //为GridLayoutManager类型的adapter设置header
        if(recyclerView.layoutManager is GridLayoutManager){
            (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position : Int): Int {
                    //每个item在布局上占据的单元格 item占据1个 header占据spanCount个
                    return if (position>=mHeadList.size) {
                        if (position-mHeadList.size>=mDataList.size){
                            return (recyclerView.layoutManager as GridLayoutManager).spanCount
                        }else{
                            val bean = mDataList[position-mHeadList.size]
                            if (bean.spanStates){
                                1
                            }else{
                                (recyclerView.layoutManager as GridLayoutManager).spanCount
                            }
                        }

                    }else {
                        (recyclerView.layoutManager as GridLayoutManager).spanCount
                    }
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        lp?.let {
            if (lp is StaggeredGridLayoutManager.LayoutParams ){
                //为StaggeredGridLayoutManager类型的adapter设置header
                if (holder.adapterPosition<mHeadList.size){
                    lp.isFullSpan = true
                }else if (holder.adapterPosition - mHeadList.size <mDataList.size) {
                    val bean = mDataList[holder.adapterPosition - mHeadList.size]
                    if (!bean.spanStates){
                        lp.isFullSpan = true
                    }
                }
            }
        }
    }

    class BaseViewHolder(view: View): RecyclerView.ViewHolder(view){

        var binding: ViewDataBinding?= null

        constructor(binding: ViewDataBinding):this(binding.root){
            this.binding = binding
        }

    }

    interface OnHeadClickListener{
        fun headClick(position: Int,item: BaseBean)
    }

    interface OnItemClickListener{
        fun itemClick(position: Int,item: BaseBean,parent:View)
    }

    interface OnItemRecycledListener{
        fun onItemRecycled(holder: BaseViewHolder)
    }

}