package com.example.crudapplication.data.api;

import com.example.crudapplication.data.dto.LoginRequestDto;
import com.example.crudapplication.data.dto.RegisterRequestDto;
import com.example.crudapplication.data.model.ApiResponse;
import com.example.crudapplication.data.model.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {
    // 회원가입
    @POST("/auth/register")
    Call<ApiResponse<Void>> registerUser(@Body RegisterRequestDto registerRequestDto);

    // 로그인
    @POST("/auth/login")
    Call<ApiResponse<Map<String, String>>> loginUser(@Body LoginRequestDto loginRequestDto);

    // AccessToken 만료시, Refresh Token을 사용해 토큰갱신하는 API
    @POST("/auth/refresh")
    Call<ApiResponse<Map<String, String>>> refreshToken(@Body Map<String, String> body);

//    // 로그아웃
//    @POST("/auth/logout")
//    Call<ApiResponse<Void>> logout(@Header("Authorization") String accessToken);
}
