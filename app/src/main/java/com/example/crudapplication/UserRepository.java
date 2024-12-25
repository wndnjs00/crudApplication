package com.example.crudapplication;

import androidx.lifecycle.MutableLiveData;
import android.util.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class UserRepository {
    private final UserProfileApi api;

    public UserRepository() {
        api = RetrofitService.getInstance().create(UserProfileApi.class);
    }

    public void fetchAllUsers(MutableLiveData<List<UserProfile>> liveData) {
        api.getAllUsers().enqueue(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    Log.e("에러", "api 에러");
                }
            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {
                Log.e("실패", t.getMessage());
            }
        });
    }

    public void createUser(UserProfile user, Runnable onSuccess) {
        api.createUser(user.getId(), user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    onSuccess.run();
                } else {
                    Log.e("에러", "api 에러");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("실패", t.getMessage());
            }
        });
    }

    public void deleteUser(String id, Runnable onSuccess) {
        api.deleteUser(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    onSuccess.run();
                } else {
                    Log.e("에러", "api 에러");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("실패", t.getMessage());
            }
        });
    }
}

