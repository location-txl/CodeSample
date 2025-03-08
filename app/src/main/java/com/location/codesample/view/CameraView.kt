package com.location.codesample.view

import android.R.attr.rotationY
import android.animation.AnimatorSet
import android.animation.FloatEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.telecom.Call.Details.can
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnRepeat
import androidx.core.graphics.withSave
import com.location.codesample.R
import com.location.codesample.databinding.ActivityCameraViewTestBinding

class CameraView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val imageWidth = 200.dpView

    private val bitmap = getBitmap(R.drawable.img, imageWidth)


    /**
     * 朝屏幕里面是正向
     */
    private val camera = Camera().apply {
        //单位是英寸 z轴默认-8
//        setLocation(0f,0f,-8f * resources.displayMetrics.density)
        //--------------------------------------------------


    }

    override fun onDraw(canvas: Canvas) {
        //反着看 绘制好的图片之后才做的变换
//        canvas.translate(imageWidth/2f, imageWidth/2f)
//        camera.applyToCanvas(canvas)
//        canvas.translate(-imageWidth/2f, -imageWidth/2f)
//        canvas.drawBitmap(bitmap, 0f, 0f, null)
//        ---------------------------

//        下半部分
        canvas.withSave {
            canvas.translate(imageWidth / 2f, imageWidth / 2f)
            canvas.rotate(-rotateAngle, 0f,0f)
            camera.withSave {
                this.rotateX(bottomAngle)
                setLocation(0f,0f,-8f * resources.displayMetrics.density)
                camera.applyToCanvas(canvas)
            }

            canvas.clipRect(-imageWidth.toFloat(), 0f, imageWidth.toFloat(), imageWidth.toFloat())
            canvas.rotate(rotateAngle, 0f,0f)
            canvas.translate(-imageWidth / 2f, -imageWidth / 2f)
            canvas.drawBitmap(bitmap, 0f, 0f, null)
        }

//        canvas.withSave {
//            canvas.translate(imageWidth / 2f, imageWidth / 2f)
//            canvas.rotate(-rotateAngle, 0f,0f)
//            camera.withSave {
//                this.rotateX(topAngle)
//                setLocation(0f,0f,-8f * resources.displayMetrics.density)
//                camera.applyToCanvas(canvas)
//            }
//            canvas.clipRect(-imageWidth/1f, -imageWidth/1f, imageWidth/1f, 0f)
//            canvas.rotate(rotateAngle, 0f,0f)
//            canvas.translate(-imageWidth / 2f, -imageWidth / 2f)
//            canvas.drawBitmap(bitmap, 0f, 0f, null)
//        }
    }

    var topAngle: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    var bottomAngle: Float = 30f
        set(value) {
            field = value
            invalidate()
        }

    var rotateAngle: Float = 20f
        set(value) {
            field = value
            invalidate()
        }


}

class CameraViewActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCameraViewTestBinding.inflate(layoutInflater) }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btn.setOnClickListener {
            val animatorTopAngle = ObjectAnimator.ofFloat(binding.xferModeView, "topAngle", 0f, -60f)
            val animatorBottomAngle = ObjectAnimator.ofFloat(binding.xferModeView, "bottomAngle", 0f, 60f)
            val animatorRotateAngle = ObjectAnimator.ofFloat(binding.xferModeView, "rotateAngle", 0f, 270f)
            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(animatorBottomAngle, animatorRotateAngle)
            animatorSet.duration = 10000
            animatorSet.reverse()
            animatorSet.start()
        }

    }
}


inline fun Camera.withSave(block: Camera.() -> Unit){
    save()
    try {
        block()
    }finally {
        restore()
    }

}