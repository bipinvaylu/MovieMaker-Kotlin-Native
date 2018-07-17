package com.moviemaker.utils

import com.moviemaker.domain.Image
import com.moviemaker.domain.Media
import com.moviemaker.domain.MediaJson
import com.moviemaker.domain.MediaType
import com.moviemaker.domain.Video
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class MediaAdapter {

    @FromJson
    fun fromJson(mediaJson: MediaJson): Media {
        return when (mediaJson.mediaType) {
            MediaType.IMAGE -> Image(
                    mediaJson.path,
                    mediaJson.createdDate,
                    mediaJson.createdDate
            )
            MediaType.VIDEO -> Video(
                    mediaJson.path,
                    mediaJson.createdDate,
                    mediaJson.createdDate
            )
        }
    }

    @ToJson
    fun toJson(media: Media): MediaJson {
        return when(media) {
            is Image -> MediaJson(
                    MediaType.IMAGE,
                    media.path,
                    media.createdDate,
                    media.fileSize
            )
            is Video -> MediaJson(
                    MediaType.VIDEO,
                    media.path,
                    media.createdDate,
                    media.fileSize
            )
            else -> throw RuntimeException("Not support data type")
        }
    }

}