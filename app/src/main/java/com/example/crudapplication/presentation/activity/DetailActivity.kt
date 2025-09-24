package com.example.crudapplication.presentation.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.crudapplication.R
import com.example.crudapplication.databinding.ActivityDetailBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var uuidString: String? = null
    private var profileImageBase64: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initializeViews()
        getData()
        setupEditButton()
        setupCallButton()
        setupMessageButton()
        displayProfileImage()
    }

    private fun initializeViews() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getData() {
        val intent = intent
        uuidString = intent.getStringExtra("uuid")
        binding.tvName.text = intent.getStringExtra("name")
        binding.tvPhoneValue.text = intent.getStringExtra("phone")
        binding.tvAddressValue.text = intent.getStringExtra("address")
        profileImageBase64 = intent.getStringExtra("profileImage")
    }

    private fun setupEditButton() {
        val editButton: FloatingActionButton = findViewById(R.id.btn_edit)
        editButton.setOnClickListener {
            val intent = Intent(this@DetailActivity, EditUserActivity::class.java)
            intent.putExtra("id", uuidString)
            intent.putExtra("name", binding.tvName.text.toString())
            intent.putExtra("phone", binding.tvPhoneValue.text.toString())
            intent.putExtra("address", binding.tvAddressValue.text.toString())
            intent.putExtra("profileImage", profileImageBase64)
            startActivity(intent)
        }
    }

    private fun displayProfileImage() {
        if (!profileImageBase64.isNullOrEmpty()) {
            try {
                val decodedBytes = Base64.decode(profileImageBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                binding.ivProfile.setImageBitmap(bitmap)
            } catch (e: IllegalArgumentException) {
                Log.e("DetailActivity", "Base64 디코딩 실패: ${e.message}")
            }
        }
    }

    private fun setupCallButton() {
        val phoneNumber = { binding.tvPhoneValue.text.toString() }
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_call).setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:" + phoneNumber())
            startActivity(callIntent)
        }
    }

    private fun setupMessageButton() {
        val phoneNumber = { binding.tvPhoneValue.text.toString() }
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_message).setOnClickListener {
            val messageIntent = Intent(Intent.ACTION_SENDTO)
            messageIntent.data = Uri.parse("smsto:" + phoneNumber())
            startActivity(messageIntent)
        }
    }
} 