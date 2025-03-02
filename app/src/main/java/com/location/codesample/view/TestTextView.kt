package com.location.codesample.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.text.Layout.Alignment
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log.w
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.withTranslation
import com.location.codesample.R
import com.location.codesample.databinding.ActivityTextTestBinding
import kotlin.math.abs
import kotlin.math.min

class TestTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object{
        private const val TEXT = "aAbgp"
    }

    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var radius: Float = 0f
    private val strokeSize = 15.dpView.toFloat()
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1e88e5")
        style = Paint.Style.STROKE
        strokeWidth = strokeSize


    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#ce93d8")
        style = Paint.Style.STROKE
        strokeWidth = strokeSize
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 75.dpView.toFloat()
        //是否是假的粗体
//        isFakeBoldText
        textAlign = Paint.Align.CENTER
    }

    private val textBonds = Rect()
    private val textMetrics = Paint.FontMetrics()
    //lorem ipsum
    private var staticText = "Lorem ipsum odor amet, consectetuer adipiscing elit. Elit platea facilisi consectetur ultrices penatibus sodales arcu tempus. Leo ad etiam id mus, senectus dapibus lobortis. Nisi morbi aliquam dictum ut interdum suspendisse. Non taciti lobortis viverra adipiscing amet at vel facilisi! Sapien phasellus a sit, hac amet duis interdum senectus. Montes tincidunt maximus nam sapien accumsan scelerisque ipsum sollicitudin? Dignissim efficitur curabitur posuere mollis cubilia congue faucibus."

    private val staticTextPaint = TextPaint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        textSize = 20.dpView.toFloat()
    }
    private var staticLayout = StaticLayout.Builder.obtain(
        staticText,
        0,
        staticText.length,
        staticTextPaint,
        width
    ).build()

    private val bitmap = getBitmap(R.drawable.img, 100.dpView)
    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, radius, bgPaint)
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, 0f, 180f, false, progressPaint)
        canvas.drawLine(0f, centerY.toFloat(), width.toFloat(), centerY.toFloat(), textPaint)
        //文字绘制y点是baseline
        /**
         * 获取到的是文字相对于绘制点的区域 底部是baseline
         *
         */
        textPaint.getTextBounds(TEXT, 0, TEXT.length, textBonds)
        textPaint.getFontMetrics(textMetrics)

        /**
         * 静态文字适合用 textBounds
         * 动态文字适合用 fontMetrics
         */
//        canvas.drawText(TEXT, centerX, centerY - (textBonds.top + textBonds.bottom) / 2, textPaint)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText(TEXT, centerX, centerY - (textMetrics.ascent + textMetrics.descent) / 2, textPaint)
        canvas.drawLine(0f, textMetrics.ascent + centerY, width.toFloat(), textMetrics.ascent + centerY, textPaint)

        canvas.withTranslation(10f.dpView, 20f.dpView){
            canvas.drawLine(0f,0f,width.toFloat(),0f, textPaint)
            canvas.drawLine(0f,0f,0f,height.toFloat(), textPaint)
            textPaint.textAlign = Paint.Align.LEFT
            /**
             * 足够精确用 getTextBonds
             * 美观用 fontMetrics
             */
            canvas.drawText(TEXT, 0f,0f - textMetrics.top, textPaint)
        }
        staticLayout.draw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = min(width, height) / 2f - 100.dpView
        staticLayout = StaticLayout.Builder.obtain(
            staticText,
            0,
            staticText.length,
            staticTextPaint,
            width
        )
            .setAlignment(Alignment.ALIGN_NORMAL)
            .build()

    }

}


class MutiLineTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 20.dpView.toFloat()
    }
    val text = "Lorem ipsum odor amet, consectetuer adipiscing elit. Elit platea facilisi consectetur ultrices penatibus sodales arcu tempus. Leo ad etiam id mus, senectus dapibus lobortis. Nisi morbi aliquam dictum ut interdum suspendisse. Non tac"

    private val fontMeasure = floatArrayOf(0f)
    private val bitmapSize = 70.dpView
    private val bitmapMarginTop = 30.dpView
    private val bitmap = getBitmap(R.drawable.img, bitmapSize)

    private val fontMetrics = Paint.FontMetrics()
    override fun onDraw(canvas: Canvas) {

        canvas.drawBitmap(bitmap, width.toFloat() - bitmap.width, bitmapMarginTop.toFloat(), textPaint)
        textPaint.getFontMetrics(fontMetrics)
        var startIndex = 0
        var count = 0
        var offset = 0 - fontMetrics.top
        while (startIndex < text.length) {
            val maxWidth = if(
                offset + fontMetrics.bottom < bitmapMarginTop
                || offset  + fontMetrics.top > bitmapMarginTop + bitmapSize
            ) {
                width.toFloat()
            } else {
                width.toFloat() - bitmap.width
            }
            count = textPaint.breakText(
                text,
                startIndex,
                text.length,
                true,
                maxWidth,
                fontMeasure
            )
            canvas.drawText(text, startIndex, startIndex + count, 0f, offset, textPaint)
            startIndex += count
            offset += textPaint.fontSpacing
        }
    }
}


class TestTextViewActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTextTestBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}