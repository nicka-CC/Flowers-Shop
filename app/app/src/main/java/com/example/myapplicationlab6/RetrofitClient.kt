package com.example.myapplicationlab6

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.myapplicationlab6.ApiService

object RetrofitClient {
    private const val BASE_URL = "https://api.nogamenolife.pro/"

    private fun getToken(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPrefs.getString("jwt_token", null)
    }

    private fun createOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = getToken(context)
                val request = chain.request().newBuilder()
                if (token != null) {
                    request.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(request.build())
            }
            .build()
    }

    fun createApiService(context: Context): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient(context))  // Attach OkHttpClient with auth interceptor
            .build()
            .create(ApiService::class.java)
    }
}