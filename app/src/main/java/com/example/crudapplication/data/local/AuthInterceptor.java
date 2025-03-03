package com.example.crudapplication.data.local;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final TokenManager tokenManager;
    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String accessToken = tokenManager.getAccessToken();

        // Authorization 헤더 추가
        if (accessToken != null) {
            Log.d("AuthInterceptor", "Using Access Token: " + accessToken);

            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            Response response = chain.proceed(newRequest);

            // 응답에 새로운 Access Token이 있으면 갱신
            if (response.header("Authorization") != null) {
                String newAccessToken = response.header("Authorization").replace("Bearer ", "");
                Log.d("AuthInterceptor", "New Access Token received: " + newAccessToken);
                tokenManager.saveTokens(newAccessToken, tokenManager.getRefreshToken());
            }
            return response;
        }
        return chain.proceed(originalRequest);
    }
}

