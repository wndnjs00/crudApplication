package com.example.crudapplication.presentation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.crudapplication.data.model.UserProfile;
import com.example.crudapplication.databinding.ActivityAddUserBinding;
import com.example.crudapplication.presentation.viewmodel.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddUserActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 100;
    private ActivityAddUserBinding binding;
    private UserViewModel viewModel;    // ViewModel 선언
    private String profileImageBase64; // Base64로 인코딩된 이미지 데이터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        initializeViews();
        setupSaveButtonListener();
        setupProfileImageClick();
    }


    // View Binding 초기화
    private void initializeViews() {
        binding = ActivityAddUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    // 이미지 클릭했을때 갤러리로 이동
    private void setupProfileImageClick() {
        binding.ivProfile.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
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


    private void setupSaveButtonListener() {
        binding.saveButton.setOnClickListener(v -> {
            // 입력값 가져오기
            String name = binding.editName.getText().toString();
            String phone = binding.editPhone.getText().toString();
            String address = binding.editAddress.getText().toString();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "모든값을 입력해주세요", Toast.LENGTH_SHORT).show();
            }else{
                // ViewModelProvider로 ViewModel 초기화 (Hilt를 사용한 ViewModel은 반드시 ViewModelProvider로 주입받아야함)
                viewModel = new ViewModelProvider(this).get(UserViewModel.class);

                // 데이터 저장
                viewModel.addUser(name, phone, address, profileImageBase64, () -> {
                    // UI 업데이트는 메인 스레드에서 처리
                    runOnUiThread(() -> {
                        Toast.makeText(this, "데이터가 추가되었습니다", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish(); // 현재 Activity 종료
                    });
                });
            }
        });
    }
}


