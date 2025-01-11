package com.example.crudapplication.data.api;

import com.example.crudapplication.data.model.User;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {
    // 회원가입
    @POST("/user/register")
    Call<Void> registerUser(@Query("email") String email,
                            @Query("password") String password,
                            @Query("name") String name,
                            @Query("phone") String phone,
                            @Query("address") String address);

    // 로그인
    @POST("/user/login")
    Call<User> loginUser(@Query("email") String email, @Query("password") String password);
}
