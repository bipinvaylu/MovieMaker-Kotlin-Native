package com.moviemaker.utils

import com.moviemaker.domain.Media
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


fun moshiBuilder() =
        Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(MediaAdapter())
                .build()

fun mediaListAdapter() = moshiBuilder()
        .adapter<List<Media>>(
                Types.newParameterizedType(
                        List::class.java,
                        Media::class.java
                )
        )

//fun moshiBuilder() =
//    AdapterFactories.PERSIST_FACTORIES
//            .fold(Moshi.Builder()) { builder, factory -> builder.add(factory) }
//            .add(KotlinJsonAdapterFactory())
//            .build()
//
//fun mediaListAdapter() = moshiBuilder()
//        .adapter<List<Media>>(List::class.java)
