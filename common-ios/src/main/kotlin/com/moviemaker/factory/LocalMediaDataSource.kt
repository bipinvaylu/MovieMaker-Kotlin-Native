package com.moviemaker.factory

import com.moviemaker.domain.Media

actual open class LocalMediaDataSource {
    actual open fun getMediaList(onComplete: (List<Media>) -> Unit) {
        onComplete(MediaFactory.mediaList())
    }
}