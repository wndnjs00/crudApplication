package com.example.crudapplication.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.crudapplication.R;
import com.example.crudapplication.presentation.viewmodel.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditUserActivity extends AppCompatActivity {
    private EditText nameEdit, phoneEdit, addressEdit;
    private Button editButton;
    private int userId;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        initializeViews();
        getData();
        setupEditButtonListener();
    }

    // UI 요소 초기화
    private void initializeViews(){
        nameEdit = findViewById(R.id.edit_name);
        phoneEdit = findViewById(R.id.edit_phone);
        addressEdit = findViewById(R.id.edit_address);
        editButton = findViewById(R.id.save_button);
    }

    // 편집버튼 눌렀을떄 각각의 리사이클러뷰 아이템데이터 받아옴 / 받아와서 그 데이터를 뿌려줌
    private void getData(){
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);  // id 값 가져오기 //id값이없으면 기본값을 -1로 지정
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");

        nameEdit.setText(name);
        phoneEdit.setText(phone);
        addressEdit.setText(address);
    }

    private void setupEditButtonListener() {
        editButton.setOnClickListener(v -> {
            String name = nameEdit.getText().toString();
            String phone = phoneEdit.getText().toString();
            String address = addressEdit.getText().toString();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "모든 값을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel = new ViewModelProvider(this).get(UserViewModel.class);
            viewModel.updateUser(userId, name, phone , address, () -> {
                Toast.makeText(this, "데이터가 수정되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            });

        });
    }
}