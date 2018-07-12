package com.moviemaker.domain

sealed class Media {

    val id: Int
        get() = arrayOf(
            path.substringAfterLast("/").substringAfterLast("\\"),
            (createdDate / 1000) * 1000, // Round to seconds to match resolution.
            fileSize
        ).hashCode()


    abstract val path: String
//    abstract val thmPath: String
    abstract val createdDate: Long
    abstract val fileSize: Long


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Functions


    abstract fun withPath(path: String): Media

//    abstract fun withThmPath(thmPath: String): Media

    data class Image(
        override val path: String,
//        override val thmPath: String,
        override val createdDate: Long,
        override val fileSize: Long
    ) : Media() {

        override fun withPath(path: String): Media {
            return copy(path = path)
        }

//        override fun withThmPath(thmPath: String): Media {
//            return copy(thmPath = thmPath)
//        }

    }

}