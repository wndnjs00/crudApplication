package com.example.crudapplication.data.dto


data class RegisterRequestDto(
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
    val address: String,
) 