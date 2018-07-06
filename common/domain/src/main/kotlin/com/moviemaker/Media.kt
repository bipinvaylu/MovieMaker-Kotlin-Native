package com.moviemaker

sealed class Media {

    val id: Int
        get() = arrayOf(
            path.substringAfterLast("/").substringAfterLast("\\"),
            (createdDate / 1000) * 1000, // Round to seconds to match resolution.
            fileSize
        ).hashCode()


    abstract val path: String
    abstract val thmPath: String
    abstract val createdDate: Long
    abstract val fileSize: Long

}