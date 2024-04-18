package com.example.gym.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gym.data.Exercise
import com.example.gym.room.dao.ExercisesDao
import com.example.gym.room.dao.SessionsDao

@Database(
    entities = [
        Exercise::class
    ],
    version = 1,
    exportSchema = true,
    autoMigrations = [
    ]
)
@TypeConverters(Converters::class)
abstract class GymDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: GymDatabase? = null

        fun getInstance(context: Context): GymDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    GymDatabase::class.java,
                    "gym.db"
                )
                    .allowMainThreadQueries()
                    .build()
            }

            return requireNotNull(INSTANCE)
        }
    }

    abstract fun exercisesDao(): ExercisesDao
    abstract fun sessionsDao(): SessionsDao
}
