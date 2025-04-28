package ru.point.menu.ui.stateholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MenuDecorator : RecyclerView.ItemDecoration() {

    private fun dpToPx(dp: Int, density: Float): Int =
        (dp * density).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        val density = view.context.resources.displayMetrics.density

        outRect.top = if (position == 0) {
            dpToPx(10, density)
        } else {
            0
        }

        outRect.bottom = if (position == itemCount - 1) {
            dpToPx(10, density)
        } else {
            dpToPx(20, density)
        }

        outRect.left = 0
        outRect.right = 0
    }
}


