package com.moviemaker.domain

data class Image(
        val path: String,
        val thmPath: String,
        val createdDate: Long,
        val fileSize: Long
)