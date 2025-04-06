package ru.point.search.ui.search

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.point.search.R

internal class SearchHistoryAdapterDecorator(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight: Int = (context.resources.displayMetrics.density * 1).toInt()

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.md_theme_outline).toInt()
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft.toFloat()
        val right = (parent.width - parent.paddingRight).toFloat()
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin.toFloat()
            val bottom = top + dividerHeight
            canvas.drawRect(left, top, right, bottom, paint)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = (16 * view.context.resources.displayMetrics.density).toInt()
    }
}