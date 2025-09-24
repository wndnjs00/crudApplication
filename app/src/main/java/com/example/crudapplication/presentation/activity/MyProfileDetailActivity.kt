package com.example.crudapplication.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.crudapplication.R
import com.example.crudapplication.databinding.ActivityMyProfileDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyProfileDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile_detail)

        binding = ActivityMyProfileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
} 