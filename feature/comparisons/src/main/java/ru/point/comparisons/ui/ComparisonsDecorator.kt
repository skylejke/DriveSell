package ru.point.comparisons.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ComparisonsDecorator: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        if (position != RecyclerView.NO_POSITION && position < itemCount - 1) {
            outRect.right = (20 * view.context.resources.displayMetrics.density).toInt()
        }
    }
}