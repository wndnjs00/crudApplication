package com.example.crudapplication.data.local;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.crudapplication.HiltApplication;
import com.example.crudapplication.data.api.AuthApi;
import com.example.crudapplication.data.model.ApiResponse;
import com.example.crudapplication.presentation.activity.LoginActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

// 에러를 받았을때만 호출되는 코드
public class AuthAuthenticator implements Authenticator {
    private final TokenManager tokenManager;
    private final AuthApi authApi;

    public AuthAuthenticator(TokenManager tokenManager, AuthApi authApi) {
        this.tokenManager = tokenManager;
        this.authApi = authApi;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {

        if (authApi == null) {
            Log.e("AuthAuthenticator", "AuthApi is null");
            return null;
        }

        String refreshToken = tokenManager.getRefreshToken();

        // Refresh Token이 없을 경우 토큰 만료 상태를 전달
        if (refreshToken == null) {
            tokenManager.notifyTokenExpired(); // 토큰 만료 알림 // Refresh Token이 없으면 로그아웃 처리
            return null; // Refresh Token이 없으면 재인증 불가능
        }

        // Refresh Token으로 새로운 Access Token 요청
        Map<String, String> body = new HashMap<>();
        body.put("refreshToken", refreshToken);

        Call<ApiResponse<Map<String, String>>> call = authApi.refreshToken(body);
        try{

        retrofit2.Response<ApiResponse<Map<String, String>>> tokenResponse = call.execute();

        // 새로운 토큰이 성공적으로 발급된 경우
        if (tokenResponse.isSuccessful() && tokenResponse.body() != null) {
            Map<String, String> tokens = tokenResponse.body().getData();

            // 새로 발급받은 Access Token 및 Refresh Token 저장
            String newAccessToken = tokens.get("accessToken");
            String newRefreshToken = tokens.get("refreshToken");

            // 로그 추가: 새로 발급받은 토큰 확인
            Log.d("AuthAuthenticator", "New Access Token: " + newAccessToken);
            Log.d("AuthAuthenticator", "New Refresh Token: " + newRefreshToken);

            // 새로운 토큰 저장
            tokenManager.saveTokens(newAccessToken, newRefreshToken);

            // 새로운 Access Token으로 요청 재전송
            return response.request().newBuilder()
                    .header("Authorization", "Bearer " + newAccessToken)
                    .build();
        } else {
            // 토큰 갱신 실패시, 토큰 만료 상태 알림
            tokenManager.notifyTokenExpired();
            return null;
        }
    }catch(Exception e){
            Log.e("AuthAuthenticator", "Error refreshing token: " + e.getMessage(), e);
            // 예외 발생 시 토큰 만료 상태 알림
            tokenManager.notifyTokenExpired();
            return null;
    } finally{
            call.cancel(); // 네트워크 요청 정리
        }
    }
}

