package com.moviemaker.factory

import com.moviemaker.domain.Media

actual class LocalMediaDataSource {
    actual fun getMediaList(): List<Media> {
        return listOf()
    }
}