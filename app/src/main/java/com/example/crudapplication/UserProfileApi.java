package com.example.crudapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface UserProfileApi {
    @GET("/user/all")
    Call<List<UserProfile>> getAllUsers();

    @POST("/user/{id}")
    Call<Void> createUser(@Path("id") String id, @Query("name") String name, @Query("phone") String phone, @Query("address") String address);

    @DELETE("/user/{id}")
    Call<Void> deleteUser(@Path("id") String id);
}

