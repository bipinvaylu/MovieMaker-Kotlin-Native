package com.moviemaker.di.module

import android.content.Context
import com.moviemaker.App
import dagger.Module
import dagger.Provides

@Module(includes = [DataModule::class])
class AppModule(private val app: App) {

    @Provides
    fun context(): Context = app

}