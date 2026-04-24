package com.weatherapp.data.local.converter

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class Converters {
    private val moshi = Moshi.Builder().build()
    private val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
    private val doubleListType = Types.newParameterizedType(List::class.java, Double::class.javaObjectType)
    private val intListType = Types.newParameterizedType(List::class.java, Int::class.java)

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val adapter: JsonAdapter<List<String>> = moshi.adapter(stringListType)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val adapter: JsonAdapter<List<String>> = moshi.adapter(stringListType)
        return adapter.fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromDoubleList(value: List<Double>): String {
        val adapter: JsonAdapter<List<Double>> = moshi.adapter(doubleListType)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toDoubleList(value: String): List<Double> {
        val adapter: JsonAdapter<List<Double>> = moshi.adapter(doubleListType)
        return adapter.fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        val adapter: JsonAdapter<List<Int>> = moshi.adapter(intListType)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        val adapter: JsonAdapter<List<Int>> = moshi.adapter(intListType)
        return adapter.fromJson(value) ?: emptyList()
    }
}
