package com.example.leftovers.barcodeAnalyzer.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View

class BarcodeBoxView(
    context: Context
) : View(context) {

    private val paint = Paint()

    private var mRect = RectF()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val cornerRadius = 7f

        paint.style = Paint.Style.STROKE
        paint.color = Color.GREEN
        paint.strokeWidth = 10f


        canvas?.drawRoundRect(mRect, cornerRadius, cornerRadius, paint)
    }

    fun setRect(rect: RectF) {
        mRect = rect
        invalidate()
        requestLayout()
    }
}