package com.example.crudapplication.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import android.os.Build;
import android.util.Log;

import com.example.crudapplication.data.dto.UserProfileRequestDto;
import com.example.crudapplication.data.model.ApiResponse;
import com.example.crudapplication.data.model.UserProfile;
import com.example.crudapplication.data.api.RetrofitService;
import com.example.crudapplication.data.api.UserProfileApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

// 데이터관리(비즈니스로직 관리)
// UserRepository의 구체적인 구현체
@Singleton
public class UserRepositoryImpl implements UserRepository{
    private final UserProfileApi api;

    public UserRepositoryImpl(UserProfileApi api) {
        this.api = api;
    }

    @Override
    public void AllFetchUsers(MutableLiveData<List<UserProfile>> liveData) {

        api.getAllUsers().enqueue(new Callback<ApiResponse<List<UserProfile>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<UserProfile>>> call, @NonNull Response<ApiResponse<List<UserProfile>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<UserProfile>> apiResponse = response.body();
                    liveData.setValue(apiResponse.getData()); // ApiResponse에서 data 추출  // setValue로 LiveData 갱신 (setValue or postValue로 LiveData를통한 데이터 실시간관찰 가능)
                    Log.d("전체 데이터조회 성공", "전체 데이터 조회 성공");
                } else {
                    Log.e("전체 데이터조회 실패", "전체 데이터 조회 실패: " + response.code()+ "-" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<UserProfile>>> call, @NonNull Throwable t) {
                Log.e("API 연결실패1", t.getMessage());
            }
        });
    }

    @Override
    public void createUser(UserProfile user, Runnable onSuccess) {
        Log.d("요청 JSON", "UserProfile : " + user);
        UserProfileRequestDto userProfileRequestDto = new UserProfileRequestDto(user.getName(), user.getPhone(), user.getAddress());

        api.createUser(userProfileRequestDto).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("데이터 저장 성공", "데이터 저장 성공");
                    onSuccess.run();
                } else {
                    Log.e("데이터 저장 실패", "데이터 저장 실패: " + response.code() + "-" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                Log.e("API 연결실패2", t.getMessage());
            }
        });
    }

    @Override
    public void updateUser(UUID uuid, String name, String phone, String address, Runnable onSuccess) {
        UserProfileRequestDto userProfileRequestDto = new UserProfileRequestDto(name, phone, address);

        api.updateUser(uuid, userProfileRequestDto).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()){
                    Log.d("데이터 수정 성공", "데이터 수정 성공");
                    onSuccess.run();
                }else{
                    Log.e("데이터 수정 실패", "데이터 수정 실패: " + response.code() + "-" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                Log.e("API 연결실패3", t.getMessage());
            }
        });
    }

    @Override
    public void deleteUser(UUID uuid, Runnable onSuccess) {

        api.deleteUser(uuid).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("데이터 삭제 성공", "데이터 삭제 성공");
                    onSuccess.run();
                } else {
                    Log.e("데이터 삭제 실패", "데이터 삭제 실패: " + response.code()+ "-" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                Log.e("API 연결실패4", t.getMessage());
            }
        });
    }
}

