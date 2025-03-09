package com.example.crudapplication.di;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.crudapplication.data.api.AuthApi;
import com.example.crudapplication.data.api.RetrofitService;
import com.example.crudapplication.data.api.UserProfileApi;
import com.example.crudapplication.data.local.TokenManager;
import com.example.crudapplication.data.repository.AuthUserRepository;
import com.example.crudapplication.data.repository.AuthUserRepositoryImpl;
import com.example.crudapplication.data.repository.UserRepository;
import com.example.crudapplication.data.repository.UserRepositoryImpl;
import com.google.gson.Gson;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Provides
    @Singleton
    public UserRepository provideUserRepository(UserProfileApi api){
        return new UserRepositoryImpl(api);
    }

    @Provides
    @Singleton
    public UserProfileApi provideUserProfileApi(Retrofit retrofit) {
        return retrofit.create(UserProfileApi.class);
    }


    @Provides
    @Singleton
    public AuthUserRepository provideAuthUserRepository(AuthApi api, TokenManager tokenManager){
        return new AuthUserRepositoryImpl(api, tokenManager);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Provides
    @Singleton
    public Retrofit provideRetrofit(TokenManager tokenManager, AuthApi authApi) {
        return RetrofitService.getInstance(tokenManager, authApi);
    }

    @Provides
    @Singleton
    public AuthApi provideAuthApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/") // AVD IP
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        return retrofit.create(AuthApi.class);
    }

    @Provides
    @Singleton
    public TokenManager provideTokenManager(@ApplicationContext Context context) {
        return new TokenManager(context);
    }
}
