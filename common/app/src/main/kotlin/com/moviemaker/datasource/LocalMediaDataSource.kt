package com.moviemaker.datasource

import com.moviemaker.domain.Media

expect class LocalMediaDataSource {

    fun getMediaList(onComplete: (List<Media>) -> Unit)

}