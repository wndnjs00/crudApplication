package com.example.crudapplication.presentation.activity;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.crudapplication.R;
import com.example.crudapplication.databinding.ActivityMainBinding;
import com.example.crudapplication.presentation.adpater.UserAdapter;
import com.example.crudapplication.presentation.viewmodel.AuthViewModel;
import com.example.crudapplication.presentation.viewmodel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding; // View Binding 객체 선언
    private UserViewModel viewModel;
    private AuthViewModel authViewModel;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View Binding 초기화
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        setupLogoutListener();
        observeTokenExpiration();

        clickFabButton();
        setupRecyclerView();
        setupLongClickListener();
        setupClickListener();
        setupSwipeRefreshLayout();
        setupDeleteAccountListener();

        viewModel.AllFetchUsers();  //데이터 새로고침(전체 데이터조회)
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.AllFetchUsers(); // 화면 복귀 시 데이터 새로고침
    }

    private void clickFabButton() {
        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddUserActivity.class);
            startActivity(intent);
        });
    }

    private void setupSwipeRefreshLayout() {

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.AllFetchUsers();

            // 새로고침 완료 후 로딩 상태 해제
            binding.swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setupRecyclerView() {
        adapter = new UserAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    // viewModel을 통해 getUserList을 실시간으로 observe해서 adapter에 데이터를 뿌려줌 (LiveData를 통해 변경되는 데이터를 실시간으로 관찰)
    private void setupViewModel() {
        // ViewModelProvider로 ViewModel 초기화
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        viewModel.getUserList().observe(this, users ->
                adapter.setUsers(users)
        );
    }

    private void setupClickListener(){
        adapter.setOnItemClickListener(user -> {
            // DetailActivity로 이동하면서 데이터 전달
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("uuid", user.getUuid().toString()); // id 값 추가
            intent.putExtra("name", user.getName());
            intent.putExtra("phone", user.getPhone());
            intent.putExtra("address", user.getAddress());
            intent.putExtra("profileImage", user.getProfileImage());
            startActivity(intent);
        });
    }

    private void setupLongClickListener() {
        adapter.setOnItemLongClickListener(user -> {
            viewModel.deleteUser(user.getUuid(), () ->
                    Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()
            );
        });
    }

    private void setupLogoutListener(){

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.logoutText.setOnClickListener(v -> {
            authViewModel.logout(
                    // 로그아웃 성공 시 처리
                    () -> {
                        Toast.makeText(this, "로그아웃 완료", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    },
                    // 로그아웃 실패 시 처리
                    () -> Toast.makeText(this, "로그아웃 실패", Toast.LENGTH_SHORT).show()
            );
        });
    }

    private void observeTokenExpiration() {
        authViewModel.getTokenExpiredLiveData().observe(this, isExpired -> {
            if (Boolean.TRUE.equals(isExpired)) {
                Toast.makeText(this, "다시 로그인해주세요.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    // 회원탈퇴 텍스트 클릭시
    private void setupDeleteAccountListener() {
        binding.deleteAccountText.setOnClickListener(v -> {
            // 다이얼로그 표시
            new AlertDialog.Builder(this)
                    .setTitle("회원탈퇴")
                    .setMessage("정말로 계정을 삭제하시겠습니까?")
                    .setPositiveButton("확인", (dialog, which) -> {
                        // 회원탈퇴 요청
                        authViewModel.deleteAccount(
                                // 성공 처리
                                () -> {
                                    Toast.makeText(this, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, LoginActivity.class);
                                    startActivity(intent);
                                    finish(); // 현재 액티비티 종료
                                },
                                // 실패 처리
                                () -> {
                                    Toast.makeText(this, "회원탈퇴에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                }
                        );
                    })
                    .setNegativeButton("취소", null) // 취소 시 아무 작업 없음
                    .show();
        });
    }

}
