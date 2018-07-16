package com.moviemaker.di.module

import android.content.Context
import com.moviemaker.settings.SettingsRepository
import com.moviemaker.utils.settingsRepository
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun prefsRepo(context: Context): SettingsRepository {
        return settingsRepository(context)
    }

    @Provides
    @Singleton
    fun picasso(context: Context): Picasso {
        return Picasso.Builder(context)
                .loggingEnabled(false)
                .build()
    }
}