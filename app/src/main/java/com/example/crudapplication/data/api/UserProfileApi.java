package com.example.crudapplication.data.api;

import com.example.crudapplication.data.model.ApiResponse;
import com.example.crudapplication.data.model.UserProfile;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
import java.util.UUID;

public interface UserProfileApi {
    @GET("/user/all")
    Call<ApiResponse<List<UserProfile>>> getAllUsers();

    @POST("/user/new")
    Call<ApiResponse<Void>> createUser(@Query("name") String name, @Query("phone") String phone, @Query("address") String address);

    @PUT("/user/{uuid}")
    Call<ApiResponse<Void>> updateUser(@Path("uuid") UUID uuid, @Query("name") String name, @Query("phone") String phone, @Query("address") String address);

    @DELETE("/user/{uuid}")
    Call<ApiResponse<Void>> deleteUser(@Path("uuid") UUID uuid);
}

