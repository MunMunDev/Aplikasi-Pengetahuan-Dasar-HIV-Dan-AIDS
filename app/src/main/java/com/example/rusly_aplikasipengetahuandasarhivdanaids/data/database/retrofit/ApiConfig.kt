package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.retrofit

import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.NotificationModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.PushNotificationModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiConfig {

    // POST
    //Daftar user

    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAAOVHRxV4:APA91bGqvH35P8L77ZH-YG2N2eU1OqzvL5qfZ4KSW1CYixNCYJeYTHvLGNL-PDcxoEz55rWrQagxEaR4pn5fMkgOc7zhBp6Bw5njHDgblAxDOeaGK_SnW_BwT7DWemmcd8ISNg_BdPrd"
    )
    @POST("fcm/send")
    fun postChat(@Body send: PushNotificationModel):Call<PushNotificationModel>

}
