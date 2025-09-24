package com.example.crudapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crudapplication.data.local.TokenManager
import com.example.crudapplication.data.model.User
import com.example.crudapplication.data.repository.AuthUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUserRepository: AuthUserRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val authUserList: MutableLiveData<User> = MutableLiveData()

    fun getAuthUserList(): LiveData<User> = authUserList

    fun getTokenExpiredLiveData(): LiveData<Boolean> = tokenManager.getTokenExpiredLiveData()

    fun registerUser(
        email: String,
        password: String,
        name: String,
        phone: String,
        address: String,
        onSuccess: Runnable,
        onError: Runnable,
    ) {
        authUserRepository.registerUser(email, password, name, phone, address, onSuccess, onError)
    }

    fun loginUser(email: String, password: String, onError: Runnable) {
        authUserRepository.loginUser(email, password, authUserList, onError)
    }

    fun logout(onSuccess: Runnable, onError: Runnable) {
        authUserRepository.logout(onSuccess, onError)
        tokenManager.setTokenExpired(true)
    }

    fun resetTokenExpired() {
        tokenManager.setTokenExpired(false)
    }

    fun deleteAccount(onSuccess: Runnable, onError: Runnable) {
        authUserRepository.deleteAccount(onSuccess, onError)
        tokenManager.clearToken()
    }
} 