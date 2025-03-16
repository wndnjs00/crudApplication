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
                    Log.d("accessToken임", "Access Token임: " + accessToken);
                    Log.d("refreshToken임", "Refresh Token임: " + refreshToken);

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
    public void logout(Runnable onSuccess, Runnable onError) {
        String accessToken = tokenManager.getAccessToken();
        if (accessToken != null) {
            // 서버 로그아웃 API 호출
            api.logout("Bearer " + accessToken).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                    if (response.isSuccessful()) {
                        Log.d("Logout", "Logout successful on server");
                        tokenManager.clearToken(); // 로컬 토큰 삭제
                        onSuccess.run(); // 성공 콜백 실행
                    } else {
                        Log.e("Logout", "Logout failed on server: " + response.code());
                        onError.run(); // 실패 콜백 실행
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable throwable) {
                    Log.e("Logout", "Logout network error: " + throwable.getMessage(), throwable);
                    onError.run(); // 네트워크 오류 콜백 실행
                }
            });
        } else {
            Log.w("Logout", "Access token is null, clearing local tokens");
            tokenManager.clearToken(); // 로컬 토큰 삭제만 수행
            onSuccess.run(); // 로컬 토큰만 삭제해도 성공 콜백 실행
        }
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
