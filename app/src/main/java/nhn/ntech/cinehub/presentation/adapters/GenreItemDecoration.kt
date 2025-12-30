package nhn.ntech.cinehub.presentation.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GenreItemDecoration(
    private val sidePadding: Int
) : RecyclerView.ItemDecoration()
{
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        // Nếu là item ĐẦU TIÊN: Gán padding sát lề trái
        if (position == 0) {
            outRect.left = sidePadding
        }

        // Nếu là item CUỐI CÙNG: Gán padding sát lề phải
        if (position == itemCount - 1) {
            outRect.right = sidePadding
        }
    }
}