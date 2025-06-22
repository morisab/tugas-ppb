package com.example.starbucks.data.model

data class UserProfile(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val birthDate: String = "",
    val passwordHash: String = ""
)

data class User(
    val id: String,
    val email: String,
    val passwordHash: String,
    val profile: UserProfile
)