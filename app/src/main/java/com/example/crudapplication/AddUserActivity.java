package com.example.crudapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class AddUserActivity extends AppCompatActivity {

    private EditText idEdit;
    private EditText nameEdit;
    private EditText phoneEdit;
    private EditText addressEdit;
    private Button saveButton;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        initializeViews();
        setupSaveButtonListener();

    }


    // UI 요소 초기화
    private void initializeViews() {
        idEdit = findViewById(R.id.edit_id);
        nameEdit = findViewById(R.id.edit_name);
        phoneEdit = findViewById(R.id.edit_phone);
        addressEdit = findViewById(R.id.edit_address);
        saveButton = findViewById(R.id.save_button);
    }

    private void setupSaveButtonListener() {
        saveButton.setOnClickListener(v -> {
            String id = idEdit.getText().toString();
            String name = nameEdit.getText().toString();
            String phone = phoneEdit.getText().toString();
            String address = addressEdit.getText().toString();

            if (id.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "모든값을 입력해주세요", Toast.LENGTH_SHORT).show();
            }

            // ViewModel 초기화
            viewModel = new ViewModelProvider(this).get(UserViewModel.class);

            // 데이터 저장
            viewModel.addUser(id, name, phone, address, () -> {
                Toast.makeText(this, "데이터가 추가되었습니다", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish(); // 현재 Activity 종료
            });
        });
    }
}


