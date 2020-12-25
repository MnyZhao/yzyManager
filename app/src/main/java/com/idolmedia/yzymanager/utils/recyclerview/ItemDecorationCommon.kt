package com.idolmedia.yzymanager.utils.recyclerview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idolmedia.yzymanager.utils.Dp2PxUtils

/**
 *  时间：2020/10/29-10:28
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.utils.recyclerview ItemDecoratioCommon
 *  描述：
 */
object ItemDecorationCommon {

    enum class OrientationType {
        TOP, RIGHT, BOTTOM,LEFT
    }

    /**下方间隔线*/
    fun addItemDecoration(height:Int,recyclerView: RecyclerView){
        val d = Dp2PxUtils.dip2px(height).toInt()
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = d
            }
        })

    }

    fun addItemDecoration(orientationType:OrientationType,height:Int,recyclerView: RecyclerView){
        val d = Dp2PxUtils.dip2px(height).toInt()
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                when(orientationType){
                    OrientationType.TOP ->  outRect.top = d
                    OrientationType.RIGHT ->  outRect.right = d
                    OrientationType.BOTTOM ->  outRect.bottom = d
                    OrientationType.LEFT ->  outRect.left = d
                }
            }
        })
    }

    /**下方间隔线与颜色*/
    fun addItemDecoration(height:Int,color:Int,recyclerView: RecyclerView){
        val d = Dp2PxUtils.dip2px(height).toInt()
        val paint =  Paint()
        paint.color = ContextCompat.getColor(recyclerView.context,color)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = d
            }

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                val count = parent.childCount
                for(index in 0 until count){
                    val child = parent.getChildAt(index)
                    c.drawRect(child.left.toFloat(),child.bottom.toFloat(),child.right.toFloat(),child.bottom.toFloat()+height,paint)
                }
            }

        })
    }

    /**上方间隔线与颜色*/
    fun addItemDecorationTop(height:Int,color:Int,recyclerView: RecyclerView){
        val d = Dp2PxUtils.dip2px(height).toInt()
        val paint =  Paint()
        paint.color = ContextCompat.getColor(recyclerView.context,color)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = d
            }

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                val count = parent.childCount
                for(index in 0 until count){
                    val child = parent.getChildAt(index)
                    c.drawRect(child.left.toFloat(),child.top.toFloat()-height,child.right.toFloat(),child.top.toFloat(),paint)
                }
            }

        })
    }

}