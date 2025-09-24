package com.example.crudapplication.data.local

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = tokenManager.accessToken

        if (accessToken != null) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()

            val response = chain.proceed(newRequest)
            if (response.code() == 401) {
                Log.d("AuthInterceptor", "Access Token expired, passing to Authenticator.")
            }
            return response
        }
        return chain.proceed(originalRequest)
    }
} 