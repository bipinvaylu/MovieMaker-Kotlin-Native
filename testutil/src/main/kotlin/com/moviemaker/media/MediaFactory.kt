package com.moviemaker.media

import com.moviemaker.domain.Media
import io.codearte.jfairy.Fairy

class MediaFactory {

    val fairy = Fairy.create()

    val baseProducer = fairy.baseProducer()
    val dateProducer = fairy.dateProducer()
    val textProducer = fairy.textProducer()

    fun media() : Media.Image {
        val createdDate = dateProducer.randomDateInThePast(1).millisOfSecond * 1000L
        return Media.Image(
            path = textProducer.sentence(),
            thmPath = textProducer.sentence(),
            createdDate = createdDate,
            fileSize = baseProducer.randomBetween(1,10000L)
        )
    }

    fun mediaList(): List<Media> {
        val listSize = baseProducer.randomInt(10)
        return (0..listSize).map{ media() }
    }

}