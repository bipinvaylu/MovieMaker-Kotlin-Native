package com.moviemaker.datasource

import com.moviemaker.domain.Media

expect class LocalMediaDataSource {
    fun getMediaList(): List<Media>
}