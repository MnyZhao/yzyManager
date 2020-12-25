package com.idolmedia.yzymanager.widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import com.idolmedia.yzymanager.R

/**
 *  时间：2019/9/25-17:53
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.widget MaxHeightRecyclerView
 *  描述：
 */
class MaxHeightRecyclerView : RecyclerView {

    private var mMaxHeight = 0

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
        initialize(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){
        initialize(context, attributeSet)
    }

    @SuppressLint("Recycle")
    private fun initialize(context:Context, attributeSet: AttributeSet){
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.MaxHeightRecyclerView)
        mMaxHeight = array.getLayoutDimension(R.styleable.MaxHeightRecyclerView_maxHeight, mMaxHeight)
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        if (mMaxHeight>0){
            val height = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST)
            super.onMeasure(widthSpec, height)
        }else{
            super.onMeasure(widthSpec, heightSpec)
        }
    }



}