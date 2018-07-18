package com.moviemaker.datasource

import android.os.Handler
import android.os.Looper
import com.moviemaker.domain.Media
import com.moviemaker.settings.SettingsRepository
import com.moviemaker.utils.mediaListAdapter
import kotlin.concurrent.thread

actual class LocalMediaDataSource(
        private val settingsRepo: SettingsRepository
) {

    actual fun getMediaList(onComplete: (List<Media>) -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        thread {
//            Timber.d("Bipin - savedMedia: ${settingsRepo.savedMedia}")
            val mediaList = mediaListAdapter().fromJson(settingsRepo.savedMedia)
//            val mediaList = settingsRepo.getMediaList()
            handler.post {
                onComplete(mediaList ?: listOf())
            }
        }
    }

}