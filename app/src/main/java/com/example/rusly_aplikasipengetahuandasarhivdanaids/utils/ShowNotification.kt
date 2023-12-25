package com.example.rusly_aplikasipengetahuandasarhivdanaids.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import java.util.Random

class ShowNotification {
    fun shotNotification(context: Context, title: String, body:String){
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "Pengetahuan Dasar HIV AIDS")

        builder.apply {
            setSmallIcon(R.drawable.logo_nurse)
            setContentTitle(title)
            setContentText(body)
            setPriority(NotificationCompat.PRIORITY_MAX)
        }

        // Style
        val bigTextStyle: NotificationCompat.BigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.bigText(title)
        bigTextStyle.setBigContentTitle(title)
        bigTextStyle.setSummaryText(title)

        builder.setStyle(bigTextStyle)

        val manager :NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channelId = "Pengetahuan Dasar HIV AIDS 2"
            var channel =  NotificationChannel(channelId, "Pengetahuan Dasar HIV AIDS 3", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }

        manager.notify(Random().nextInt(), builder.build())
    }
}