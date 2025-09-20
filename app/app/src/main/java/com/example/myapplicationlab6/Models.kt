package com.example.myapplicationlab6

data class LoginResponse(
    val token: String // JWT-токен, возвращаемый сервером
)

data class UserResponse(
    val id: String,
    val name: String,
    val avatar: String
)

data class RegisterRequest(
    val login: String,
    val password: String,
    val name: String
)

data class RegisterResponse(
    val token: String
)

data class LoginRequest(
    val login: String,
    val password: String
)

data class ContactRequest(
    val requestId: String,
    val userId: String,
    val name: String,
    val login: String,
    val avatar: String?,
    val status: String
)

data class AcceptDeclineRequest(
    val requestId: String
)
