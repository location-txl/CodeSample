package com.location.codesample.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.location.codesample.R

/**
 * 协作性的多点触控
 */
class MutiTouchViewCooperate @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private val bitmap = getBitmap(R.drawable.test, 200.dpView, preciseSize = false)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
    }

    private val rectf = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(rectf, paint)
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectf.set(0f, 0f, w / 2f, h / 2f)
    }

    private var lastTouchX = 0f
    private var lastTouchY = 0f
    companion object{
        private val TAG = MutiTouchViewCooperate::class.java.simpleName
    }



    /**
     * 每个 MotionEvent 都是针对于这个 View 的，
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "onTouchEvent pointCount: ${event.pointerCount}")
        var sumX = 0f
        var sumY = 0f
        val skipIndex = if(event.actionMasked == MotionEvent.ACTION_POINTER_UP){
            event.actionIndex
        }else{
            -1
        }

        for (index in 0 until event.pointerCount){
            if(index == skipIndex) continue
            val x = event.getX(index)
            val y = event.getY(index)
            sumX += x
            sumY += y
        }
        val count = if(event.actionMasked == MotionEvent.ACTION_POINTER_UP){
            event.pointerCount - 1
        }else{
            event.pointerCount
        }
        val focusX = sumX / count
        val focusY = sumY / count

        when(event.actionMasked){
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                lastTouchX = focusX
                lastTouchY = focusY
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = focusX - lastTouchX
                val dy = focusY - lastTouchY
                rectf.offset(dx, dy)
                invalidate()
                lastTouchX = focusX
                lastTouchY = focusY
            }
        }
        return true
    }

}