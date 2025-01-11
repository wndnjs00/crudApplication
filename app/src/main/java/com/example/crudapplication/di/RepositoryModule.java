package com.example.crudapplication.di;

import com.example.crudapplication.data.api.AuthApi;
import com.example.crudapplication.data.api.RetrofitService;
import com.example.crudapplication.data.api.UserProfileApi;
import com.example.crudapplication.data.repository.AuthUserRepository;
import com.example.crudapplication.data.repository.AuthUserRepositoryImpl;
import com.example.crudapplication.data.repository.UserRepository;
import com.example.crudapplication.data.repository.UserRepositoryImpl;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

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
    public UserProfileApi provideUserProfileApi(){
        return RetrofitService.getInstance().create(UserProfileApi.class);
    }


    @Provides
    @Singleton
    public AuthUserRepository provideAuthUserRepository(AuthApi api){
        return new AuthUserRepositoryImpl(api);
    }

    @Provides
    @Singleton
    public AuthApi provideAuthApi(){
        return RetrofitService.getInstance().create(AuthApi.class);
    }
}
