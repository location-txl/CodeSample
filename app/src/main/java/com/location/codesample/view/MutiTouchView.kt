package com.location.codesample.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.location.codesample.R
import com.location.codesample.databinding.ActivityTestMutiTouchBinding

class MutiTouchView @JvmOverloads constructor(
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
        private val TAG = MutiTouchView::class.java.simpleName
    }


    private var trackPointId = 0

    /**
     * 每个 MotionEvent 都是针对于这个 View 的，
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "onTouchEvent pointCount: ${event.pointerCount}")

        when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                trackPointId = event.getPointerId(0)
                Log.d(TAG, "")
                lastTouchX = event.getXByPointId(trackPointId)
                lastTouchY = event.getYByPointId(trackPointId)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                trackPointId = event.getPointerId(event.actionIndex)
                lastTouchX = event.getXByPointId(trackPointId)
                lastTouchY = event.getYByPointId(trackPointId)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val upId = event.getPointerId(event.actionIndex)
                if(upId == trackPointId){
                    val newIndex = if(event.actionIndex == event.pointerCount - 1){
                        event.pointerCount - 2
                    }else{
                        event.pointerCount - 1
                    }
                    trackPointId = event.getPointerId(newIndex)
                    lastTouchX = event.getXByPointId(trackPointId)
                    lastTouchY = event.getYByPointId(trackPointId)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.getXByPointId(trackPointId) - lastTouchX
                val dy = event.getYByPointId(trackPointId) - lastTouchY
                rectf.offset(dx, dy)
                invalidate()
                lastTouchX = event.getXByPointId(trackPointId)
                lastTouchY = event.getYByPointId(trackPointId)
            }
        }
        return true
    }

}

class TestMutiTouchViewActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTestMutiTouchBinding.inflate(layoutInflater) }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.touchView.setOnTouchListener(object : View.OnTouchListener{
            var count = 0
            val touchList = mutableListOf<String>()
            override fun onTouch(
                v: View,
                event: MotionEvent
            ): Boolean {
                when(event.actionMasked){
                    MotionEvent.ACTION_DOWN -> {
                        touchList.clear()
                        count++
                        touchList.add("第 1 根手指按下 id: ${event.getPointerId(0)}")
                        binding.touchText.text = touchList.joinToString("\n")
                    }
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        count++
                        touchList.add("第 ${event.pointerCount} 根手指按下 id: ${event.getPointerId(event.actionIndex)}")
                        binding.touchText.text = touchList.joinToString("\n")

                    }
                    MotionEvent.ACTION_UP -> {
                        count--
                        touchList.add("第 1 根手指抬起 id: ${event.getPointerId(0)}")
                        binding.touchText.text = touchList.joinToString("\n")

                    }
                    MotionEvent.ACTION_POINTER_UP -> {
                        count--
                        touchList.add("第 ${event.pointerCount} 根手指抬起 id: ${event.getPointerId(event.actionIndex)}")
                        binding.touchText.text = touchList.joinToString("\n")
                    }
                }

                return false
            }

        })
    }
}

