package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.retrofit

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private const val BASE_URL_MESSAGE = "https://fcm.googleapis.com/"  //Message

    fun getRetrofit(): ApiConfig {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL_MESSAGE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(ApiConfig::class.java)
    }

    const val BASE_URL_MYSQL = "https://e-portofolio.web.id/"
    fun getRetrofitMySql(): ApiConfig {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL_MYSQL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(ApiConfig::class.java)
    }

}