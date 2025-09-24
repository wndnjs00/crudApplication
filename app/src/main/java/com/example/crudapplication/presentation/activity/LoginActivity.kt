package com.example.crudapplication.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.crudapplication.R
import com.example.crudapplication.databinding.ActivityLoginBinding
import com.example.crudapplication.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupLoginButtonListener()
        setUpRegisterButtonListener()
    }

    private fun initializeViews() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupLoginButtonListener() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail2.text.toString().trim()
            val password = binding.etPassword2.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            }

            authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

            authViewModel.getAuthUserList().observe(this) { user ->
                if (user != null) {
                    authViewModel.resetTokenExpired()
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            authViewModel.loginUser(email, password) {
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpRegisterButtonListener() {
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
} 