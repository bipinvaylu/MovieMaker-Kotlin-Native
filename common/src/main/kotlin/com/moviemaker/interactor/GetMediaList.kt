package com.moviemaker.interactor

import com.moviemaker.datasource.LocalMediaDataSource
import com.moviemaker.domain.Media

class GetMediaList constructor(
    private val local: LocalMediaDataSource
) {
    fun execute(onComplete: (List<Media>) -> Unit) {
        local.getMediaList {
            onComplete(it)
        }
    }
}