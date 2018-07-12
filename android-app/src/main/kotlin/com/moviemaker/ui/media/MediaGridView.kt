package com.moviemaker.ui.media

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.constraint.Group
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import com.moviemaker.App
import com.moviemaker.R
import com.moviemaker.datasource.LocalMediaDataSource
import com.moviemaker.domain.Media
import com.moviemaker.interactor.GetMediaList
import com.moviemaker.widget.recyclerview.SpacesItemDecoration
import kotterknife.bindView

class MediaGridView : ConstraintLayout {

    // private members

    // protected members

    // public members

    // views
    private val progressBar: ProgressBar by bindView(R.id.progress_bar)
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
    private val emptyViewGroup: Group by bindView(R.id.empty_group)

    // properties
    private val controller: MediaController by lazy {
        MediaController()
    }
    private val mediaList = mutableListOf<Media>()
    private val getMediaList: GetMediaList by lazy {
        GetMediaList(LocalMediaDataSource(App.settingsRepo))
    }

    // constructors

    @JvmOverloads
    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr)


    // Private functions

    // Override functions

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val spanCount = resources.getInteger(R.integer.grid_span_count)
        val spacing = resources.getDimensionPixelSize(R.dimen.material_baseline_grid_1x)
        val layoutManager = GridLayoutManager(context, spanCount)
        layoutManager.spanSizeLookup = controller.getSpanSizeLookup(spanCount)

        recyclerView.adapter = controller.adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(SpacesItemDecoration(spacing))

        loadMediaList()

    }

    // public functions
    fun loadMediaList() {
        emptyViewGroup.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        getMediaList.execute {
            progressBar.visibility = GONE
            mediaList.clear()
            mediaList.addAll(it)
            controller.media.accept(it)
            if (mediaList.size == 0) {
                emptyViewGroup.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyViewGroup.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }
    }

    fun addMedia(media: Media) {
        mediaList.add(media)
        App.settingsRepo.savedMedia = App.mediaAdapter.toJson(mediaList.toList())
        controller.media.accept(mediaList.toList())
    }

}