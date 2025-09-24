package com.example.crudapplication.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles")
    fun getAll(): Flow<List<UserProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(users: List<UserProfileEntity>)

    @Query("DELETE FROM user_profiles WHERE uuid = :uuid")
    suspend fun deleteById(uuid: String)

    @Query("DELETE FROM user_profiles")
    suspend fun clear()
} 