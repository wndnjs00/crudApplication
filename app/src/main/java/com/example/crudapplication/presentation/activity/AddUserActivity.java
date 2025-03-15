package com.example.crudapplication.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.crudapplication.R;
import com.example.crudapplication.databinding.ActivityAddUserBinding;
import com.example.crudapplication.presentation.viewmodel.UserViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddUserActivity extends AppCompatActivity {
    private ActivityAddUserBinding binding;
    private UserViewModel viewModel;    // ViewModel 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        initializeViews();
        setupSaveButtonListener();
    }


    // View Binding 초기화
    private void initializeViews() {
        binding = ActivityAddUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void setupSaveButtonListener() {
        binding.saveButton.setOnClickListener(v -> {
            // 입력값 가져오기
            String name = binding.editName.getText().toString();
            String phone = binding.editPhone.getText().toString();
            String address = binding.editAddress.getText().toString();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "모든값을 입력해주세요", Toast.LENGTH_SHORT).show();
            }else{
                // ViewModelProvider로 ViewModel 초기화 (Hilt를 사용한 ViewModel은 반드시 ViewModelProvider로 주입받아야함)
                viewModel = new ViewModelProvider(this).get(UserViewModel.class);

                // 데이터 저장
                viewModel.addUser(name, phone, address, () -> {
                    Toast.makeText(this, "데이터가 추가되었습니다", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish(); // 현재 Activity 종료
                });
            }
        });
    }
}


