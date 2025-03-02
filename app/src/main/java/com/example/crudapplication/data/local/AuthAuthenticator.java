package com.example.crudapplication.data.local;

import android.content.Context;
import android.content.Intent;
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
        String refreshToken = tokenManager.getRefreshToken();

        if (refreshToken == null) {
            handleLogout(); // Refresh Token이 없으면 로그아웃 처리
            return null; // Refresh Token이 없으면 재인증 불가능
        }

        // Refresh Token으로 새로운 Access Token 요청
        Map<String, String> body = new HashMap<>();
        body.put("refreshToken", refreshToken);

        Call<ApiResponse<Map<String, String>>> call = authApi.refreshToken(body);
        retrofit2.Response<ApiResponse<Map<String, String>>> tokenResponse = call.execute();

        if (tokenResponse.isSuccessful() && tokenResponse.body() != null) {
            ApiResponse<Map<String, String>> apiResponse = tokenResponse.body();

//            if ("400".equals(apiResponse.getStatus())) {
//                handleLogout();
//                return null;
//            }

            Map<String, String> data = apiResponse.getData();

            String newAccessToken = data.get("accessToken");
            String newRefreshToken = data.get("refreshToken");

            // 새로운 토큰 저장
            tokenManager.saveTokens(newAccessToken, newRefreshToken);

            // 새로운 Access Token으로 요청 재전송
            return response.request().newBuilder()
                    .header("Authorization", "Bearer " + newAccessToken)
                    .build();
        }else{
            handleLogout(); // Refresh Token도 만료된 경우
            return null;
        }
    }

    private void handleLogout() {
        tokenManager.clearToken(); // 토큰 삭제

        // 로그아웃 처리: 사용자에게 알림
        Context context = HiltApplication.getContext(); // 글로벌 컨텍스트 가져오기
        Toast.makeText(context, "토큰이 만료되었습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
    }
}

