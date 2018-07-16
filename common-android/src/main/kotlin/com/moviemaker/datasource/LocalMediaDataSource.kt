package com.moviemaker.datasource

import android.os.Handler
import com.moviemaker.domain.Media
import com.moviemaker.settings.SettingsRepository
import com.moviemaker.utils.mediaListAdapter
import kotlin.concurrent.thread

actual class LocalMediaDataSource(
        private val settingsRepo: SettingsRepository
) {

    actual fun getMediaList(onComplete: (List<Media>) -> Unit) {
        val handler = Handler()
        thread {
            val mediaList = mediaListAdapter().fromJson(settingsRepo.savedMedia)
            handler.post {
                onComplete(mediaList ?: listOf())
            }
        }
    }

}