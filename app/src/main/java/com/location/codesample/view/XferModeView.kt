package com.location.codesample.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.AttributeSet
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withSave
import androidx.core.graphics.withTranslation
import com.location.codesample.databinding.ActivityXfermodeTestBinding
import com.location.codesample.R
import com.location.codesample.composeCommon.SelectView

class XferModeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
): View(context, attrs, defStyleAttr, defStyleRes) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }

    private val imageWidth = 150.dpView
    private val bitmap = getBitmap(R.drawable.img, imageWidth)
    private val imagePadding = 10.dpView



    var mode: PorterDuff.Mode = PorterDuff.Mode.DST_ATOP
        set(value) {
            field = value
            uiMode = PorterDuffXfermode(field)
            invalidate()
        }

    private var uiMode = PorterDuffXfermode(mode)




    private val destPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#ec407a")
    }
    private val srctPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1e88e5")
    }

    override fun onDraw(canvas: Canvas) {
        canvas.withSave {
            translate(imagePadding.toFloat(), imagePadding.toFloat())
            val count = saveLayer(0f, 0f, imageWidth.toFloat(), imageWidth.toFloat(), null)
            canvas.drawOval(0f, 0f, imageWidth.toFloat(), imageWidth.toFloat(), paint)
            paint.xfermode = uiMode
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
            paint.xfermode = null
            canvas.restoreToCount(count)
        }

        testXfermode(canvas)


    }

    private val bitmaps by lazy {
        buildList {
            val centerX = 250.dpView
            val centerY = 60.dpView
            val radius = 50.dpView
            val srcL = centerX - radius * 2
            val srcB = centerY + radius * 2

            val destBitmap = Bitmap.createBitmap(centerX + radius - srcL,
                srcB - (centerY - radius), Bitmap.Config.ARGB_8888)
            add(destBitmap)
            val sourceBitmap = Bitmap.createBitmap(centerX + radius - srcL,
                srcB - (centerY - radius), Bitmap.Config.ARGB_8888)
            add(sourceBitmap)
            val canvas = Canvas(destBitmap)
            canvas.drawCircle(radius*2f, radius.toFloat(), radius.toFloat(), destPaint)
            canvas.setBitmap(sourceBitmap)
            canvas.drawRect(0f,radius.toFloat(),radius*2f, radius*3f, srctPaint)
        }
    }

    private fun testXfermode(canvas: Canvas) {
        canvas.withTranslation(imagePadding.toFloat(), imagePadding.toFloat() + imageWidth) {
            val centerX = 250.dpView
            val centerY = 60.dpView
            val radius = 50.dpView
            val srcL = centerX - radius * 2
            val srcB = centerY + radius * 2
            withSaveLayer(
                srcL.toFloat(),
                (centerY - radius).toFloat(),
                (centerX + radius).toFloat(),
                srcB.toFloat(),
                null
            ) {
                /**
                 * https://www.runoob.com/w3cnote/android-tutorial-xfermode-porterduff.html
                 * 这里要用 bitmap 测试
                 * 因为xfermode 是对当前绘制区域的融合
                 * 如果用drawRect drawCircle测试则只会融合绘制的区域
                 */

                canvas.drawBitmap(bitmaps[0],srcL.toFloat(),(centerY - radius).toFloat(), srctPaint)
                srctPaint.xfermode = uiMode
                canvas.drawBitmap(bitmaps[1],srcL.toFloat(),(centerY - radius).toFloat(), srctPaint)
                srctPaint.xfermode = null
            }
        }
    }




}

fun View.getBitmap(resId: Int, width: Int, preciseSize: Boolean = true): Bitmap{
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(resources, resId, options)
    with(options){
        inJustDecodeBounds = false
        inDensity = this.outWidth
        inTargetDensity = width
    }
    return BitmapFactory.decodeResource(resources, resId, options).let {
        if(preciseSize){
            Bitmap.createScaledBitmap(it, width, width, true)
        }else{
            it
        }
    }
}

class XferModelActivity : AppCompatActivity() {
    private val binding by lazy { ActivityXfermodeTestBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.composeView.setContent {
            var model by remember { mutableStateOf<PorterDuff.Mode>(PorterDuff.Mode.DST_ATOP) }
            Column {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(PorterDuff.Mode.entries.toTypedArray()){

                        SelectView(
                            modifier = Modifier.height(50.dp)
                                .fillMaxWidth(),
                            text = it.name,
                            selected = model == it,
                            onSelect  = {
                                model = it
                                binding.xferModeView.mode = model
                            }
                        )
                    }
                }
            }
        }
    }
}

inline fun Canvas.withSaveLayer(left: Float, top: Float, right: Float, bottom: Float,
                                paint: Paint? = null,
                                block: Canvas.() -> Unit) {
    val count = saveLayer(null, null)
    try {
        saveLayer(left, top, right, bottom, paint)
        block()
    }finally {
        restoreToCount(count)
    }
}
