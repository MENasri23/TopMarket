package ir.jatlin.topmarket.ui.util

import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.IntDef
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpacerItemDecoration(
    var space: Int,
    private val orientation: Int = VERTICAL,
    private var isReversed: Boolean = false
) : RecyclerView.ItemDecoration() {

    companion object {
        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter ?: return
        val viewPosition = parent.getChildAdapterPosition(view)
        if (viewPosition == adapter.itemCount - 1) return

        val isRtl =
            (parent.layoutDirection == View.LAYOUT_DIRECTION_LTR) xor isReversed
        when {
            isRtl && orientation == HORIZONTAL -> outRect.left = space
            orientation == HORIZONTAL -> outRect.right = space
            else -> outRect.bottom = space
        }

    }

}