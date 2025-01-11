package com.example.crudapplication.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.crudapplication.data.api.AuthApi;
import com.example.crudapplication.data.api.RetrofitService;
import com.example.crudapplication.data.model.User;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AuthUserRepositoryImpl implements AuthUserRepository{
    private final AuthApi api;

    public AuthUserRepositoryImpl(AuthApi api){
        this.api = RetrofitService.getInstance().create(AuthApi.class);
    }

    @Override
    public void registerUser(String email, String password, String name, String phone, String address, Runnable onSuccess, Runnable onError) {
        api.registerUser(email, password, name, phone, address).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()){
                    onSuccess.run();
                }else{
                    onError.run();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                onError.run();
            }
        });
    }

    @Override
    public void loginUser(String email, String password, MutableLiveData<User> authLivedata, Runnable onError) {
        api.loginUser(email, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()){
                    // 서버가 성공응답(200ok반환)과 User객체를 반환하면 authLivedata.setValue(response.body()) 호출 -> LiveData인 authLiveData값 업데이트
                    authLivedata.setValue(response.body());  // 즉,데이터가 성공적으로 반환되면 LiveData로 설정된값(authLivedata값) 업데이트
                }else{
                    onError.run();  // 로그인 실패 콜백 실행  //서버가 응답을 제대로 반환하지 않을시에 LiveData값 업데이트되지 않고, onError.run()실행
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable throwable) {
                onError.run();  // 네트워크 오류 콜백 실행
            }
        });
    }
}
