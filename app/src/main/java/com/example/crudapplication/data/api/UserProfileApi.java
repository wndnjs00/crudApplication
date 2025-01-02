package com.example.crudapplication.data.api;

import com.example.crudapplication.data.model.UserProfile;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface UserProfileApi {
    @GET("/user/all")
    Call<List<UserProfile>> getAllUsers();

    @POST("/user")
    Call<Void> createUser(@Query("name") String name, @Query("phone") String phone, @Query("address") String address);

    @PUT("/user/{id}")
    Call<Void> updateUser(@Path("id") int id, @Query("name") String name, @Query("phone") String phone, @Query("address") String address);

    @DELETE("/user/{id}")
    Call<Void> deleteUser(@Path("id") int id);
}

