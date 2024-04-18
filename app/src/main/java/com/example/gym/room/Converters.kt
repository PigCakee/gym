package com.example.gym.room

import androidx.room.TypeConverter
import com.example.gym.data.Exercise
import com.example.gym.data.ExerciseWithSets
import com.example.gym.data.Set
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun fromStringToStringList(value: String?): List<String> {
        val listType: Type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromStringListToString(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToIntList(value: String?): List<Int> {
        val listType: Type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromIntListToString(list: List<Int>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toExercises(value: String?): List<Exercise> {
        val listType: Type = object : TypeToken<List<Exercise>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromExercises(exercises: List<Exercise>): String {
        return Gson().toJson(exercises)
    }

    @TypeConverter
    fun fromSets(sets: List<Set>): String {
        return Gson().toJson(sets)
    }

    @TypeConverter
    fun toSets(value: String?): List<Set> {
        val listType: Type = object : TypeToken<List<Set>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromEWS(sets: List<ExerciseWithSets>): String {
        return Gson().toJson(sets)
    }

    @TypeConverter
    fun toEWS(value: String?): List<ExerciseWithSets> {
        val listType: Type = object : TypeToken<List<ExerciseWithSets>>() {}.type
        return Gson().fromJson(value, listType)
    }
}

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)