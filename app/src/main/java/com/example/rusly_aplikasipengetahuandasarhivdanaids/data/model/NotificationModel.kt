package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model

import com.google.gson.annotations.SerializedName

class NotificationModel(
    @SerializedName("title")
    var title: String,

    @SerializedName("content")
    var content: String


)