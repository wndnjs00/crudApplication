package com.example.crudapplication.presentation.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.crudapplication.R;
import com.example.crudapplication.presentation.adpater.UserAdapter;
import com.example.crudapplication.presentation.viewmodel.AuthViewModel;
import com.example.crudapplication.presentation.viewmodel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private UserViewModel viewModel;
    private AuthViewModel authViewModel;
    private UserAdapter adapter;
    private TextView logoutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickFabButton();
        setupRecyclerView();
        setupViewModel();
        setupLongClickListener();
        setupClickListener();
        setupLogoutListener();
        observeTokenExpiration();

        viewModel.AllFetchUsers();  //데이터 새로고침(전체 데이터조회)
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.AllFetchUsers(); // 화면 복귀 시 데이터 새로고침
    }

    private void clickFabButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> IntentAddUserActivity());
    }

    private void IntentAddUserActivity() {
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivity(intent);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new UserAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
        logoutText = findViewById(R.id.logout_text);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        logoutText.setOnClickListener(v -> {
            authViewModel.logout(); //로그아웃 처리
            Intent intent = new Intent(MainActivity.this , LoginActivity.class);
            startActivity(intent);
            finish(); // 현재 액티비티 종료
        });
    }

    private void observeTokenExpiration() {
        authViewModel.getTokenExpiredLiveData().observe(this, isExpired -> {
            if (Boolean.TRUE.equals(isExpired)) {
                Toast.makeText(this, "토큰이 만료되었습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

}
