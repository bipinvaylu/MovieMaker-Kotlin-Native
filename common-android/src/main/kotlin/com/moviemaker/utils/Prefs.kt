package com.moviemaker.utils

import android.content.Context

class Prefs(context: Context) {
    private val PREF_FILENAME = "com.moviemaker.prefs"
    private val prefs = context.getSharedPreferences(PREF_FILENAME, 0)

    private val MEDIA_LIST = "media_list"
    var savedMedia: String
        get() = prefs.getString(MEDIA_LIST, "")
        set(value) = prefs.edit().putString(MEDIA_LIST, value).apply()


}