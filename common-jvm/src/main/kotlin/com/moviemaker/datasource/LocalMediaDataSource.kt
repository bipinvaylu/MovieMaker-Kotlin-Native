package com.moviemaker.datasource

import com.moviemaker.domain.Media
import com.moviemaker.factroy.MediaFactory

actual open class LocalMediaDataSource {
    actual open fun getMediaList(onComplete: (List<Media>) -> Unit) {
        onComplete(MediaFactory.mediaList())
    }
}