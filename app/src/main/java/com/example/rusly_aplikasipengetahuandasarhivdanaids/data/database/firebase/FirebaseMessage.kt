package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.PushNotificationModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.ShowNotification
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.util.Random

class FirebaseMessage: FirebaseMessagingService() {
    lateinit var database : DatabaseReference
    lateinit var sharedpref : SharedPreferencesLogin
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("messagingToken", "onSuccess2: $token")
        sharedpref = SharedPreferencesLogin(this)
        database = FirebaseService().firebase()
        database.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                database.child(sharedpref.getId()).child("id").setValue(sharedpref.getId())
                database.child(sharedpref.getId()).child("nama").setValue(sharedpref.getNama())
                database.child(sharedpref.getId()).child("umur").setValue(sharedpref.getUmur())
                database.child(sharedpref.getId()).child("username").setValue(sharedpref.getUsername())
                database.child(sharedpref.getId()).child("password").setValue(sharedpref.getPassword())
                database.child(sharedpref.getId()).child("sebagai").setValue(sharedpref.getSebagai())
                database.child(sharedpref.getId()).child("token").setValue(token)

                sharedpref.setLogin(sharedpref.getId(), sharedpref.getNama(),
                    sharedpref.getUmur(), sharedpref.getUsername(),sharedpref.getPassword(),
                    sharedpref.getSebagai(), token)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FirebaseMessage, "Gagal", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onMessageReceived(message: RemoteMessage) {
//        var dataGson =
        var dataGson = "${message.data};;;"
        try{
            super.onMessageReceived(message)
//            dataGson = Gson().toJson(message.data)
//            dataGson = Gson().toJson(message.data)

//            var title = message.notification!!.title.toString()
//            var content = message.notification!!.body.toString()

            val limitGson = dataGson.split(", content=")

            val valueLimitTitle = limitGson[0].split("{title=")
            val valueLimitContent = limitGson[1].split("};;;")

            val title = valueLimitTitle[1]
            val content = valueLimitContent[0]

//        ShowNotification().shotNotification(this, title, content)

            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, "Pengetahuan Dasar HIV AIDS 1")

            builder.apply {
                setSmallIcon(R.drawable.logo_nurse)
                setContentTitle(title)
                setContentText(content)
                setPriority(NotificationCompat.PRIORITY_MAX)
            }

            val manager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                var channelId = "Pengetahuan Dasar HIV AIDS 2"
                var channel = NotificationChannel(
                    channelId,
                    "Pengetahuan Dasar HIV AIDS 3",
                    NotificationManager.IMPORTANCE_HIGH
                )
                manager.createNotificationChannel(channel)
                builder.setChannelId(channelId)
            }

            manager.notify(Random().nextInt(), builder.build())
        }
        catch (ex: Exception){
            Log.d("ExceptionTag", "onMessageReceived: $dataGson dan $ex")
        }
    }

    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)

//        {
//            "data":{
//            "title":"",
//            "content":"Halo bang"
//        },
//            "to":""
//        }

//        c7qrm-kKQJiU4FbuJer-9N:APA91bHAH0TNor3cxU4e0a4J_of3QfSbHzaA8X-vTvOtaB-7YbTkxJyKYJF6WHtkVrk7F6nyTuACiKRX3p4aeqDqNPJPndR2ThLa6X94XHQr9lofdRrCRgW6WAqF43Dq4F0DFlUrcKlj
    }
}