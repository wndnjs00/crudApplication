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

import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditUserActivity extends AppCompatActivity {
    private ActivityAddUserBinding binding; // View Binding 객체 재사용
    private UUID userUuid;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        initializeViews();
        getData();
        setupEditButtonListener();
    }

    // View Binding 초기화
    private void initializeViews(){
        binding = ActivityAddUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    // 편집버튼 눌렀을떄 각각의 리사이클러뷰 아이템데이터 받아옴 / 받아와서 그 데이터를 뿌려줌
    private void getData(){
        Intent intent = getIntent();
        userUuid = UUID.fromString(intent.getStringExtra("id"));  // id 값 가져오기
        binding.editName.setText(intent.getStringExtra("name"));
        binding.editPhone.setText(intent.getStringExtra("phone"));
        binding.editAddress.setText(intent.getStringExtra("address"));
    }

    private void setupEditButtonListener() {
        binding.saveButton.setOnClickListener(v -> {
            String name = binding.editName.getText().toString();
            String phone = binding.editPhone.getText().toString();
            String address = binding.editAddress.getText().toString();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "모든 값을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel = new ViewModelProvider(this).get(UserViewModel.class);
            viewModel.updateUser(userUuid, name, phone , address, () -> {
                Toast.makeText(this, "데이터가 수정되었습니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            });

        });
    }
}