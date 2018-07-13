package com.moviemaker.ui.media

import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import com.airbnb.epoxy.EpoxyController
import com.moviemaker.domain.Media
import io.reactivex.functions.Consumer
import timber.log.Timber

class MediaController : EpoxyController() {

    // private members
    private val _media = mutableListOf<Media>()

    // public properties
    val media = Consumer<Collection<Media>> { media ->
        _media.clear()
        _media.addAll(media.sortedBy { it.createdDate })
        Timber.d("Bipin - MediaController media: $_media")
        requestModelBuild()
    }

    // override functions
    override fun buildModels() {
        _media.forEach { mediaItem ->
            Timber.d("Bipin - MediaController mediaItem: $mediaItem")
            MediaItemView.Model(mediaItem)
                    .id(mediaItem.id)
                    .addTo(this)
        }
    }

    // functions
    fun getSpanSizeLookup(spanCount: Int): SpanSizeLookup {
        return object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return spanCount
            }
        }
    }

}