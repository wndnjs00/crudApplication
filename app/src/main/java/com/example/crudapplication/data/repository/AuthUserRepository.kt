package com.example.crudapplication.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.crudapplication.data.model.User

interface AuthUserRepository {
    fun registerUser(
        email: String,
        password: String,
        name: String?,
        phone: String?,
        address: String?,
        onSuccess: Runnable,
        onError: Runnable,
    )

    fun loginUser(
        email: String,
        password: String,
        authLivedata: MutableLiveData<User>,
        onError: Runnable,
    )

    fun logout(onSuccess: Runnable, onError: Runnable)
    fun isLoggedIn(): Boolean
    fun getStoredToken(): String?
    fun deleteAccount(onSuccess: Runnable, onError: Runnable)
} 