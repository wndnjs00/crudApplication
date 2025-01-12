package com.example.crudapplication.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.crudapplication.data.api.AuthApi;
import com.example.crudapplication.data.api.RetrofitService;
import com.example.crudapplication.data.local.TokenManager;
import com.example.crudapplication.data.model.User;

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
        // api호출
        api.registerUser(email, password, name, phone, address).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()){
                    onSuccess.run();    // 성공 시 콜백 실행
                }else{
                    onError.run();      // 실패 시 콜백 실행
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                onError.run();          // 네트워크 오류 시 콜백 실행
            }
        });
    }

    @Override
    public void loginUser(String email, String password, MutableLiveData<User> authLivedata, Runnable onError) {

        // api호출
        api.loginUser(email, password).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Object>> call, @NonNull Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null){

                    // 서버 응답에서 토큰과 user데이터 추출
                    Map<String, Object> responseMap = response.body();
                    String token = (String) responseMap.get("token");
                    
                     // Map을 User 객체로 변환
                    Map<String, Object> userMap = (Map<String, Object>) responseMap.get("user");
                    User user = new User();
                    user.setId(((Double) userMap.get("id")).intValue());
                    user.setEmail((String) userMap.get("email"));
                    user.setName((String) userMap.get("name"));
                    user.setPhone((String) userMap.get("phone"));
                    user.setAddress((String) userMap.get("address"));

                    // 토큰 저장
                    tokenManager.saveToken(token);

                    // user 정보를 LiveData로 전달
                    authLivedata.setValue(user);  // 즉,데이터가 성공적으로 반환되면 LiveData로 설정된값(authLivedata값) 업데이트
                }else{
                    onError.run();  // 로그인 실패 콜백 실행  //서버가 응답을 제대로 반환하지 않을시에 LiveData값 업데이트되지 않고, onError.run()실행
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Object>> call, @NonNull Throwable throwable) {
                onError.run();  // 네트워크 오류 콜백 실행
            }
        });
    }

    // 로그아웃 메서드
    @Override
    public void logout() {
        tokenManager.clearToken();  // 저장된 토큰 삭제
    }

    // 로그인 상태 확인
    @Override
    public boolean isLoggedIn() {
        return tokenManager.hasToken(); // 토큰 존재 여부 확인하여 결과반환
    }

    // 저장된 토큰 조회
    @Override
    public String getStoredToken() {
        return tokenManager.getToken(); // 저장된 토큰 반환
    }
}
