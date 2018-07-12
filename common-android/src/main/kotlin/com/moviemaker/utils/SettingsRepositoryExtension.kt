package com.moviemaker.utils

import android.content.Context
import com.moviemaker.settings.SettingsRepository
import com.russhwolf.settings.PlatformSettings.Factory

fun settingsRepository(context: Context) = SettingsRepository(Factory(context))