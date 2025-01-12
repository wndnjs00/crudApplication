package com.example.crudapplication.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.crudapplication.R;
import com.example.crudapplication.data.local.TokenManager;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {
    
    @Inject 
    // TokenManager 인스턴스를 Hilt를 통해 주입받음
    TokenManager tokenManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setDelay(2000); // 2초 지연 (2초(2000ms) 후에 실행)
    }

    private void setDelay(int delayMillis) {
        // Handler -> 스레드에서 작업을 지연시킬때 사용
        // postDelayed -> 설정한 지연시간(delayMillis)후애 주어진 코드 실행
        new Handler().postDelayed(() -> {
            Intent intent;

            // 저장된 토큰이 있는지 확인
            if (tokenManager.hasToken()) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, RegisterActivity.class);
            }
            startActivity(intent);
            finish();
        }, delayMillis);
    }
} 