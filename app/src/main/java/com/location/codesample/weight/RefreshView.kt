package com.location.codesample.weight

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ScrollView
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * 一个支持嵌套滚动的下拉刷新控件
 * 
 * 特点：
 * 1. 支持嵌套滚动，可以配合 RecyclerView、ScrollView 等可滚动控件使用
 * 2. 支持自定义刷新头
 * 3. 支持下拉阈值和最大下拉距离的配置
 * 4. 带有阻尼效果的下拉体验
 * 
 * 使用方法：
 * ```
 * <RefreshView>
 *     <!-- 内容视图，如 RecyclerView -->
 *     <RecyclerView/>
 *     <!-- 可选的刷新头 -->
 *     <YourRefreshHeader/>
 * </RefreshView>
 * ```
 * 
 * @constructor 创建 RefreshView 实例
 * @param context 上下文
 * @param attrs 属性集
 * @param defStyleAttr 默认样式属性
 * @param defStyleRes 默认样式资源
 *
 *
 * 嵌套滚动机制：
 * NestedScrollingParent3 接口实现
 * 嵌套滚动的生命周期：
 * onStartNestedScroll: 开始滚动
 * onNestedScrollAccepted: 接受滚动
 * onNestedScroll: 处理滚动
 * onNestedPreScroll: 预处理滚动
 * onStopNestedScroll: 停止滚动
 */
class RefreshView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes), NestedScrollingParent3 {

    /**
     * 刷新状态枚举
     */
    enum class RefreshState {
        /** 空闲状态 */
        IDLE,
        /** 正在下拉状态 */
        PULLING,
        /** 正在刷新状态 */
        REFRESHING
    }

    // 当前刷新状态
    private var refreshState = RefreshState.IDLE
    
    /** 下拉阈值，超过这个值才会触发刷新 */
    private val refreshThreshold = 200f
    
    /** 最大下拉距离，防止过度下拉 */
    private val maxPullDistance = 400f
    
    /** 当前下拉的距离 */
    private var currentPullDistance = 0f
    
    /** 上次触摸事件的Y坐标 */
    private var lastTouchY = 0f
    
    /** 刷新回调监听器 */
    private var onRefreshListener: OnRefreshListener? = null
    
    /** 下拉阻尼系数，使下拉有阻尼感 */
    private val dampingCoefficient = 0.5f
    
    /** 嵌套滚动帮助类，用于处理嵌套滚动事件 */
    private val parentHelper = NestedScrollingParentHelper(this)

