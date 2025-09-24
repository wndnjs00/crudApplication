package com.example.crudapplication.data.api

import com.example.crudapplication.data.local.AuthAuthenticator
import com.example.crudapplication.data.local.AuthInterceptor
import com.example.crudapplication.data.local.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "http://10.0.2.2:8080/"

    fun getInstance(tokenManager: TokenManager, authApi: AuthApi): Retrofit {
        if (retrofit == null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(tokenManager))
                .authenticator(AuthAuthenticator(tokenManager, authApi))
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
} 