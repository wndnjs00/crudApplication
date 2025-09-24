package com.example.crudapplication.data.dto


data class UserProfileRequestDto(
    val name: String,
    val phone: String,
    val address: String,
    val profileImage: String?,
) 