    /** 默认的刷新头 */
    private var defaultRefreshHeader: DefaultRefreshHeader? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        // 在所有子View添加完成后，如果只有一个子View（内容视图），则添加默认刷新头
        if (childCount == 1) {
            defaultRefreshHeader = DefaultRefreshHeader(context)
            addView(defaultRefreshHeader, LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ))
        }
    }

    /**
     * 测量视图大小
     * 规则：
     * 1. 最多支持两个子View（内容视图和刷新头）
     * 2. 宽高取所有子View中的最大值
     * 3. 考虑padding的影响
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        require (childCount <= 2) { "RefreshView can host only up to 2 direct children" }
        
        var maxHeight = 0
        var maxWidth = 0

        measureChildren(widthMeasureSpec, heightMeasureSpec)
        
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            maxWidth = maxWidth.coerceAtLeast(child.measuredWidth)
            maxHeight = maxHeight.coerceAtLeast(child.measuredHeight)
        }
        
        maxWidth += paddingLeft + paddingRight
        maxHeight += paddingTop + paddingBottom
        
        setMeasuredDimension(
            resolveSize(maxWidth, widthMeasureSpec),
            resolveSize(maxHeight, heightMeasureSpec)
        )
    }

    /**
     * 布局子视图
     * 规则：
     * 1. 内容视图位置会根据下拉距离向下偏移
     * 2. 刷新头位置在内容视图上方，也会随下拉距离移动
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) return
        
        val contentView = getChildAt(0)
        contentView.layout(
            paddingLeft,
            paddingTop + currentPullDistance.toInt(),
            paddingLeft + contentView.measuredWidth,
            paddingTop + currentPullDistance.toInt() + contentView.measuredHeight
        )
        
        if (childCount > 1) {
            val refreshHeader = getChildAt(1)
            refreshHeader.layout(
                paddingLeft,
                paddingTop - refreshHeader.measuredHeight + currentPullDistance.toInt(),
                paddingLeft + refreshHeader.measuredWidth,
                paddingTop + currentPullDistance.toInt()
            )
        }
    }

    /**
     * 拦截触摸事件
     * 条件：
     * 1. 不在刷新状态
     * 2. 是下拉动作
     * 3. 内容已经滚动到顶部
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (refreshState == RefreshState.REFRESHING) return false
        
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchY = ev.y
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaY = ev.y - lastTouchY
                return deltaY > 0 && !canChildScroll()
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 处理触摸事件
     * 1. 处理下拉动作
     * 2. 处理手指抬起时的刷新触发判断
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (refreshState == RefreshState.REFRESHING) return false
        
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val deltaY = event.y - lastTouchY
                handlePull(deltaY)
                lastTouchY = event.y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (refreshState == RefreshState.PULLING) {
                    if (currentPullDistance >= refreshThreshold) {
                        startRefreshing()
                    } else {
                        resetPullDistance()
                    }
                }
            }
        }
        return true
    }

    /**
     * 处理下拉动作
     * 1. 应用阻尼效果
     * 2. 限制最大下拉距离
     * 3. 更新刷新状态
     * @param deltaY 下拉的距离
     */
    private fun handlePull(deltaY: Float) {
        if (deltaY > 0 || currentPullDistance > 0) {
            currentPullDistance += deltaY * dampingCoefficient
            currentPullDistance = currentPullDistance.coerceIn(0f, maxPullDistance)
            refreshState = RefreshState.PULLING
            // 更新刷新头进度
            defaultRefreshHeader?.onPullProgress(currentPullDistance, refreshThreshold)
            requestLayout()
        }
    }

    /**
     * 检查子View是否可以继续向上滚动
     * @return true 表示可以继续向上滚动，false 表示已到达顶部
     */
    private fun canChildScroll(): Boolean {
        if (childCount == 0) return false
        val child = getChildAt(0)
        return child.canScrollVertically(-1)
    }

    /**
     * 开始刷新
     * 1. 更新状态为刷新中
     * 2. 展示刷新动画
     * 3. 回调刷新监听器
     */
    private fun startRefreshing() {
        refreshState = RefreshState.REFRESHING
        defaultRefreshHeader?.onStartRefresh()
        animatePullDistance(refreshThreshold)
        onRefreshListener?.onRefresh()
    }

    /**
     * 重置下拉距离
     * 1. 更新状态为空闲
     * 2. 执行回弹动画
     */
    private fun resetPullDistance() {
        refreshState = RefreshState.IDLE
        defaultRefreshHeader?.onFinishRefresh()
        animatePullDistance(0f)
    }

    /**
     * 执行下拉距离的动画
     * @param targetDistance 目标距离
     */
    private fun animatePullDistance(targetDistance: Float) {
        ValueAnimator.ofFloat(currentPullDistance, targetDistance).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
            addUpdateListener { 
                currentPullDistance = it.animatedValue as Float
                requestLayout()
            }
            start()
        }
    }

    /**
     * 完成刷新，重置控件状态
     */
    fun finishRefresh() {
        if (refreshState == RefreshState.REFRESHING) {
            resetPullDistance()
        }
    }

    /**
     * 设置刷新监听器
     * @param listener 刷新监听器
     */
    fun setOnRefreshListener(listener: OnRefreshListener) {
        onRefreshListener = listener
    }

    /**
     * 刷新监听器接口
     */
    interface OnRefreshListener {
        /**
         * 当触发刷新时回调
         */
        fun onRefresh()
    }

    // 以下是嵌套滚动相关的实现 NestedScrollingParent3 的方法

    /**
     * 是否接受嵌套滚动
     * @return true 表示接受垂直方向的嵌套滚动
     */
    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    /**
     * 接受嵌套滚动
     */
    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        parentHelper.onNestedScrollAccepted(child, target, axes, type)
    }

    /**
     * 停止嵌套滚动
     * 处理是否需要触发刷新
     */
    override fun onStopNestedScroll(target: View, type: Int) {
        parentHelper.onStopNestedScroll(target, type)
        if (refreshState == RefreshState.PULLING) {
            if (currentPullDistance >= refreshThreshold) {
                startRefreshing()
            } else {
                resetPullDistance()
            }
        }
    }

    /**
     * 处理嵌套滚动
     * 当子View滚动到顶部后继续下拉时触发刷新
     */
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyUnconsumed < 0 && !target.canScrollVertically(-1)) {
            handlePull(-dyUnconsumed.toFloat())
        }
    }

    /**
     * 处理嵌套滚动（重载方法）
     */
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        if (dyUnconsumed < 0 && !target.canScrollVertically(-1)) {
            handlePull(-dyUnconsumed.toFloat())
        }
    }

    /**
     * 处理子View滚动前的操作
     * 主要用于处理下拉刷新的回弹过程
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (currentPullDistance > 0 && dy > 0) {
            if (dy > currentPullDistance) {
                consumed[1] = currentPullDistance.toInt()
                currentPullDistance = 0f
            } else {
                currentPullDistance -= dy
                consumed[1] = dy
            }
            requestLayout()
        }
    }
}