package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barPaint = Paint().apply {
        color = Color.parseColor("#8E87E4")
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = Color.parseColor("#CFCFEF")
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    private var hourlyData: Map<String, Float> = emptyMap()

    fun setData(data: Map<String, Float>) {
        hourlyData = data
        invalidate() // Redraw the view with the new data
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (hourlyData.isEmpty()) return

        val barWidth = width / (hourlyData.size * 2f)
        val maxBarHeight = height * 0.6f
        val maxDataValue = hourlyData.values.maxOrNull() ?: 1f

        hourlyData.entries.forEachIndexed { index, entry ->
            val barHeight = (entry.value / maxDataValue) * maxBarHeight
            val left = index * 2 * barWidth + barWidth / 2
            val top = height - barHeight - 80f
            val right = left + barWidth
            val bottom = height - 80f

            canvas.drawRect(left, top, right, bottom, barPaint)
            canvas.drawText(entry.key, left + barWidth / 2, height.toFloat() - 20, textPaint)
            canvas.drawText("${entry.value.toInt()}", left + barWidth / 2, top - 20f, textPaint)
        }
    }
}