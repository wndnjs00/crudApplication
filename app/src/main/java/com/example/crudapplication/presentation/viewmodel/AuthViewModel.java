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
        // authUserRepository.registerUser를 호출하여 회원가입 요청을 서버로 전송
        authUserRepository.registerUser(email, password, name, phone, address, onSuccess, onError);
    }

    public void loginUser(String email, String password, Runnable onError){
        // authUserRepository.loginUser()를 호출해 API요청을 보냄
        // 서버 응답이 성공적이면 -> authUserList에 사용자 데이터를 업데이트 , UI는 실시간으로 관찰중이기때문에 실시간 업데이트돰
        authUserRepository.loginUser(email, password, authUserList, onError);
    }

    // 로그아웃
    public void logout() {
        // 토큰 삭제
        authUserRepository.logout();
    }

    public boolean isLoggedIn() {
        // 저장된 토큰의 존재여부로 사용자가 로그인한 상태인지 아닌지 확인
        return authUserRepository.isLoggedIn();
    }

    public String getStoredToken() {
        // 저장된 토큰 반환
        return authUserRepository.getStoredToken();
    }
}
