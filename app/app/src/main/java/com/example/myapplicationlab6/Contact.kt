package com.example.myapplicationlab6

import com.google.gson.annotations.SerializedName

data class Contact(

    val id: String,
    val name: String,
    val login: String,
    @SerializedName("avatar") val avatarUrl: String? = null
)