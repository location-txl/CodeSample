package com.location.codesample.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.PathEffect
import android.graphics.PathMeasure
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.withTranslation
import com.location.codesample.databinding.ActivityPathTestBinding
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class PathView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    val path = Path()
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }

    var pathMeasure: PathMeasure? = null

    private var pathProvider: PathProvider = PieChart(this)

    override fun onDraw(canvas: Canvas) {

//        canvas.drawPath(path, paint)
        pathProvider.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pathProvider.onSizeChange()
//        testPath(w, h)
    }

    private fun testPath(w: Int, h: Int) {
        with(path) {
            reset()
            /**
             * 最后一个参数 代表是顺时针画还是逆时针画
             */
            val radius = 200f
            addCircle(w / 2f, h / 2f, 200f, Path.Direction.CCW)
            val l = w / 2 - radius
            val top = h / 2f
            addRect(l, top, l + radius * 2, h / 2f + radius * 2, Path.Direction.CCW)
            /**
             * [Path.FillType.WINDING] 默认的填充类型， 类比与螺旋网上的楼梯 绕线
             *                          从一个点射出一条线 遇到相交的点 向左转就+1 向右转就-1
             *                          最终加的值不为0 就是内部 否则为外部 外部不填充
             * [Path.FillType.EVEN_ODD]  不看方向 奇数内部 偶数外部， 类比与螺旋网上的楼梯 绕线 每遇到一个交点就+1
             */
            fillType = Path.FillType.EVEN_ODD
            /**
             * 第一个参数 代表是否强制封口
             */
            pathMeasure = PathMeasure(this, false).also {
                it.length // 计算长度
                it.getPosTan(it.length, null, null) // 计算切角
                it.getSegment(0f, it.length, null, true) // 计算截取路径
                it.nextContour() // 下一个轮廓
                it.getSegment(0f, it.length, null, true) // 计算截取路径
//                it.getSegment()
            }
        }
    }


    sealed class PathProvider(val view: View) {
        abstract fun onDraw(canvas: Canvas)
        abstract fun onSizeChange()
    }

    class Dashboard(view: View) : PathProvider(view) {
        companion object {
            private val DASH_WIDTH = 2.dpView.toFloat()
            private val DASH_LENGTH = 10.dpView.toFloat()
            private const val OPEN_ANGLE = 120f
            private const val START_ANGLE = 90 + OPEN_ANGLE / 2f
            private const val SWIPE_ANGLE = 360 - OPEN_ANGLE

        }

        private val dashPath = Path()
        private val path = Path()
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 3.dpView.toFloat()
        }

        private var dashEffect: PathEffect? = null
        private var pathMeasure: PathMeasure? = null
        override fun onDraw(canvas: Canvas) {


            paint.pathEffect = null
            canvas.drawPath(path, paint)
            paint.pathEffect = dashEffect
            canvas.drawPath(path, paint)
            paint.pathEffect = null
            val radians = Math.toRadians((START_ANGLE + SWIPE_ANGLE / 20 * 5).toDouble())
            canvas.drawLine( centerX, centerY,
                centerX + lineLength * cos(radians).toFloat(),
                centerY + lineLength * sin(radians).toFloat(),  paint)

        }

        private var centerX = 0f
        private var centerY = 0f
        private var radius = 0
        private var lineLength = 0f
        private var dashRectF = RectF()
        override fun onSizeChange() {
            centerX = view.width / 2f
            centerY = view.height / 2f
            with(path) {
                reset()
                radius = min(view.width, view.height) / 2 - 100.dpView
                lineLength = radius * 0.8f
                val left = centerX - radius
                val top = centerY - radius
                val right = centerX + radius
                val bottom = centerY + radius
                dashRectF.set(
                    left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat()
                )
                addArc(dashRectF, START_ANGLE, SWIPE_ANGLE)
                pathMeasure = PathMeasure(this, false)
            }
            with(dashPath) {
                reset()
                addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CCW)
                dashEffect = PathDashPathEffect(
                    dashPath,
                    (pathMeasure!!.length - DASH_WIDTH ) / 20/* - DASH_WIDTH*/,
                    0f,
                    PathDashPathEffect.Style.ROTATE
                )
            }

        }


    }


    class PieChart(view: View) : PathProvider(view) {
        private val paint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.GREEN
        }
        private val paint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLUE
        }
        private val paint1Angle = 220f
        private val paint2Angle = 360 - paint1Angle
        private val paint2Radians = Math.toRadians(paint2Angle / 2.toDouble())

        private val LENGTH = 15.dpView
        override fun onDraw(canvas: Canvas) {
            canvas.drawArc(dashRectF, 0f, paint1Angle, true, paint1)

            canvas.withTranslation(LENGTH * cos(paint2Radians).toFloat(), -LENGTH * sin(paint2Radians).toFloat()){
                canvas.drawArc(dashRectF, paint1Angle, paint2Angle, true, paint2)
            }

        }
        private var dashRectF = RectF()

        override fun onSizeChange() {

           val  radius = min(view.width, view.height) / 2 - 100.dpView
            val left = view.width/2 - radius
            val top = view.height/2 - radius
            val right = view.width/2 + radius
            val bottom = view.height/2 + radius
            dashRectF.set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
        }


    }
}


val Int.dpView: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dpView: Float
    get() = this.toInt().dpView.toFloat()


val Int.spView: Int
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity + 0.5f).toInt()


class PathActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPathTestBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}