package com.example.crudapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crudapplication.data.model.UserProfile
import com.example.crudapplication.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val userList: MutableLiveData<List<UserProfile>> = MutableLiveData()

    fun getUserList(): LiveData<List<UserProfile>> = userList

    fun AllFetchUsers() {
        userRepository.AllFetchUsers(userList)
    }

    fun addUser(name: String, phone: String, address: String, profileImage: String?, onSuccess: Runnable) {
        val user = UserProfile(name = name, phone = phone, address = address, profileImage = profileImage)
        userRepository.createUser(user) {
            AllFetchUsers()
            onSuccess.run()
        }
    }

    fun updateUser(uuid: UUID, name: String, phone: String, address: String, profileImage: String?, onSuccess: Runnable) {
        userRepository.updateUser(uuid, name, phone, address, profileImage) {
            AllFetchUsers()
            onSuccess.run()
        }
    }

    fun deleteUser(uuid: UUID, onSuccess: Runnable) {
        userRepository.deleteUser(uuid) {
            AllFetchUsers()
            onSuccess.run()
        }
    }
} 