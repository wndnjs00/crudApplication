package com.example.crudapplication.presentation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.crudapplication.R;
import com.example.crudapplication.databinding.ActivityAddUserBinding;
import com.example.crudapplication.presentation.viewmodel.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditUserActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 100;
    private ActivityAddUserBinding binding; // View Binding 객체 재사용
    private UUID userUuid;
    private UserViewModel viewModel;
    private String profileImageBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        initializeViews();
        getData();
        setupEditButtonListener();
        setupProfileImageClick();
    }

    // View Binding 초기화
    private void initializeViews(){
        binding = ActivityAddUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    // 편집버튼 눌렀을떄 각각의 리사이클러뷰 아이템데이터 받아옴 / 받아와서 그 데이터를 뿌려줌
    private void getData(){
        Intent intent = getIntent();
        userUuid = UUID.fromString(intent.getStringExtra("id"));  // id 값 가져오기
        binding.editName.setText(intent.getStringExtra("name"));
        binding.editPhone.setText(intent.getStringExtra("phone"));
        binding.editAddress.setText(intent.getStringExtra("address"));

        // 이미지 데이터를 받아와서 디코딩 후 CircleImageView에 표시
        profileImageBase64 = intent.getStringExtra("profileImage");
        if (profileImageBase64 != null && !profileImageBase64.isEmpty()) {
            try {
                byte[] decodedBytes = Base64.decode(profileImageBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                binding.ivProfile.setImageBitmap(bitmap);
            } catch (IllegalArgumentException e) {
                Log.e("EditUserActivity", "Base64 디코딩 실패: " + e.getMessage());
            }
        }
    }

    // 이미지를 클릭했을 때 갤러리로 이동
    private void setupProfileImageClick() {
        binding.ivProfile.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 100);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                // 이미지 크기 조정
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true); // 300x300 크기로 조정
                binding.ivProfile.setImageBitmap(resizedBitmap);

                // 이미지를 Base64로 인코딩
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream); // 품질을 80으로 설정
                byte[] imageBytes = outputStream.toByteArray();
                profileImageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                Log.d("AddUserActivity", "이미지 Base64로 변환 완료");
            } catch (IOException e) {
                Log.e("AddUserActivity", "이미지 처리 실패: " + e.getMessage());
            }
        }
    }

    private void setupEditButtonListener() {
        binding.saveButton.setOnClickListener(v -> {
            String name = binding.editName.getText().toString();
            String phone = binding.editPhone.getText().toString();
            String address = binding.editAddress.getText().toString();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "모든 값을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel = new ViewModelProvider(this).get(UserViewModel.class);

            // 데이터 수정
            viewModel.updateUser(userUuid, name, phone , address, profileImageBase64, () -> {
                // UI 업데이트는 메인 스레드에서 처리
                runOnUiThread(() -> {
                    Toast.makeText(this, "데이터가 수정되었습니다", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            });

        });
    }
}