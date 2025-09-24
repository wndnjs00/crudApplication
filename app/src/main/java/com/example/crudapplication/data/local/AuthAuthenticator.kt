package com.example.crudapplication.data.local

import android.util.Log
import com.example.crudapplication.data.api.AuthApi
import com.example.crudapplication.data.model.ApiResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthAuthenticator(
    private val tokenManager: TokenManager,
    private val authApi: AuthApi?
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (authApi == null) {
            Log.e("AuthAuthenticator", "AuthApi is null")
            return null
        }

        val refreshToken = tokenManager.refreshToken
        if (refreshToken == null) {
            tokenManager.notifyTokenExpired()
            return null
        }

        val body: MutableMap<String, String> = HashMap()
        body["refreshToken"] = refreshToken

        return try {
            val tokenResponse: ApiResponse<Map<String, String>> = runBlocking {
                authApi.refreshToken(body)
            }
            val tokens = tokenResponse.data
            val newAccessToken = tokens?.get("accessToken")
            val newRefreshToken = tokens?.get("refreshToken")

            Log.d("AuthAuthenticator", "New Access Token: $newAccessToken")
            Log.d("AuthAuthenticator", "New Refresh Token: $newRefreshToken")

            tokenManager.saveTokens(newAccessToken, newRefreshToken)

            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
        } catch (e: Exception) {
            Log.e("AuthAuthenticator", "Error refreshing token: ${e.message}", e)
            tokenManager.notifyTokenExpired()
            null
        }
    }
} 