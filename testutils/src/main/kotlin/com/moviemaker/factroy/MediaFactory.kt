package com.moviemaker.factroy

import com.moviemaker.domain.Media
import io.codearte.jfairy.Fairy

object MediaFactory {

    private val fairy = Fairy.create()

    private val baseProducer = fairy.baseProducer()
    private val dateProducer = fairy.dateProducer()
    private val textProducer = fairy.textProducer()

    private fun media() : Media.Image {
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