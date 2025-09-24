package com.example.crudapplication.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val tokenExpired: MutableLiveData<Boolean> = MutableLiveData(false)

    fun saveTokens(accessToken: String?, refreshToken: String?) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .apply()
        setTokenExpired(false)
        Log.d("TokenManager", "Access Token: ${accessToken}")
        Log.d("TokenManager", "Refresh Token: ${refreshToken}")
    }

    val accessToken: String?
        get() = prefs.getString(KEY_ACCESS_TOKEN, null)

    val refreshToken: String?
        get() {
            val token = prefs.getString(KEY_REFRESH_TOKEN, null)
            Log.d("TokenManager", "Current Refresh Token: ${token}")
            return token
        }

    fun clearToken() {
        prefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .apply()
        setTokenExpired(true)
    }

    fun setTokenExpired(isExpired: Boolean) {
        tokenExpired.postValue(isExpired)
    }

    fun hasToken(): Boolean = accessToken != null

    fun notifyTokenExpired() {
        tokenExpired.postValue(true)
    }

    fun getTokenExpiredLiveData(): LiveData<Boolean> = tokenExpired

    companion object {
        private const val PREF_NAME = "AuthPrefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }
} 