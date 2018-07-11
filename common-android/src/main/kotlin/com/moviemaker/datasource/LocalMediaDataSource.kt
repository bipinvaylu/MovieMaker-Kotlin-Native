package com.moviemaker.datasource

import com.moviemaker.domain.Media
import com.moviemaker.utils.Prefs
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

actual class LocalMediaDataSource (
        private val prefs: Prefs
) {
    actual fun getMediaList(): List<Media> {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(Media::class.java)
        val adapter = moshi.adapter<List<Media>>(type)
        val mediaList = adapter.fromJson(prefs.savedMedia)
        return mediaList ?: listOf()
    }
}