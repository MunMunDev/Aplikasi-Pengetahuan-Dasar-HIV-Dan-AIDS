package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model

import com.google.gson.annotations.SerializedName

class PushNotificationModel(
    @SerializedName("data")
    var data: NotificationModel,

    @SerializedName("to")
    var to: String

)