package com.example.crudapplication.data.api;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.crudapplication.data.local.AuthAuthenticator;
import com.example.crudapplication.data.local.AuthInterceptor;
import com.example.crudapplication.data.local.TokenManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RetrofitService {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:8080/";   // AVD(에뮬레이터)로만 테스트할때는 localhost대신 10.0.2.2   // 실제 기기로 테스트할땐 내IP주소 넣기

    public static Retrofit getInstance(TokenManager tokenManager,AuthApi authApi){
        if (retrofit == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(tokenManager))  // Access Token 처리
                    .authenticator(new AuthAuthenticator(tokenManager, authApi))    // Refresh Token 처리
                    .build();
//            throw new IllegalStateException("RetrofitClient is not initialized, call initialize(Context) first");

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();
        }
        return retrofit;
    }



    //JSON의 날짜-시간 문자열을 LocalDateTime 객체로 변환
//    static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime>{
//        @Override
//        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{
//            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
//                    .appendFraction(ChronoField.MILLI_OF_SECOND, 0,9,true)
//                    .toFormatter(Locale.ENGLISH);
//
//            return LocalDateTime.parse(json.getAsString(), formatter);
//        }
//    }

    //LocalDateTime 객체를 JSON 문자열로 변환
//    static class LocalDateTimeSerializer implements JsonDeserializer<LocalDateTime>{
//        @Override
//        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{
//            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
//                    .appendFraction(ChronoField.MILLI_OF_SECOND, 0,9,true)
//                    .toFormatter(Locale.ENGLISH);
//
//            return LocalDateTime.parse(json.getAsString(), formatter);
//        }
//    }

}

