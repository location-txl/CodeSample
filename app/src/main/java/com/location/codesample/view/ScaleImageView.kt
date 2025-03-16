package com.location.codesample.view

import android.R.attr.bitmap
import android.R.attr.height
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.input.key.Key.Companion.G
import androidx.compose.ui.input.key.Key.Companion.K
import androidx.core.animation.doOnEnd
import androidx.core.graphics.withSave
import androidx.core.graphics.withScale
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat.postOnAnimation
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.location.codesample.R
import com.location.codesample.databinding.ActivityTestScaleImageBinding
import com.location.codesample.databinding.ActivityTextTestBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class ScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes){
    private val bitmap = getBitmap(R.drawable.test, 200.dpView, preciseSize = false)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var offsetX = 0f
    private var offsetY = 0f
    private var isFling = false
    private var touchOffsetX = 0f
        set(value) {
            field = if(isFling) value else value.coerceIn(-maxTouchOffsetX, maxTouchOffsetX)
        }
    private var touchOffsetY = 0f
        set(value) {
            field = if(isFling) value else value.coerceIn(-maxTouchOffsetY, maxTouchOffsetY)
        }
    private var maxTouchOffsetX = 0f
    private var maxTouchOffsetY = 0f
    private var smallScale = 0f
    private var bigScale = 0f
    private var big = false
    private val scroller = OverScroller(context) //处理惯性滑动
    private var scaleFraction = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val scaleFractionAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 200
        addUpdateListener {
            scaleFraction = it.animatedValue as Float
        }
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val scale =  smallScale + (bigScale - smallScale) * scaleFraction
        canvas.withSave {
            translate(touchOffsetX * scaleFraction, touchOffsetY * scaleFraction)
            scale(scale, scale, width/2f, height/2f)
            drawBitmap(bitmap, offsetX, offsetY, paint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        offsetX = (width - bitmap.width) / 2f
        offsetY = (height - bitmap.height) / 2f

        if(bitmap.width /  bitmap.height.toFloat() > width/height.toFloat()){
            //图片大
            smallScale = w / bitmap.width.toFloat()
            bigScale = h / bitmap.height.toFloat()
        }else{
            //图片小
            smallScale = h / bitmap.height.toFloat()
            bigScale = w / bitmap.width.toFloat()
        }
        bigScale *= 3f

        maxTouchOffsetX = (bitmap.width * bigScale - width) / 2f
        maxTouchOffsetY = (bitmap.height * bigScale - height) / 2f
    }

    private val scaleGesture = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener(){
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFraction  = detector.scaleFactor
            return super.onScale(detector)
        }
    })
    private val touchGesture = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            Log.d("TAG", "onDoubleTap: ")
            big = !big
            if(big){
                Log.d("tt", "bigScale / smallScale: ${1- bigScale / smallScale}")
                /**
                 * 缩放后，图片的坐标系会改变，所以要计算出图片的坐标系中的偏移量
                 * 如果点击的右边 那么放大后点击的点在中间 那么偏移应该是负的 图片要向左边移动
                 */
                touchOffsetX = (e.x - width/2f) * (1 - bigScale / smallScale)
                touchOffsetY = (e.y - height/2f) * (1 - bigScale / smallScale)
                scaleFractionAnimator.start()
            }else{
                scaleFractionAnimator.reverse()
            }
            return true

        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if(big){
                touchOffsetX -= distanceX
                touchOffsetY -= distanceY
                invalidate()
            }


            return true
        }

        private fun refresh(){
            if(big){
                val update = object : Runnable{
                    override fun run() {
                        if(scroller.computeScrollOffset()){
                            isFling = true
                            touchOffsetX = scroller.currX.toFloat()
                            touchOffsetY = scroller.currY.toFloat()
                            isFling = false
                            invalidate()
                            postOnAnimation(this)
                        }
                    }
                }
                postOnAnimation(update)
            }
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if(big){
                /**
                 *  startX 是 惯性滑动的起始位置
                 *  startY 是 惯性滑动的起始位置
                 *  velocityX 是 惯性滑动的起始速度
                 *  velocityY 是 惯性滑动的起始速度
                 *  minX 是 惯性滑动的结束位置
                 *  minY 是 惯性滑动的结束位置
                 *  maxX 是 惯性滑动的结束位置
                 *  maxY 是 惯性滑动的结束位置
                 *
                 *  使用 touchOffsetX 和 touchOffsetY 来控制图片的位置
                 */
                scroller.fling(touchOffsetX.toInt(), touchOffsetY.toInt(), velocityX.toInt(), velocityY.toInt(),
                    -maxTouchOffsetX.toInt(), maxTouchOffsetX.toInt(),
                    -maxTouchOffsetY.toInt(), maxTouchOffsetY.toInt(),
                    100.dpView, 100.dpView
                    )
                refresh()
            }
            return super.onFling(e1, e2, velocityX, velocityY)
        }
    })

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return touchGesture.onTouchEvent(event)
    }

}


class TestScaleImageViewActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTestScaleImageBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}