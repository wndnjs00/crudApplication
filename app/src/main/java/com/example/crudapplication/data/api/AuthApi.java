package com.example.crudapplication.data.api;

import com.example.crudapplication.data.dto.LoginRequestDto;
import com.example.crudapplication.data.dto.RegisterRequestDto;
import com.example.crudapplication.data.model.ApiResponse;
import com.example.crudapplication.data.model.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {
    // 회원가입
    @POST("/auth/register")
    Call<ApiResponse<?>> registerUser(@Body RegisterRequestDto registerRequestDto);

    // 로그인
    @POST("/auth/login")
    Call<ApiResponse<Map<String, Object>>> loginUser(@Body LoginRequestDto loginRequestDto);
}
