package com.moviemaker.di.component

import com.moviemaker.di.module.AppModule
import com.moviemaker.settings.SettingsRepository
import com.squareup.picasso.Picasso
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun picasso(): Picasso

    fun prefsRepo(): SettingsRepository

}