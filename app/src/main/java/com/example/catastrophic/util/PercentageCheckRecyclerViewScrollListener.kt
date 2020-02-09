package com.example.catastrophic.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PercentageCheckRecyclerViewScrollListener(
    val layoutManager: GridLayoutManager
)  : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val firstPosition = layoutManager.findFirstVisibleItemPosition()
        val lastPosition = layoutManager.findLastVisibleItemPosition()

        val globalVisibleRect = Rect()
        recyclerView.getGlobalVisibleRect(globalVisibleRect)

        var total = 0
        for (pos in firstPosition..lastPosition) {
            val view = layoutManager.findViewByPosition(pos)
            if (view != null) {
                onViewChecked(pos, getVisibleHeightPercentage(view))
                total++
            }
        }

        onComplete(total, firstPosition, lastPosition)
    }

    abstract fun onViewChecked(position: Int, percentage: Double)

    abstract fun onComplete(totalChecked: Int, firstPosition: Int, lastPosition: Int)

    private fun getVisibleHeightPercentage(view: View): Double {

        val itemRect = Rect()
        val isParentViewEmpty = view.getLocalVisibleRect(itemRect)

        // Find the height of the item.
        val visibleHeight = itemRect.height().toDouble()
        val height = view.getMeasuredHeight()

        val viewVisibleHeightPercentage = visibleHeight / height * 100

        if(isParentViewEmpty){
            return viewVisibleHeightPercentage
        } else{
            return 0.0
        }
    }
}