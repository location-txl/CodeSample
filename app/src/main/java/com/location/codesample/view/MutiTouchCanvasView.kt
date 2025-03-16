package com.location.codesample.view

import android.R.attr.path
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MutiTouchCanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes){

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 3f.dpView
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    private val pathMap = mutableMapOf<Int, Path>()

    override fun onDraw(canvas: Canvas) {
        for (entry in pathMap) {
            canvas.drawPath(entry.value, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when(event.actionMasked){
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val path = Path()
                pathMap[event.getPointerId(event.actionIndex)] = path
                path.moveTo(event.getX(event.actionIndex), event.getY(event.actionIndex))
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                pathMap.forEach { (id, path) ->
                    path.lineTo(event.getXByPointId(id) , event.getYByPointId(id))
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                pathMap.clear()
                invalidate()
            }
            MotionEvent.ACTION_POINTER_UP -> {
                pathMap.remove(event.getPointerId(event.actionIndex))
                invalidate()
            }
        }
        return true
    }


}