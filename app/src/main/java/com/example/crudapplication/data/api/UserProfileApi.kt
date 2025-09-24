package com.example.crudapplication.data.api

import com.example.crudapplication.data.dto.UserProfileRequestDto
import com.example.crudapplication.data.model.ApiResponse
import com.example.crudapplication.data.model.UserProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface UserProfileApi {
    @GET("/user/all")
    fun getAllUsers(): Call<ApiResponse<List<UserProfile>>>

    @POST("/user/new")
    fun createUser(@Body user: UserProfileRequestDto): Call<ApiResponse<Void>>

    @PUT("/user/{uuid}")
    fun updateUser(@Path("uuid") uuid: UUID, @Body user: UserProfileRequestDto): Call<ApiResponse<Void>>

    @DELETE("/user/{uuid}")
    fun deleteUser(@Path("uuid") uuid: UUID): Call<ApiResponse<Void>>
} 