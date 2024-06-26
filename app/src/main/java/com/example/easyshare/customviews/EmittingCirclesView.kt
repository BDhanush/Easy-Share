package com.example.easyshare.customviews

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class EmittingCirclesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    private val circles: MutableList<Circle> = mutableListOf()
    private var animator: ValueAnimator? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (circle in circles) {
            paint.alpha = circle.alpha
            canvas.drawCircle(circle.centerX, circle.centerY, circle.radius, paint)
        }
    }

    fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2000 // Duration of the animation
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                updateCircles(animatedValue)
                invalidate()
            }
            start()
        }
    }

    private fun updateCircles(progress: Float) {
        circles.clear()
        val maxRadius = minOf(width, height) / 2f
        for (i in 0 until 5) {
            val radiusProgress = (progress + i * 0.2f) % 1
            val radius = maxRadius * radiusProgress
            val alpha = ((1 - radiusProgress) * 255).toInt()
            circles.add(Circle(width / 2f, height / 2f, radius, alpha))
        }
    }

    private data class Circle(val centerX: Float, val centerY: Float, val radius: Float, val alpha: Int)
}
