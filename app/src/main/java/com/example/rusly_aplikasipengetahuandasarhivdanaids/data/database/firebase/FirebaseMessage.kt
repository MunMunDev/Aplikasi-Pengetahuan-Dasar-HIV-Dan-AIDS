package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.PushNotificationModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.ChatActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.KonsultasiActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user.UpdateAkunActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.ShowNotification
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.remoteMessage
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
                    sharedpref.getUmur(), sharedpref.getEmail(), sharedpref.getUsername(), sharedpref.getPassword(),
                    sharedpref.getSebagai(), token)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FirebaseMessage, "Gagal", Toast.LENGTH_SHORT).show()
            }

        })
    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Tampilkan Toast (Pop Up)
        val title = message.notification?.title ?: "Pesan baru"
        val body = message.notification?.body ?: "Pesan baru"
        try {
//            Toast.makeText(applicationContext, body, Toast.LENGTH_LONG).show()
            Log.d("MessageTAG", "Title: $title")
            Log.d("MessageTAG", "Body: $body")

            // Tampilkan Notifikasi
            if (message.notification != null) {
                showNotification(
                    message.notification!!.title ?: "Pesan Baru",
                    message.notification!!.body ?: "",
                    applicationContext
                )
            }
        } catch (ex: Exception){
            Log.d("MessageTAG", "Error: ${ex.message}")
        }
    }

    fun showNotification(title: String, message: String, context: Context) {
        val arrayContent = message.split(";-;")
        val isiChat = arrayContent.getOrNull(0) ?: message
        val id = arrayContent.getOrNull(1) ?: ""
        val token = arrayContent.getOrNull(2) ?: ""

        val channelId = "channel_id_hiv_aids"
        val builder = NotificationCompat.Builder(context, channelId)

        val i = Intent(context, ChatActivity::class.java)
        i.putExtra("id", id)
        i.putExtra("nama", title)
        i.putExtra("token", token)
        val p = PendingIntent.getActivity(
            context, 0, i,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        builder.apply {
            setSmallIcon(R.drawable.logo_nurse)
            setContentTitle(title)
            setContentText(isiChat)
            setPriority(NotificationCompat.PRIORITY_MAX)
            setContentIntent(p)
            setAutoCancel(true)
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "channel_id_hiv_aids",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }

        manager.notify(Random().nextInt(), builder.build())
    }

}