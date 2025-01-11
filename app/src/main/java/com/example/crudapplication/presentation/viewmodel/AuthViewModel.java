package com.example.crudapplication.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.crudapplication.data.model.User;
import com.example.crudapplication.data.repository.AuthUserRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private final AuthUserRepository authUserRepository;
    private final MutableLiveData<User> authUserList = new MutableLiveData<>();

    @Inject
    public AuthViewModel(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    public LiveData<User> getAuthUserList(){
        return authUserList;
    }

    public void registerUser(String email, String password, String name, String phone, String address, Runnable onSuccess, Runnable onError){
        authUserRepository.registerUser(email, password, name, phone, address, onSuccess, onError);
    }

    public void loginUser(String email, String password, Runnable onError){
        // authUserRepository.loginUser()를 호출해 API요청을 보냄
        authUserRepository.loginUser(email, password, authUserList, onError);
    }
}
