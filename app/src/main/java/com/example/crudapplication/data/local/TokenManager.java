package com.example.crudapplication.data.local;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.crudapplication.HiltApplication;
import com.example.crudapplication.presentation.activity.LoginActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton  // 앱 전체에서 단일 인스턴스만 생성되도록 보장
public class TokenManager {
    // SharedPreferences 관련 상수 정의
    private static final String PREF_NAME = "AuthPrefs";              // SharedPreferences 파일 이름
    private static final String KEY_ACCESS_TOKEN  = "access_token";   // access_token을 저장할 키값이름 정의
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    // SharedPreferences 인스턴스 선언(final로 선언하여 한 번 초기화된 후 변경 불가능하도록)
    private final SharedPreferences prefs;
    //LiveData를 사용해 토큰 만료 상태를 관찰 가능하도록 변경하기위해
    private final MutableLiveData<Boolean> tokenExpired = new MutableLiveData<>();

    // Context를 주입받아 SharedPreferences 초기화
    @Inject  // Hilt를 통한 의존성 주입
    public TokenManager(@ApplicationContext Context context) {
        // SharedPreferences 초기화
        // context.getSharedPreferences -> SharedPreferences 인스턴스를 생성하거나, 기존에 존재하면 해당 인스턴스를 반환
        // MODE_PRIVATE: 현재앱에서만 접근가능하도록(외부앱에서는 접근불가)
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // 토큰 저장 메서드 (로그인 성공시 토근저장)
    public void saveTokens(String accessToken, String refreshToken) {
        // 키(KEY_TOKEN)와 값(token) 쌍을 SharedPreferences에 저장
        // apply() -> 변경사항을 비동기적으로 저장
        prefs.edit()
             .putString(KEY_ACCESS_TOKEN, accessToken)
             .putString(KEY_REFRESH_TOKEN, refreshToken)
             .apply();

        // 저장된 토큰 확인 로그 추가
        Log.d("TokenManager", "Access Token: " + getAccessToken());
        Log.d("TokenManager", "Refresh Token: " + getRefreshToken());
    }

    // 저장된 ACCESS_TOKEN 조회 메서드
    public String getAccessToken(){
        // 저장된 토큰 반환, 없으면 null 반환
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    // 저장된 REFRESH_TOKEN 조회 메서드
    public String getRefreshToken(){
        // 저장된 토큰 반환, 없으면 null 반환
        String refreshToken = prefs.getString(KEY_REFRESH_TOKEN, null);
        Log.d("TokenManager", "Current Refresh Token: " + refreshToken);
        return refreshToken;
    }

    // 토큰 삭제 메서드 (로그아웃 시 사용)
    public void clearToken() {
        // 저장된 ACCESS_TOKEN, REFRESH_TOKEN 토큰 값 삭제
        // apply() -> 변경사항을 비동기적으로 저장
        prefs.edit()
             .remove(KEY_ACCESS_TOKEN)
             .remove(KEY_REFRESH_TOKEN)
             .apply();
    }

    // 토큰 존재 여부 확인 메서드
    public boolean hasToken() {
        // Token이 null이 아니면 true 반환 -> Token이 null이 아니면 토큰이 존재한다고 간주
        return getAccessToken() != null;
    }

    public void notifyTokenExpired() {
        tokenExpired.postValue(true); // 토큰 만료 상태를 LiveData로 전달
    }

    // LiveData를 사용해 토큰 만료 상태를 관찰 가능하도록
    public LiveData<Boolean> getTokenExpiredLiveData() {
        return tokenExpired;
    }
}