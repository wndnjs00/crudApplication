package com.example.crudapplication.presentation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.crudapplication.R;
import com.example.crudapplication.databinding.ActivityMainBinding;
import com.example.crudapplication.databinding.ActivityMyProfileDetailBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyProfileDetailActivity extends AppCompatActivity {
    private ActivityMyProfileDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_detail);

        // View Binding 초기화
        binding = ActivityMyProfileDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }


}