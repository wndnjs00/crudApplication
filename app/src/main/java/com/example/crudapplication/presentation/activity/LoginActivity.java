package com.example.crudapplication.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.crudapplication.R;
import com.example.crudapplication.presentation.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupLoginButtonListener();
    }

    // UI 요소 초기화
    private void initializeViews(){
        emailEditText = findViewById(R.id.et_email2);
        passwordEditText = findViewById(R.id.et_password2);
        loginButton = findViewById(R.id.btn_login);
    }

    private void setupLoginButtonListener(){
        loginButton.setOnClickListener(v -> {
            // 입력값 가져오기
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            }

            // ViewModel 초기화
            authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

            // LiveData값을 관찰해서 해당값이 업데이트되면(200ok되고, 성공적으로 User데이터가 들어갔다는뜻), 로그인 성공이라는 메시지가 뜸
            authViewModel.getAuthUserList().observe(this, user -> {
                if (user != null) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            // onError 호출됐을때 실행     // () -> 는 Runnable onError호출시 실행
            authViewModel.loginUser(email, password, () ->
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show());
        });
    }
}