package com.example.crudapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface UserProfileApi {
    @GET("/user/all")
    Call<List<UserProfile>> getAllUsers();

    @POST("/user/{id}")
    Call<Void> createUser(@Path("id") String id, @Body UserProfile user);

    @DELETE("/user/{id}")
    Call<Void> deleteUser(@Path("id") String id);
}

