package com.example.crudapplication.data.model


data class ApiResponse<T>(
    var status: String? = null,
    var resultMsg: String? = null,
    var data: T? = null,
) 