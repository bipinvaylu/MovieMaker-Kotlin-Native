package com.moviemaker.factory

import com.moviemaker.domain.Image
import com.moviemaker.domain.Media
import java.util.*

object MediaFactory {

//    private val fairy = Fairy.create()
//
//    private val baseProducer = fairy.baseProducer()
//    private val dateProducer = fairy.dateProducer()
//    private val textProducer = fairy.textProducer()

    private fun media(
        path: String,
//        thmPath: String,
        createdDate: Long,
        fileSize: Long
    ): Media {
        return Image(
            path,
//            thmPath,
            createdDate,
            fileSize
        )
    }

    fun mediaList(): List<Media> {
        val randomNumber = Random().nextInt(10)
        val listSize = if (randomNumber <= 0) randomNumber * -1 else randomNumber
        return (0..listSize).map {
            media(
                path = RandomString.generateRandomString(),
//                thmPath = RandomString.generateRandomString(),
                createdDate = Date().time - Random().nextLong(),
                fileSize = Random().nextLong()
            )
        }
    }
}

object RandomString {
    private val CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
    private val RANDOM_STRING_LENGTH = 10

    fun generateRandomString(): String {
        val randStr = StringBuffer()
        for (i in 0 until RANDOM_STRING_LENGTH) {
            val number = getRandomNumber()
            val ch = CHAR_LIST[number]
            randStr.append(ch)
        }
        return randStr.toString()
    }

    private fun getRandomNumber(): Int {
        val randomGenerator = Random()
        val randomInt = randomGenerator.nextInt(CHAR_LIST.length)
        return if (randomInt - 1 == -1) {
            randomInt
        } else {
            randomInt - 1
        }
    }
}