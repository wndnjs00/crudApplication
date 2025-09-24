package com.example.crudapplication.data.api

import com.example.crudapplication.data.dto.LoginRequestDto
import com.example.crudapplication.data.dto.RegisterRequestDto
import com.example.crudapplication.data.model.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/register")
    fun registerUser(@Body registerRequestDto: RegisterRequestDto): Call<ApiResponse<Void>>

    @POST("/auth/login")
    fun loginUser(@Body loginRequestDto: LoginRequestDto): Call<ApiResponse<Map<String, String>>>

    @POST("/auth/refresh")
    fun refreshToken(@Body body: Map<String, String>): Call<ApiResponse<Map<String, String>>>

    @POST("/auth/logout")
    fun logout(@Header("Authorization") token: String): Call<ApiResponse<Void>>

    @DELETE("/auth/delete")
    fun deleteAccount(@Header("Authorization") token: String): Call<ApiResponse<Void>>

    @GET("/auth/info")
    fun getUserInfo(@Header("Authorization") token: String): Call<ApiResponse<Void>>
} 