package com.moviemaker.interactor

import com.moviemaker.datasource.LocalMediaDatasSource
import com.moviemaker.domain.Media

class GetMediaList constructor(
    private val local: LocalMediaDatasSource
) {
    fun execute(onComplete: (List<Media>) -> Unit) {
        local.getMediaList() {
            onComplete(it)
        }
    }
}