package com.example.crudapplication.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.crudapplication.data.model.UserProfile
import java.util.UUID

interface UserRepository {
    fun AllFetchUsers(liveData: MutableLiveData<List<UserProfile>>)
    fun createUser(user: UserProfile, onSuccess: Runnable)
    fun updateUser(uuid: UUID, name: String, phone: String, address: String, profileImage: String?, onSuccess: Runnable)
    fun deleteUser(uuid: UUID, onSuccess: Runnable)
} 