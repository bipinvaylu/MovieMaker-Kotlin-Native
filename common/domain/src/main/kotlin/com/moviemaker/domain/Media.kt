package com.moviemaker.domain

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


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Functions


    abstract fun getRelatedPaths(): Collection<String>

    abstract fun withPath(path: String): Media

    abstract fun withThmPath(thmPath: String): Media

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Instances

    data class Video(
        override val path: String,
        override val thmPath: String,
        override val createdDate: Long,
        override val fileSize: Long,
        val duration: Long,
        val lrvPath: String,
        val csvPath: String
    ) : Media() {

        override fun getRelatedPaths(): Collection<String> {
            return listOf(lrvPath, csvPath, thmPath)
        }

        override fun withPath(path: String): Media {
            return copy(path = path)
        }

        override fun withThmPath(thmPath: String): Media {
            return copy(thmPath = thmPath)
        }

    }

    data class Image(
        override val path: String,
        override val thmPath: String,
        override val createdDate: Long,
        override val fileSize: Long
    ) : Media() {

        override fun getRelatedPaths(): Collection<String> {
            return listOf(thmPath)
        }

        override fun withPath(path: String): Media {
            return copy(path = path)
        }

        override fun withThmPath(thmPath: String): Media {
            return copy(thmPath = thmPath)
        }

    }

    companion object {

        val VIDEO_EXTENSIONS = arrayOf("MP4")
        val IMAGE_EXTENSIONS = arrayOf("JPG")

    }

}
