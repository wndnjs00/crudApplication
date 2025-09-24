package com.example.crudapplication.data.local

import android.util.Log
import com.example.crudapplication.data.api.AuthApi
import com.example.crudapplication.data.model.ApiResponse
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Call

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

        val call: Call<ApiResponse<Map<String, String>>> = authApi.refreshToken(body)
        return try {
            val tokenResponse = call.execute()
            if (tokenResponse.isSuccessful && tokenResponse.body() != null) {
                val tokens = tokenResponse.body()!!.data
                val newAccessToken = tokens?.get("accessToken")
                val newRefreshToken = tokens?.get("refreshToken")

                Log.d("AuthAuthenticator", "New Access Token: $newAccessToken")
                Log.d("AuthAuthenticator", "New Refresh Token: $newRefreshToken")

                tokenManager.saveTokens(newAccessToken, newRefreshToken)

                response.request().newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            } else {
                tokenManager.notifyTokenExpired()
                null
            }
        } catch (e: Exception) {
            Log.e("AuthAuthenticator", "Error refreshing token: ${e.message}", e)
            tokenManager.notifyTokenExpired()
            null
        } finally {
            call.cancel()
        }
    }
} 