package com.example.crudapplication.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.crudapplication.data.api.UserProfileApi
import com.example.crudapplication.data.dto.UserProfileRequestDto
import com.example.crudapplication.data.local.db.UserProfileDao
import com.example.crudapplication.data.local.db.UserProfileEntity
import com.example.crudapplication.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: UserProfileApi,
    private val userDao: UserProfileDao,
) : UserRepository {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private fun UserProfile.toEntity(): UserProfileEntity =
        UserProfileEntity(
            uuid = (this.uuid ?: UUID.randomUUID()).toString(),
            name = this.name,
            phone = this.phone,
            address = this.address,
            profileImage = this.profileImage,
        )

    private fun UserProfileEntity.toModel(): UserProfile =
        UserProfile(
            uuid = UUID.fromString(this.uuid),
            name = this.name,
            phone = this.phone,
            address = this.address,
            profileImage = this.profileImage,
        )

    override fun AllFetchUsers(liveData: MutableLiveData<List<UserProfile>>) {
        // DB observe → UI 업데이트
        ioScope.launch {
            userDao.getAll().collectLatest { list ->
                val mapped = list.map { it.toModel() }
                liveData.postValue(mapped)
            }
        }
        // 네트워크 → DB upsert
        ioScope.launch {
            try {
                val response = api.getAllUsers()
                val data = response.data ?: emptyList()
                val entities = data.map { it.toEntity() }
                userDao.upsertAll(entities)
                Log.d("전체 데이터조회 성공", "네트워크 동기화 완료")
            } catch (e: Exception) {
                Log.e("전체 데이터조회 실패", e.message ?: "")
            }
        }
    }

    override fun createUser(user: UserProfile, onSuccess: Runnable) {
        ioScope.launch {
            try {
                val dto = UserProfileRequestDto(user.name ?: "", user.phone ?: "", user.address ?: "", user.profileImage)
                api.createUser(dto)
                // 성공 후 최신 목록 동기화
                val response = api.getAllUsers()
                val entities = (response.data ?: emptyList()).map { it.toEntity() }
                userDao.upsertAll(entities)
                onSuccess.run()
                Log.d("데이터 저장 성공", "데이터 저장 + 캐시 반영")
            } catch (e: Exception) {
                Log.e("데이터 저장 실패", e.message ?: "")
            }
        }
    }

    override fun updateUser(
        uuid: UUID,
        name: String,
        phone: String,
        address: String,
        profileImage: String?,
        onSuccess: Runnable
    ) {
        ioScope.launch {
            try {
                val dto = UserProfileRequestDto(name, phone, address, profileImage)
                api.updateUser(uuid, dto)
                val response = api.getAllUsers()
                val entities = (response.data ?: emptyList()).map { it.toEntity() }
                userDao.upsertAll(entities)
                onSuccess.run()
                Log.d("데이터 수정 성공", "데이터 수정 + 캐시 반영")
            } catch (e: Exception) {
                Log.e("데이터 수정 실패", e.message ?: "")
            }
        }
    }

    override fun deleteUser(uuid: UUID, onSuccess: Runnable) {
        ioScope.launch {
            try {
                api.deleteUser(uuid)
                userDao.deleteById(uuid.toString())
                onSuccess.run()
                Log.d("데이터 삭제 성공", "네트워크 + 캐시 반영")
            } catch (e: Exception) {
                Log.e("데이터 삭제 실패", e.message ?: "")
            }
        }
    }
} 