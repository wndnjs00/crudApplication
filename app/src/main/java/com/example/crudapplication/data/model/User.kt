package com.example.crudapplication.data.model

import java.util.UUID


data class User(
    var uuid: UUID = UUID.randomUUID(),
    var email: String? = null,
    var password: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var address: String? = null,
) 