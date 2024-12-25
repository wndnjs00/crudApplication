package com.example.crudapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class UserViewModel extends ViewModel {

    private UserRepository repository;
    private MutableLiveData<List<UserProfile>> userList = new MutableLiveData<>();

    public UserViewModel() {
        repository = new UserRepository();
    }

    public MutableLiveData<List<UserProfile>> getUserList() {
        return userList;
    }

    public void fetchUsers() {
        repository.fetchAllUsers(userList);
    }

    public void addUser(UserProfile user, Runnable onSuccess) {
        repository.createUser(user, () -> {
            fetchUsers();
            onSuccess.run();
        });
    }

    public void deleteUser(String id, Runnable onSuccess) {
        repository.deleteUser(id, () -> {
            fetchUsers();
            onSuccess.run();
        });
    }
}

