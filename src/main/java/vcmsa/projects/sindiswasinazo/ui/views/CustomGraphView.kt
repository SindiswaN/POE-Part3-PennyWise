package vcmsa.projects.sindiswasinazo.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomGraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var values: List<Float> = listOf()
    var labels: List<String> = listOf()
    var minGoal: Float = 0f
    var maxGoal: Float = 0f

    private val paintBar = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    private val paintLine = Paint().apply {
        color = Color.RED
        strokeWidth = 4f
    }

    private val paintText = Paint().apply {
        color = Color.BLACK
        textSize = 30f
    }

    fun setMinLine(value: Float) {
        minGoal = value
        invalidate()
    }

    fun setMaxLine(value: Float) {
        maxGoal = value
        invalidate()
    }

    fun setBarChartData(labels: List<String>, values: List<Float>) {
        this.labels = labels
        this.values = values
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (values.isEmpty()) return

        val graphWidth = width - 100f
        val graphHeight = height - 100f
        val barWidth = graphWidth / values.size

        val maxValue = (values.maxOrNull() ?: 0f).coerceAtLeast(maxGoal) + 20

        values.forEachIndexed { i, value ->
            val scaledHeight = (value / maxValue) * graphHeight
            val left = i * barWidth + 50f
            val top = graphHeight - scaledHeight + 50f
            val right = left + barWidth * 0.6f
            val bottom = graphHeight + 50f

            // Draw bar
            canvas.drawRect(left, top, right, bottom, paintBar)

            // Draw label below bar
            labels.getOrNull(i)?.let {
                canvas.drawText(it, left, bottom + 30f, paintText)
            }
        }

        // Draw Min goal line
        val minY = graphHeight - ((minGoal / maxValue) * graphHeight) + 50f
        canvas.drawLine(50f, minY, graphWidth + 50f, minY, paintLine)
        canvas.drawText("Min Goal", 10f, minY, paintText)

        // Draw Max goal line
        val maxY = graphHeight - ((maxGoal / maxValue) * graphHeight) + 50f
        canvas.drawLine(50f, maxY, graphWidth + 50f, maxY, paintLine)
        canvas.drawText("Max Goal", 10f, maxY, paintText)
    }
}
