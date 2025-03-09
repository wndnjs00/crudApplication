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

            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            // 응답 처리: 401 응답은 Authenticator로 전달
            Response response = chain.proceed(newRequest);

            if (response.code() == 401) {
                Log.d("AuthInterceptor", "Access Token expired, passing to Authenticator.");
            }

            return response;
        }
        return chain.proceed(originalRequest);
    }
}

