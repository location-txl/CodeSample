package com.location.codesample.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.location.codesample.databinding.ActivityMaterialEdittextBinding
import kotlin.apply

class MaterialEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
    private var labelSize = 12.dpView
    private var labelMargin = 8.dpView
    var labelFraction = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var showLabel = true

    init {
        setPadding(
            paddingLeft,
            paddingTop + labelSize + labelMargin,
            paddingRight,
            paddingBottom
        )
        showLabel = text.isNullOrEmpty().not()
        labelFraction = if(showLabel) 1f else 0f
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = labelSize.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.alpha  = (labelFraction * 0xff).toInt()
        canvas.drawText(hint.toString(), 0f,10f.dpView, paint)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

        if(showLabel && text.isNullOrEmpty()){
            showLabel = false
            val animator = ObjectAnimator.ofFloat(this, "labelFraction", 0f)
            animator.start()
        }else if(!showLabel && text.isNullOrEmpty().not()){
            showLabel = true
            val animator = ObjectAnimator.ofFloat(this, "labelFraction", 1f)
            animator.start()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        MotionEvent.ACTION_UP
        return super.onTouchEvent(event)
    }
}

class MaterialEditTextActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMaterialEdittextBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}