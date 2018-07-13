package com.moviemaker.datasource

import com.moviemaker.domain.Media
import com.moviemaker.settings.SettingsRepository
import com.moviemaker.utils.mediaListAdapter

actual class LocalMediaDataSource(
        private val settingsRepo: SettingsRepository
) {
    actual fun getMediaList(onComplete: (List<Media>) -> Unit) {
        val mediaList = mediaListAdapter().fromJson(settingsRepo.savedMedia)
        onComplete(mediaList ?: listOf())
    }
}