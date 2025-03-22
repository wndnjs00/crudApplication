package com.example.crudapplication.data.repository;

import androidx.lifecycle.MutableLiveData;
import com.example.crudapplication.data.model.UserProfile;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
    void AllFetchUsers(MutableLiveData<List<UserProfile>> liveData);
    void createUser(UserProfile user, Runnable onSuccess);
    void updateUser(UUID uuid, String name, String phone, String address, String profileImage, Runnable onSuccess);
    void deleteUser(UUID uuid, Runnable onSuccess);
}