package com.moviemaker.datasource

import com.moviemaker.domain.Media

interface LocalMediaDatasSource {

    fun getMediaList(onComplete: (List<Media>) -> Unit)

}