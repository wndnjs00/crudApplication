package com.example.crudapplication.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.crudapplication.data.model.UserProfile;
import com.example.crudapplication.data.repository.UserRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserViewModel extends ViewModel {

    private final UserRepository repository;
    private final MutableLiveData<List<UserProfile>> userList = new MutableLiveData<>();

    // Hilt를 통해 UserRepository 인터페이스를 주입받음
    @Inject
    public UserViewModel(UserRepository repository) {
        this.repository = repository;
    }

    // LiveData를 사용해 실시간으로 데이터를 관찰해서 view에 보겨주기위함
    public LiveData<List<UserProfile>> getUserList() {
        return userList;
    }

    public void AllFetchUsers() {
        repository.AllFetchUsers(userList);
    }

    public void addUser(String name, String phone, String address, Runnable onSuccess) {
        UserProfile user = new UserProfile(name, phone, address);
        repository.createUser(user, () -> {
            AllFetchUsers();    // 데이터 새로고침
            onSuccess.run();
        });
    }

    public void updateUser(int id, String name, String phone, String address, Runnable onSuccess){
        repository.updateUser(id, name, phone, address, () -> {
            AllFetchUsers();    //데이터 새로고침
            onSuccess.run();
        });
    }

    public void deleteUser(int id, Runnable onSuccess) {
        repository.deleteUser(id, () -> {
            AllFetchUsers();    //데이터 새로고침
            onSuccess.run();
        });
    }
}

