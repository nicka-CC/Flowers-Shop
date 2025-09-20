package com.example.myapplicationlab6

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>


    @GET("/users/user")
    fun getUser(): Call<UserResponse>

    @POST("/auth/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @GET("/contacts")
    suspend fun getContacts(): List<Contact>

    @GET("/contacts/in_requests")
    suspend fun getIncomingRequests(): List<ContactRequest>

    @POST("/contacts/accept")
    suspend fun acceptRequest(@Body request: AcceptDeclineRequest): Response<Unit>

    @POST("/contacts/decline")
    suspend fun declineRequest(@Body request: AcceptDeclineRequest): Response<Unit>

    @GET("/users/search")
    suspend fun searchUsers(
        @Query("search") query: String,
        @Query("limit") limit: Int = 50
    ): List<Contact>

    // Отправка запроса на добавление
    @POST("/contacts/add")
    suspend fun sendContactRequest(
        @Body request: AcceptDeclineRequest
    ): Response<Unit>

    @Multipart
    @PATCH("/users/update/avatar")
    suspend fun updateAvatar(
        @Part file: MultipartBody.Part
    ): Response<String>

}