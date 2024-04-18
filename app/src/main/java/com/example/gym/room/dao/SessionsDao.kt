package com.example.gym.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gym.data.Session

@Dao
interface SessionsDao {

    @Query("SELECT * FROM sessions")
    suspend fun getAllSessions(): List<Session>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)
}