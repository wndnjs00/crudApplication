package com.example.crudapplication.data.model

import java.util.UUID


data class UserProfile(
    var uuid: UUID = UUID.randomUUID(),
    var name: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var profileImage: String? = null,
) 