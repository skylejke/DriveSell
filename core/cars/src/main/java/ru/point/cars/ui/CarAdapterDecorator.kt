package ru.point.cars.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CarAdapterDecorator : RecyclerView.ItemDecoration() {
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