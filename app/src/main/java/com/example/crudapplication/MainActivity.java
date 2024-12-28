package com.example.crudapplication;

import static android.icu.number.NumberRangeFormatter.with;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private UserViewModel viewModel;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickFabButton();
        setupRecyclerView();
        setupViewModel();
        setupLongClickListener();

        viewModel.AllFetchUsers();  //데이터 새로고침(전체 데이터조회)
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

    // viewModel을 통해 getUserList을 실시간으로 observe해서 adapter에 데이터를 뿌려줌
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);   // ViewModel 초기화
        viewModel.getUserList().observe(this, users -> adapter.setUsers(users));
    }

    private void setupLongClickListener() {
        viewModel.getUserList().observe(this, users -> adapter.setUsers(users));

        adapter.setOnItemLongClickListener(user -> {
            viewModel.deleteUser(user.getId(), () ->
                    Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()
            );
        });
    }
}
