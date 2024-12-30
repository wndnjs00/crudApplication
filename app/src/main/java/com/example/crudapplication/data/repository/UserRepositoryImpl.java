package com.example.crudapplication.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;
import com.example.crudapplication.data.model.UserProfile;
import com.example.crudapplication.data.api.RetrofitService;
import com.example.crudapplication.data.api.UserProfileApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import javax.inject.Singleton;

// 데이터관리(비즈니스로직 관리)
// UserRepository의 구체적인 구현체
@Singleton
public class UserRepositoryImpl implements UserRepository{
    private final UserProfileApi api;

    public UserRepositoryImpl(UserProfileApi api) {
        this.api = RetrofitService.getInstance().create(UserProfileApi.class);
    }

    @Override
    public void AllFetchUsers(MutableLiveData<List<UserProfile>> liveData) {
        api.getAllUsers().enqueue(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserProfile>> call, @NonNull Response<List<UserProfile>> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    Log.e("전체 데이터조회 실패", "전체 데이터 조회 실패: " + response.code()+ "-" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserProfile>> call, @NonNull Throwable t) {
                Log.e("API 연결실패", t.getMessage());
            }
        });
    }

    @Override
    public void createUser(UserProfile user, Runnable onSuccess) {
        Log.d("요청 JSON", "UserProfile : " + user);

        api.createUser(user.getName(), user.getPhone(), user.getAddress()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("데이터 저장 성공", "데이터 저장 성공");
                    onSuccess.run();
                } else {
                    Log.e("데이터 저장 실패", "데이터 저장 실패: " + response.code() + "-" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API 연결실패", t.getMessage());
            }
        });
    }

    @Override
    public void deleteUser(int id, Runnable onSuccess) {
        api.deleteUser(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("데이터 삭제 성공", "데이터 삭제 성공");
                    onSuccess.run();
                } else {
                    Log.e("데이터 삭제 실패", "데이터 삭제 실패: " + response.code()+ "-" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API 연결실패", t.getMessage());
            }
        });
    }
}

