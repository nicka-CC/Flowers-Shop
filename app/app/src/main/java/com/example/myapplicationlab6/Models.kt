package com.example.myapplicationlab6

data class LoginResponse(
    val access_token: String // JWT-токен, возвращаемый сервером
)

data class UserResponse(
    val id: String,
    val name: String,
    val avatar: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val phone: String
)

data class UserRegisterResponse(
    val id: Int,
    val permission: Int,
    val email: String,
    val name: String,
    val surname: String,
    val phone: String,
    val password: String,
    val face: String,
    val date_created: String,
    val date_updated: String
)

data class RegisterResponse(
    val message: String,
    val user: UserRegisterResponse
)

data class LoginRequest(
    val identifier: String,
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
