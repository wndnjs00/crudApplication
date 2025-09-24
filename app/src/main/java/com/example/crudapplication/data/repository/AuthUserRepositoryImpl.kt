package com.example.crudapplication.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.crudapplication.data.api.AuthApi
import com.example.crudapplication.data.dto.LoginRequestDto
import com.example.crudapplication.data.dto.RegisterRequestDto
import com.example.crudapplication.data.local.TokenManager
import com.example.crudapplication.data.model.ApiResponse
import com.example.crudapplication.data.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUserRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager,
) : AuthUserRepository {

    override fun registerUser(
        email: String,
        password: String,
        name: String?,
        phone: String?,
        address: String?,
        onSuccess: Runnable,
        onError: Runnable,
    ) {
        val requestDto = RegisterRequestDto(email, password, name ?: "", phone ?: "", address ?: "")
        api.registerUser(requestDto).enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(
                call: Call<ApiResponse<Void>>, response: Response<ApiResponse<Void>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("RegisterUser_성공", "User registered successfully: ${response.body()}")
                    onSuccess.run()
                } else {
                    Log.e("RegisterUser_실패", "Registration failed: ${response.code()}")
                    onError.run()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                Log.e("RegisterUser_네트워크 오류", "Network error: ${t.message}", t)
                onError.run()
            }
        })
    }

    override fun loginUser(
        email: String,
        password: String,
        authLivedata: MutableLiveData<User>,
        onError: Runnable,
    ) {
        val requestDto = LoginRequestDto(email, password)
        api.loginUser(requestDto).enqueue(object : Callback<ApiResponse<Map<String, String>>> {
            override fun onResponse(
                call: Call<ApiResponse<Map<String, String>>>,
                response: Response<ApiResponse<Map<String, String>>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val tokens = response.body()!!.data
                    val accessToken = tokens?.get("accessToken")
                    val refreshToken = tokens?.get("refreshToken")

                    tokenManager.saveTokens(accessToken, refreshToken)
                    authLivedata.value = User(email = email, password = password, name = null, phone = null, address = null)
                    Log.d("LoginUser_성공", "Login successful")
                    Log.d("accessToken임", "Access Token임: ${accessToken}")
                    Log.d("refreshToken임", "Refresh Token임: ${refreshToken}")
                } else {
                    Log.e("LoginUser_실패", "Login failed: Invalid data")
                    onError.run()
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<Map<String, String>>>,
                t: Throwable
            ) {
                Log.e("LoginUser_네트워크오류", "Network error: ${t.message}", t)
                onError.run()
            }
        })
    }

    override fun logout(onSuccess: Runnable, onError: Runnable) {
        val accessToken = tokenManager.accessToken
        if (accessToken != null) {
            api.logout("Bearer $accessToken").enqueue(object : Callback<ApiResponse<Void>> {
                override fun onResponse(
                    call: Call<ApiResponse<Void>>, response: Response<ApiResponse<Void>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Logout", "Logout successful on server")
                        tokenManager.clearToken()
                        onSuccess.run()
                    } else {
                        Log.e("Logout", "Logout failed on server: ${response.code()}")
                        onError.run()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                    Log.e("Logout", "Logout network error: ${t.message}", t)
                    onError.run()
                }
            })
        } else {
            Log.w("Logout", "Access token is null, clearing local tokens")
            tokenManager.clearToken()
            onSuccess.run()
        }
    }

    override fun isLoggedIn(): Boolean {
        Log.d("IsLoggedIn", "Logged in status: ${tokenManager.hasToken()}")
        return tokenManager.hasToken()
    }

    override fun getStoredToken(): String? = tokenManager.accessToken

    override fun deleteAccount(onSuccess: Runnable, onError: Runnable) {
        val accessToken = tokenManager.accessToken
        if (accessToken != null) {
            api.deleteAccount("Bearer $accessToken").enqueue(object : Callback<ApiResponse<Void>> {
                override fun onResponse(
                    call: Call<ApiResponse<Void>>, response: Response<ApiResponse<Void>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("DeleteAccount", "Account deletion successful on server")
                        tokenManager.clearToken()
                        onSuccess.run()
                    } else {
                        Log.e("DeleteAccount", "Account deletion failed on server: ${response.code()}")
                        onError.run()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                    Log.e("DeleteAccount", "Account deletion network error: ${t.message}", t)
                    onError.run()
                }
            })
        } else {
            onError.run()
        }
    }
} 