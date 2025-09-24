package com.example.crudapplication.data.api

import com.example.crudapplication.data.dto.LoginRequestDto
import com.example.crudapplication.data.dto.RegisterRequestDto
import com.example.crudapplication.data.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/register")
    suspend fun registerUser(@Body registerRequestDto: RegisterRequestDto): ApiResponse<Void>

    @POST("/auth/login")
    suspend fun loginUser(@Body loginRequestDto: LoginRequestDto): ApiResponse<Map<String, String>>

    @POST("/auth/refresh")
    suspend fun refreshToken(@Body body: Map<String, String>): ApiResponse<Map<String, String>>

    @POST("/auth/logout")
    suspend fun logout(@Header("Authorization") token: String): ApiResponse<Void>

    @DELETE("/auth/delete")
    suspend fun deleteAccount(@Header("Authorization") token: String): ApiResponse<Void>

    @GET("/auth/info")
    suspend fun getUserInfo(@Header("Authorization") token: String): ApiResponse<Void>
} 