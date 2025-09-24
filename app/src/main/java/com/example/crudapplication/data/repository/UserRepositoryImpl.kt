package com.example.crudapplication.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.crudapplication.data.api.UserProfileApi
import com.example.crudapplication.data.dto.UserProfileRequestDto
import com.example.crudapplication.data.model.ApiResponse
import com.example.crudapplication.data.model.UserProfile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: UserProfileApi
) : UserRepository {

    override fun AllFetchUsers(liveData: MutableLiveData<List<UserProfile>>) {
        api.getAllUsers().enqueue(object : Callback<ApiResponse<List<UserProfile>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<UserProfile>>>,
                response: Response<ApiResponse<List<UserProfile>>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    liveData.value = apiResponse.data
                    Log.d("전체 데이터조회 성공", "전체 데이터 조회 성공")
                } else {
                    Log.e("전체 데이터조회 실패", "전체 데이터 조회 실패: ${response.code()}-${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<List<UserProfile>>>,
                t: Throwable
            ) {
                Log.e("API 연결실패1", t.message ?: "")
            }
        })
    }

    override fun createUser(user: UserProfile, onSuccess: Runnable) {
        Log.d("요청 JSON", "UserProfile : ${user}")
        val dto = UserProfileRequestDto(user.name ?: "", user.phone ?: "", user.address ?: "", user.profileImage)
        api.createUser(dto).enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(
                call: Call<ApiResponse<Void>>, response: Response<ApiResponse<Void>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("데이터 저장 성공", "데이터 저장 성공")
                    onSuccess.run()
                } else {
                    Log.e("데이터 저장 실패", "데이터 저장 실패: ${response.code()}-${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                Log.e("API 연결실패2", t.message ?: "")
            }
        })
    }

    override fun updateUser(
        uuid: UUID,
        name: String,
        phone: String,
        address: String,
        profileImage: String?,
        onSuccess: Runnable
    ) {
        val dto = UserProfileRequestDto(name, phone, address, profileImage)
        api.updateUser(uuid, dto).enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(
                call: Call<ApiResponse<Void>>, response: Response<ApiResponse<Void>>
            ) {
                if (response.isSuccessful) {
                    Log.d("데이터 수정 성공", "데이터 수정 성공")
                    onSuccess.run()
                } else {
                    Log.e("데이터 수정 실패", "데이터 수정 실패: ${response.code()}-${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                Log.e("API 연결실패3", t.message ?: "")
            }
        })
    }

    override fun deleteUser(uuid: UUID, onSuccess: Runnable) {
        api.deleteUser(uuid).enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(
                call: Call<ApiResponse<Void>>, response: Response<ApiResponse<Void>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("데이터 삭제 성공", "데이터 삭제 성공")
                    onSuccess.run()
                } else {
                    Log.e("데이터 삭제 실패", "데이터 삭제 실패: ${response.code()}-${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                Log.e("API 연결실패4", t.message ?: "")
            }
        })
    }
} 