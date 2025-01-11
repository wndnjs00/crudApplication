package com.example.crudapplication.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.crudapplication.R;
import com.example.crudapplication.presentation.viewmodel.AuthViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {
    private EditText emailEdit, passwordEdit, nameEdit, phoneEdit, addressEdit;
    private Button registerButton;
    private TextView loginText;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();
        setupRegisterButtonListener();
        setupLoginActivity();
    }

    // UI요소 초기화
    private void initializeViews(){
        emailEdit = findViewById(R.id.et_email);
        passwordEdit = findViewById(R.id.et_password);
        nameEdit = findViewById(R.id.et_name);
        phoneEdit = findViewById(R.id.et_phone);
        addressEdit = findViewById(R.id.et_address);
        loginText = findViewById(R.id.tv_login);
        registerButton = findViewById(R.id.btn_register);
    }

    private void setupRegisterButtonListener() {
        registerButton.setOnClickListener(v -> {
            // 입력값 가져오기
            String email = emailEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            String name = nameEdit.getText().toString();
            String phone = phoneEdit.getText().toString();
            String address = addressEdit.getText().toString();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // ViewModel 초기화
            authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

            // 회원가입 요청
            authViewModel.registerUser(email, password, name, phone, address, () -> {
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    },
                    () -> Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show());
        });
    }

    private void setupLoginActivity(){
        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
}