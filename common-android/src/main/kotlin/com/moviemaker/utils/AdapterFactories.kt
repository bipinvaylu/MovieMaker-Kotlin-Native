package com.moviemaker.utils

import com.moviemaker.domain.Media

object AdapterFactories {

    private val MEDIA = TypeValueAdapterFactory(Media::class.java)

    val PERSIST_FACTORIES = listOf(MEDIA)
}