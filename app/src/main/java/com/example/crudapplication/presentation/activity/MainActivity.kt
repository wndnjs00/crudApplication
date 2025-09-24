package com.example.crudapplication.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudapplication.R
import com.example.crudapplication.databinding.ActivityMainBinding
import com.example.crudapplication.presentation.adpater.UserAdapter
import com.example.crudapplication.presentation.viewmodel.AuthViewModel
import com.example.crudapplication.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupLogoutListener()
        observeTokenExpiration()
        clickFabButton()
        setupRecyclerView()
        setupLongClickListener()
        setupClickListener()
        setupSwipeRefreshLayout()
        setupDeleteAccountListener()

        viewModel.AllFetchUsers()
    }

    private fun clickFabButton() {
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.AllFetchUsers()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel.getUserList().observe(this) { users ->
            adapter.setUsers(users)
        }
    }

    private fun setupClickListener() {
        adapter.setOnItemClickListener { user ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("uuid", user.uuid.toString())
            intent.putExtra("name", user.name)
            intent.putExtra("phone", user.phone)
            intent.putExtra("address", user.address)
            intent.putExtra("profileImage", user.profileImage)
            startActivity(intent)
        }
    }

    private fun setupLongClickListener() {
        adapter.setOnItemLongClickListener { user ->
            viewModel.deleteUser(user.uuid) {
                Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupLogoutListener() {
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.logoutText.setOnClickListener {
            authViewModel.logout({
                Toast.makeText(this, "로그아웃 완료", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }) {
                Toast.makeText(this, "로그아웃 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeTokenExpiration() {
        authViewModel.getTokenExpiredLiveData().observe(this) { isExpired ->
            if (isExpired == true) {
                Toast.makeText(this, "다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupDeleteAccountListener() {
        binding.deleteAccountText.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("회원탈퇴")
                .setMessage("정말로 계정을 삭제하시겠습니까?")
                .setPositiveButton("확인") { _, _ ->
                    authViewModel.deleteAccount({
                        Toast.makeText(this, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }) {
                        Toast.makeText(this, "회원탈퇴에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("취소", null)
                .show()
        }
    }
} 