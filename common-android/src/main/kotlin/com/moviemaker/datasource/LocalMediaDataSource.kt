package com.moviemaker.datasource

import com.moviemaker.domain.Media
import com.moviemaker.settings.SettingsRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

actual class LocalMediaDataSource (
        private val settingsRepo: SettingsRepository
) {
    actual fun getMediaList(onComplete: (List<Media>) -> Unit) {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(Media::class.java)
        val adapter = moshi.adapter<List<Media>>(type)
        val mediaList = adapter.fromJson(settingsRepo.savedMedia)
        onComplete(mediaList ?: listOf())
    }
}