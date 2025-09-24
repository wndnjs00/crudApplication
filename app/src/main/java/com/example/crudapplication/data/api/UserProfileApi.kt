package com.example.crudapplication.data.api

import com.example.crudapplication.data.dto.UserProfileRequestDto
import com.example.crudapplication.data.model.ApiResponse
import com.example.crudapplication.data.model.UserProfile
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface UserProfileApi {
    @GET("/user/all")
    suspend fun getAllUsers(): ApiResponse<List<UserProfile>>

    @POST("/user/new")
    suspend fun createUser(@Body user: UserProfileRequestDto): ApiResponse<Void>

    @PUT("/user/{uuid}")
    suspend fun updateUser(@Path("uuid") uuid: UUID, @Body user: UserProfileRequestDto): ApiResponse<Void>

    @DELETE("/user/{uuid}")
    suspend fun deleteUser(@Path("uuid") uuid: UUID): ApiResponse<Void>
} 