package com.moviemaker.ui.media

import com.airbnb.epoxy.EpoxyController
import com.moviemaker.domain.Media
import io.reactivex.functions.Consumer
import timber.log.Timber

class MediaController (
        private val selectedMedia: Collection<Media>? = null,
        private val clickListener: (Media) -> Any?,
        private val longClickListener: (Media) -> Any?
) : EpoxyController() {

    // private members
    private val _media = mutableListOf<Media>()

    // public properties
    val media = Consumer<Collection<Media>> { media ->
        _media.clear()
        _media.addAll(media)
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


}