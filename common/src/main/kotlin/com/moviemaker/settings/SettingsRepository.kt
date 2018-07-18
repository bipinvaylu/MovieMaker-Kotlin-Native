package com.moviemaker.settings

import com.russhwolf.settings.Settings

private val SETTINGS_NAME: String? = "com.moviemaker.settings"

class SettingsRepository(factory: Settings.Factory) {

    private val settings = factory.create(SETTINGS_NAME)

    private val MEDIA_LIST = "media_list"
    var savedMedia: String
        get() =  settings.getString(MEDIA_LIST, "[]")
        set(value) = settings.putString(MEDIA_LIST, value)


    fun clear() = settings.clear()

//    private val mediaList = mutableListOf<Media>()
//
//    fun addMedia(media: Media) {
//        mediaList.add(media)
//    }
//
//    fun getMediaList() = mediaList.toList()


}