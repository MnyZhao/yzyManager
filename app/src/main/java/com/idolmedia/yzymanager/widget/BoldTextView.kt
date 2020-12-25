package com.idolmedia.yzymanager.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 *  时间：2020/8/4-17:19
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.widget.text BoldTextView
 *  描述：
 */
class BoldTextView : AppCompatTextView {

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    override fun onDraw(canvas: Canvas?) {
        val paint = paint
        paint.strokeWidth = 0.85f
        paint.style = Paint.Style.FILL_AND_STROKE
        super.onDraw(canvas)
    }

}