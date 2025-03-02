package com.example.crudapplication.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.crudapplication.data.model.User;

public interface AuthUserRepository {
    void registerUser(String email, String password, String name, String phone, String address, Runnable onSuccess, Runnable onError);
    void loginUser(String email, String password, MutableLiveData<User> authLivedata, Runnable onError);
    void logout();
    boolean isLoggedIn();
    String getStoredToken();
}
