package com.example.crudapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class UserViewModel extends ViewModel {

    private final UserRepository repository;
    private final MutableLiveData<List<UserProfile>> userList = new MutableLiveData<>();

    public UserViewModel() {
        repository = new UserRepository();
    }

    public MutableLiveData<List<UserProfile>> getUserList() {
        return userList;
    }

    public void AllFetchUsers() {
        repository.AllFetchUsers(userList);
    }

    public void addUser(String id, String name, String phone, String address, Runnable onSuccess) {
        UserProfile user = new UserProfile(id, name, phone, address);
        repository.createUser(user, () -> {
            AllFetchUsers();    // 데이터 새로고침
            onSuccess.run();
        });
    }

    public void deleteUser(String id, Runnable onSuccess) {
        repository.deleteUser(id, () -> {
            AllFetchUsers();    //데이터 새로고침
            onSuccess.run();
        });
    }
}

