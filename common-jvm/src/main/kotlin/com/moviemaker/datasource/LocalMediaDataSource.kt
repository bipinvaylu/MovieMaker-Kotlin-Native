package com.moviemaker.datasource

import com.moviemaker.domain.Media

actual open class LocalMediaDataSource {
    actual open fun getMediaList(onComplete: (List<Media>) -> Unit) {}
}