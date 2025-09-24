package com.example.crudapplication.di

import android.content.Context
import androidx.room.Room
import com.example.crudapplication.data.api.AuthApi
import com.example.crudapplication.data.api.RetrofitService
import com.example.crudapplication.data.api.UserProfileApi
import com.example.crudapplication.data.local.TokenManager
import com.example.crudapplication.data.local.db.AppDatabase
import com.example.crudapplication.data.local.db.UserProfileDao
import com.example.crudapplication.data.repository.AuthUserRepository
import com.example.crudapplication.data.repository.AuthUserRepositoryImpl
import com.example.crudapplication.data.repository.UserRepository
import com.example.crudapplication.data.repository.UserRepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app.db").build()

    @Provides
    @Singleton
    fun provideUserProfileDao(db: AppDatabase): UserProfileDao = db.userProfileDao()

    @Provides
    @Singleton
    fun provideUserRepository(api: UserProfileApi, dao: UserProfileDao): UserRepository = UserRepositoryImpl(api, dao)

    @Provides
    @Singleton
    fun provideUserProfileApi(retrofit: Retrofit): UserProfileApi = retrofit.create(UserProfileApi::class.java)

    @Provides
    @Singleton
    fun provideAuthUserRepository(api: AuthApi, tokenManager: TokenManager): AuthUserRepository =
        AuthUserRepositoryImpl(api, tokenManager)

    @Provides
    @Singleton
    fun provideRetrofit(tokenManager: TokenManager, authApi: AuthApi): Retrofit =
        RetrofitService.getInstance(tokenManager, authApi)

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager = TokenManager(context)
} 