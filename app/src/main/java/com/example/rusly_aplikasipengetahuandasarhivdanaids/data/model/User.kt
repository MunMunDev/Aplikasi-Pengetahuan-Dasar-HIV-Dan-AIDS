package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model

import com.google.gson.annotations.SerializedName

class User (
    @SerializedName("id")
    val id: Int,

    @SerializedName("umur")
    val umur: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("sebagai")
    val sebagai: String,

    @SerializedName("message")
    val message: String
)