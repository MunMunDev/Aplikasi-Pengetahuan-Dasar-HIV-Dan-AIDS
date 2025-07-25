package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.retrofit

import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.NotificationModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.PushNotificationModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.ResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiConfig {

    // POST
    //Daftar user

    @Headers(
        "Content-Type:application/json",
        "Authorization:key=BAcDd7ysZ1P-lShZw0XaDD1MUGLRYwjl3ocUpXudCQhtF5a1cfnJaTK3xhEzvc0MFdbd3u4UQGVkOTeaf20ZfBM"
    )
    @POST("fcm/send")
    fun postChat(@Body send: PushNotificationModel):Call<PushNotificationModel>

    @FormUrlEncoded
    @POST("rusly/send_chat.php")
    fun postChat(
        @Field("send_chat") send_chat: String,
        @Field("title") title: String,
        @Field("body") body: String,
        @Field("deviceToken") deviceToken: String,
    ): Call<ResponseModel>

    @Multipart
    @POST("rusly/upload_gambar.php")
    fun postGambarChat(
        @Part("post_gambar_chat") post_gambar_chat: RequestBody,
        @Part gambar: MultipartBody.Part,
        @Part("nama") nama: RequestBody,
    ): Call<ArrayList<ResponseModel>>

    @Multipart
    @POST("rusly/upload_gambar.php")
    fun postAdminTambahGambarInformation(
        @Part("post_gambar_chat") post_gambar_chat: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part file_image: MultipartBody.Part,
    ): Call<ArrayList<ResponseModel>>

    @FormUrlEncoded
    @POST("rusly/send_email.php")
    fun postLupaAkun(
        @Field("send_email_password") send_email_password: String,
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<ResponseModel>
}
