package com.moviemaker.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonReader.Token
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

class TypeValueAdapterFactory<T : Any>(
        private val targetType: Class<T>
) : JsonAdapter.Factory {

    override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<T>? {
        val rawType = Types.getRawType(type)
        if (!targetType.isAssignableFrom(rawType)) return null
        return TypeValueAdapter(this, annotations, moshi)
    }

    @Suppress("UNCHECKED_CAST")
    private class TypeValueAdapter<T : Any>(
            private val factory: TypeValueAdapterFactory<T>,
            private val annotations: Set<Annotation>,
            private val moshi: Moshi
    ) : JsonAdapter<T>() {

        private val instanceAdapters = mutableMapOf<String, JsonAdapter<out T>>()

        override fun fromJson(reader: JsonReader): T? {
            if (reader.peek() == Token.NULL) {
                return null
            }

            reader.beginObject()
            val className = reader.nextName()
            val rawType = Class.forName(className) as Class<T>
            val instanceAdapter = getAdapter(rawType)
            val value = instanceAdapter.fromJson(reader)
            reader.endObject()

            return value
        }

        override fun toJson(writer: JsonWriter, value: T?) {
            if (value == null) {
                writer.nullValue()
                return
            }

            val rawType = Types.getRawType(value.javaClass) as Class<T>
            val className = rawType.name
            val instanceAdapter = getAdapter(rawType)

            writer.beginObject()
            writer.name(className)
            instanceAdapter.toJson(writer, value)
            writer.endObject()
        }

        private fun getAdapter(type: Class<out T>): JsonAdapter<T> {
            val className = type.name
            return instanceAdapters.getOrPut(className) {
                moshi.nextAdapter<T>(factory, type, annotations)
            } as JsonAdapter<T>
        }

    }

}