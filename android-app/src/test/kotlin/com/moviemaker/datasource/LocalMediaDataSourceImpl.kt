package com.moviemaker.datasource

import com.moviemaker.domain.Media

class LocalMediaDataSourceImpl: LocalMediaDataSource() {
    override fun getMediaList(onComplete: (List<Media>) -> Unit) {
        //TODO: Read from shared preferences
    }
}
