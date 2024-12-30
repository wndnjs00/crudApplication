package com.example.crudapplication.data.repository;

import androidx.lifecycle.MutableLiveData;
import com.example.crudapplication.data.model.UserProfile;
import java.util.List;

public interface UserRepository {
    void AllFetchUsers(MutableLiveData<List<UserProfile>> liveData);
    void createUser(UserProfile user, Runnable onSuccess);
    void deleteUser(int id, Runnable onSuccess);
}