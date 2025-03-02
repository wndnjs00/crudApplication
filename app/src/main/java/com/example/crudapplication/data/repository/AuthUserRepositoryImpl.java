package com.example.crudapplication.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.crudapplication.data.api.AuthApi;
import com.example.crudapplication.data.api.RetrofitService;
import com.example.crudapplication.data.dto.LoginRequestDto;
import com.example.crudapplication.data.dto.RegisterRequestDto;
import com.example.crudapplication.data.local.TokenManager;
import com.example.crudapplication.data.model.ApiResponse;
import com.example.crudapplication.data.model.User;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AuthUserRepositoryImpl implements AuthUserRepository{
    private final AuthApi api;
    private final TokenManager tokenManager;

    @Inject
    public AuthUserRepositoryImpl(AuthApi api, TokenManager tokenManager){
        this.api = api;     // API 통신을 위한 인터페이스
        this.tokenManager = tokenManager;   // 토큰 관리를 위한 클래스
    }

    @Override
    public void registerUser(String email, String password, String name, String phone, String address, Runnable onSuccess, Runnable onError) {

        RegisterRequestDto requestDto = new RegisterRequestDto(email, password, name, phone, address);
        // api호출
        api.registerUser(requestDto).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null ){
                    Log.d("RegisterUser_성공", "User registered successfully: " + response.body());
                    onSuccess.run();    // 성공 시 콜백 실행
                }else{
                    Log.e("RegisterUser_실패", "Registration failed: " + response.code());
                    onError.run();      // 실패 시 콜백 실행
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable throwable) {
                Log.e("RegisterUser_네트워크 오류", "Network error: " + throwable.getMessage(), throwable);
                onError.run();          // 네트워크 오류 시 콜백 실행
            }
        });
    }

    @Override
    public void loginUser(String email, String password, MutableLiveData<User> authLivedata, Runnable onError) {
        LoginRequestDto requestDto = new LoginRequestDto(email, password);

        // api호출
        api.loginUser(requestDto).enqueue(new Callback<ApiResponse<Map<String, String>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Map<String, String>>> call, @NonNull Response<ApiResponse<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    // 서버 응답에서 토큰추출
                    Map<String, String> tokens = response.body().getData();
                    String accessToken = tokens.get("accessToken");
                    String refreshToken = tokens.get("refreshToken");

                    // 토큰 저장
                    tokenManager.saveTokens(accessToken, refreshToken);

                    // User 객체 생성 및 LiveData 업데이트
                    authLivedata.setValue(new User(email, password, null, null, null)); // User 객체 업데이트 // 즉,데이터가 성공적으로 반환되면 LiveData로 설정된값(authLivedata값) 업데이트
                    Log.d("LoginUser_성공", "Login successful");
                    } else {
                        Log.e("LoginUser_실패", "Login failed: Invalid data");
                        onError.run();  // 로그인 실패 콜백 실행  //서버가 응답을 제대로 반환하지 않을시에 LiveData값 업데이트되지 않고, onError.run()실행
                    }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Map<String, String>>> call, @NonNull Throwable throwable) {
                Log.e("LoginUser_네트워크오류", "Network error: " + throwable.getMessage(), throwable);
                onError.run();  // 네트워크 오류 콜백 실행
            }
        });
    }

    // 로그아웃 메서드
    @Override
    public void logout(String accessToken, Runnable onSuccess, Runnable onError) {
        api.logout("Bearer " + accessToken).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()){
                    // 로그아웃 성공시 토큰 삭제
                    tokenManager.clearToken();
                    onSuccess.run();
                } else if (response.code() == 401 || response.code() == 403) {
                    Log.e("LogoutServer", "Access token expired. Attempting refresh.");
                    refreshTokenAndRetryLogout(onSuccess, onError);
                } else {
                    Log.e("LogoutServer", "Failed to logout: " + response.code());
                    onError.run();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable throwable) {
                Log.e("LogoutServer", "Network error: " + throwable.getMessage(), throwable);
                onError.run();
            }
        });
    }


    private void refreshTokenAndRetryLogout(Runnable onSuccess, Runnable onError) {
        String refreshToken = tokenManager.getRefreshToken();
        if (refreshToken == null) {
            Log.e("LogoutServer", "No refresh token available. Logout failed.");
            onError.run();
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("refreshToken", refreshToken);

        api.refreshToken(body).enqueue(new Callback<ApiResponse<Map<String, String>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Map<String, String>>> call, @NonNull Response<ApiResponse<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, String> tokens = response.body().getData();
                    String newAccessToken = tokens.get("accessToken");
                    String newRefreshToken = tokens.get("refreshToken");

                    // 새로운 토큰 저장
                    tokenManager.saveTokens(newAccessToken, newRefreshToken);

                    // 새 토큰으로 로그아웃 재시도
                    logout(newAccessToken, onSuccess, onError);
                } else {
                    Log.e("LogoutServer", "Failed to refresh token for logout.");
                    onError.run();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Map<String, String>>> call, @NonNull Throwable throwable) {
                Log.e("LogoutServer", "Network error during token refresh: " + throwable.getMessage(), throwable);
                onError.run();
            }
        });
    }


    // 로그인 상태 확인
    @Override
    public boolean isLoggedIn() {
        Log.d("IsLoggedIn", "Logged in status: " + tokenManager.hasToken());
        return tokenManager.hasToken(); // 토큰 존재 여부 확인하여 결과반환
    }

    // 저장된 토큰 조회
    @Override
    public String getStoredToken() {
        return tokenManager.getAccessToken(); // 저장된 토큰 반환
    }
}
