package com.moviemaker.widget.recyclerview

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.State
import android.view.View

class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: State?) {
        val padding = space / 2
        outRect.left = padding
        outRect.right = padding
        outRect.top = padding
        outRect.bottom = padding
    }
}