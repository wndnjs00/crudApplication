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
import com.example.crudapplication.presentation.viewmodel.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailActivity extends AppCompatActivity {
    private TextView tvName, tvPhone, tvAddress;
    private int userId; //id값 저장을 위한

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeViews();
        getData();
        setupEditButton();
    }

    // UI요소 초기화
    private void initializeViews(){
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone_value);
        tvAddress = findViewById(R.id.tv_address_value);
    }

    // MainActivity에서 전달한 데이터받아와서 뿌려줌
    private void getData(){
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1); // id 값 가져오기
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");

        tvName.setText(name);
        tvPhone.setText(phone);
        tvAddress.setText(address);
    }


    // 편집버튼 눌렀을때
    private void setupEditButton() {
        Button editButton = findViewById(R.id.btn_edit);
        editButton.setOnClickListener(v -> {
            // EditUserActivity로 이동하면서 데이터 전달
            Intent intent = new Intent(DetailActivity.this, EditUserActivity.class);
            intent.putExtra("id", userId);  // id 값 전달
            intent.putExtra("name", tvName.getText().toString());
            intent.putExtra("phone", tvPhone.getText().toString());
            intent.putExtra("address", tvAddress.getText().toString());
//            Log.d("id", String.valueOf(userId));
//            Log.d("name", tvName.getText().toString());
//            Log.d("phone", tvPhone.getText().toString());
//            Log.d("address", tvAddress.getText().toString());
            startActivity(intent);
        });
    }
}