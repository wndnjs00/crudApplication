package com.example.crudapplication.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val uuid: String,
    val name: String?,
    val phone: String?,
    val address: String?,
    val profileImage: String?,
) 