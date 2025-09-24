package com.example.crudapplication.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.crudapplication.data.api.AuthApi
import com.example.crudapplication.data.dto.LoginRequestDto
import com.example.crudapplication.data.dto.RegisterRequestDto
import com.example.crudapplication.data.local.TokenManager
import com.example.crudapplication.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUserRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager,
) : AuthUserRepository {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    override fun registerUser(
        email: String,
        password: String,
        name: String?,
        phone: String?,
        address: String?,
        onSuccess: Runnable,
        onError: Runnable,
    ) {
        ioScope.launch {
            try {
                val requestDto = RegisterRequestDto(email, password, name ?: "", phone ?: "", address ?: "")
                api.registerUser(requestDto)
                Log.d("RegisterUser_성공", "User registered successfully")
                onSuccess.run()
            } catch (e: Exception) {
                Log.e("RegisterUser_실패", e.message ?: "")
                onError.run()
            }
        }
    }

    override fun loginUser(
        email: String,
        password: String,
        authLivedata: MutableLiveData<User>,
        onError: Runnable,
    ) {
        ioScope.launch {
            try {
                val requestDto = LoginRequestDto(email, password)
                val response = api.loginUser(requestDto)
                val tokens = response.data
                val accessToken = tokens?.get("accessToken")
                val refreshToken = tokens?.get("refreshToken")

                tokenManager.saveTokens(accessToken, refreshToken)
                authLivedata.postValue(User(email = email, password = password, name = null, phone = null, address = null))
                Log.d("LoginUser_성공", "Login successful")
            } catch (e: Exception) {
                Log.e("LoginUser_실패", e.message ?: "")
                onError.run()
            }
        }
    }

    override fun logout(onSuccess: Runnable, onError: Runnable) {
        ioScope.launch {
            try {
                val accessToken = tokenManager.accessToken
                if (accessToken != null) {
                    api.logout("Bearer $accessToken")
                    tokenManager.clearToken()
                    onSuccess.run()
                } else {
                    tokenManager.clearToken()
                    onSuccess.run()
                }
            } catch (e: Exception) {
                Log.e("Logout", e.message ?: "")
                onError.run()
            }
        }
    }

    override fun isLoggedIn(): Boolean = tokenManager.hasToken()

    override fun getStoredToken(): String? = tokenManager.accessToken

    override fun deleteAccount(onSuccess: Runnable, onError: Runnable) {
        ioScope.launch {
            try {
                val accessToken = tokenManager.accessToken
                if (accessToken != null) {
                    api.deleteAccount("Bearer $accessToken")
                    tokenManager.clearToken()
                    onSuccess.run()
                } else {
                    onError.run()
                }
            } catch (e: Exception) {
                Log.e("DeleteAccount", e.message ?: "")
                onError.run()
            }
        }
    }
} 