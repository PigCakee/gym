package com.example.gym.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gym.components.ExerciseUi
import com.google.gson.annotations.SerializedName

@Entity(tableName = "exercises")
data class Exercise(
    @SerializedName("name") val name: String,
    @SerializedName("force") val force: String?,
    @SerializedName("level") val level: String,
    @SerializedName("mechanic") val mechanic: String?,
    @SerializedName("equipment") val equipment: String?,
    @SerializedName("primaryMuscles") val primaryMuscles: List<String>,
    @SerializedName("secondaryMuscles") val secondaryMuscles: List<String>,
    @SerializedName("instructions") val instructions: List<String>,
    @SerializedName("category") val category: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("recentlyAdded") val recentlyAdded: Boolean = false,
    @PrimaryKey(autoGenerate = false) @SerializedName("id") val id: String
) {
    fun mapToUi() = ExerciseUi(id = id, imageUrl = images.firstOrNull() ?: "", name = name, primaryMuscles = primaryMuscles, secondaryMuscles = secondaryMuscles)
}

data class ExerciseList(
    val exercises: List<Exercise>
)
