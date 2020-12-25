package com.idolmedia.yzymanager.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 *  时间：2020/12/11-10:29
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.widget BoldEditView
 *  描述：
 */
class BoldEditText : AppCompatEditText {

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    override fun onDraw(canvas: Canvas?) {
        val paint = paint
        paint.strokeWidth = 0.75f
        paint.style = Paint.Style.FILL_AND_STROKE
        super.onDraw(canvas)
    }

}