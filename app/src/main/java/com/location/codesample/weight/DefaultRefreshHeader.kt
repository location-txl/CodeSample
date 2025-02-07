package com.location.codesample.weight

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.progressindicator.CircularProgressIndicator

/**
 * 默认的刷新头视图
 * 包含一个圆形进度条和文字提示
 */
class DefaultRefreshHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    /** 进度条 */
    private val progressBar: CircularProgressIndicator
    /** 提示文本 */
    private val textView: TextView
    /** 旋转动画 */
    private var rotateAnimator: ValueAnimator? = null
    /** 当前进度 */
    private var currentProgress = 0f
    
    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        
        // 设置内边距
        val padding = (16 * resources.displayMetrics.density).toInt()
        setPadding(padding, padding, padding, padding)

        // 设置默认背景色
        setBackgroundColor(Color.WHITE)
        
        // 设置最小高度
        minimumHeight = (64 * resources.displayMetrics.density).toInt()
        
        // 创建进度条
        progressBar = CircularProgressIndicator(context).apply {
            isIndeterminate = false
            setIndicatorColor(Color.GRAY)
            trackCornerRadius = 2
            indicatorSize = (32 * resources.displayMetrics.density).toInt()
            trackThickness = (2 * resources.displayMetrics.density).toInt()
            progress = 0
        }
        
        // 创建文本视图
        textView = TextView(context).apply {
            setTextColor(Color.GRAY)
            textSize = 14f
            text = "下拉刷新"
        }
        
        // 添加视图
        addView(progressBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        addView(textView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            marginStart = (8 * resources.displayMetrics.density).toInt()
        })
    }

    /**
     * 更新下拉进度
     * @param pullDistance 下拉距离
     * @param refreshThreshold 刷新阈值
     */
    fun onPullProgress(pullDistance: Float, refreshThreshold: Float) {
        currentProgress = (pullDistance / refreshThreshold * 100).coerceIn(0f, 100f)
        progressBar.progress = currentProgress.toInt()
        
        // 更新提示文本
        textView.text = if (currentProgress >= 100) "释放刷新" else "下拉刷新"
    }

    /**
     * 开始刷新动画
     */
    fun onStartRefresh() {
        textView.text = "正在刷新..."
        progressBar.isIndeterminate = true
        
        // 停止之前的动画
        rotateAnimator?.cancel()
    }

    /**
     * 结束刷新
     */
    fun onFinishRefresh() {
        progressBar.isIndeterminate = false
        progressBar.progress = 0
        textView.text = "下拉刷新"
        
        // 停止动画
        rotateAnimator?.cancel()
        rotateAnimator = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        rotateAnimator?.cancel()
        rotateAnimator = null
    }
} 