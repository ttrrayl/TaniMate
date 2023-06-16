package com.example.tanimate.data.local

data class UserModel(
    val name: String,
    val isLogin: Boolean,
    val token: String = ""
)
