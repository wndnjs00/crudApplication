package com.example.crudapplication.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.crudapplication.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailActivity extends AppCompatActivity {
    private TextView tvName, tvPhone, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeViews();
        getData();
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
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");

        tvName.setText(name);
        tvPhone.setText(phone);
        tvAddress.setText(address);
    }
}