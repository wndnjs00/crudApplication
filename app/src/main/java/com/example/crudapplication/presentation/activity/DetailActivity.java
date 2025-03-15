package com.example.crudapplication.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.crudapplication.R;
import com.example.crudapplication.databinding.ActivityDetailBinding;
import com.example.crudapplication.presentation.viewmodel.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding; // View Binding 객체 선언
    private String uuidString; //id값 저장을 위한

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeViews();
        getData();
        setupEditButton();
    }

    // View Binding 초기화
    private void initializeViews(){
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    // MainActivity에서 전달한 데이터받아와서 뿌려줌
    private void getData(){
        Intent intent = getIntent();
        uuidString = intent.getStringExtra("uuid");
        binding.tvName.setText(intent.getStringExtra("name"));
        binding.tvPhoneValue.setText(intent.getStringExtra("phone"));
        binding.tvAddressValue.setText(intent.getStringExtra("address"));
    }


    // 편집버튼 눌렀을때
    private void setupEditButton() {
        Button editButton = findViewById(R.id.btn_edit);
        editButton.setOnClickListener(v -> {
            // EditUserActivity로 이동하면서 데이터 전달
            Intent intent = new Intent(DetailActivity.this, EditUserActivity.class);
            intent.putExtra("id", uuidString);  // id 값 전달
            intent.putExtra("name", binding.tvName.getText().toString());
            intent.putExtra("phone", binding.tvPhoneValue.getText().toString());
            intent.putExtra("address", binding.tvAddressValue.getText().toString());
//            Log.d("id", String.valueOf(userId));
//            Log.d("name", tvName.getText().toString());
//            Log.d("phone", tvPhone.getText().toString());
//            Log.d("address", tvAddress.getText().toString());
            startActivity(intent);
        });
    }
}