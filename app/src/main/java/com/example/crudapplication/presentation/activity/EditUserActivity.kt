package com.example.crudapplication.presentation.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.crudapplication.R
import com.example.crudapplication.databinding.ActivityAddUserBinding
import com.example.crudapplication.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.UUID

@AndroidEntryPoint
class EditUserActivity : AppCompatActivity() {
    private val GALLERY_REQUEST_CODE = 100
    private lateinit var binding: ActivityAddUserBinding
    private lateinit var userUuid: UUID
    private lateinit var viewModel: UserViewModel
    private var profileImageBase64: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        initializeViews()
        getData()
        setupEditButtonListener()
        setupProfileImageClick()
    }

    private fun initializeViews() {
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getData() {
        val intent = intent
        userUuid = UUID.fromString(intent.getStringExtra("id"))
        binding.editName.setText(intent.getStringExtra("name"))
        binding.editPhone.setText(intent.getStringExtra("phone"))
        binding.editAddress.setText(intent.getStringExtra("address"))
        profileImageBase64 = intent.getStringExtra("profileImage")
        if (!profileImageBase64.isNullOrEmpty()) {
            try {
                val decodedBytes = Base64.decode(profileImageBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                binding.ivProfile.setImageBitmap(bitmap)
            } catch (e: IllegalArgumentException) {
                Log.e("EditUserActivity", "Base64 디코딩 실패: ${e.message}")
            }
        }
    }

    private fun setupProfileImageClick() {
        binding.ivProfile.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
                binding.ivProfile.setImageBitmap(resizedBitmap)

                val outputStream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                val imageBytes = outputStream.toByteArray()
                profileImageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT)

                Log.d("AddUserActivity", "이미지 Base64로 변환 완료")
            } catch (e: IOException) {
                Log.e("AddUserActivity", "이미지 처리 실패: ${e.message}")
            }
        }
    }

    private fun setupEditButtonListener() {
        binding.saveButton.setOnClickListener {
            val name = binding.editName.text.toString()
            val phone = binding.editPhone.text.toString()
            val address = binding.editAddress.text.toString()

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "모든 값을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel = ViewModelProvider(this)[UserViewModel::class.java]
            viewModel.updateUser(userUuid, name, phone, address, profileImageBase64) {
                runOnUiThread {
                    Toast.makeText(this, "데이터가 수정되었습니다", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
} 