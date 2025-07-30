package com.example.rusly_aplikasipengetahuandasarhivdanaids.utils

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class TanggalDanWaktu {
    fun hurufAcak(idSent:String): String{

        val dateTime = "${tanggalSekarangZonaMakassar()}-${waktuSekarangZonaMakassar()}-$idSent"

        return dateTime
    }

    @SuppressLint("SimpleDateFormat")
    fun tanggalSekarangZonaMakassar():String{
        var date = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val makassarZone  = ZoneId.of("Asia/Makassar")
            val makassarTanggal = LocalDate.now(makassarZone)
            val tanggal = makassarTanggal
            date = "$tanggal"
        } else {
            val makassarTimeZone = TimeZone.getTimeZone("Asia/Makassar")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateFormat.timeZone = makassarTimeZone
            val currentDate = Date()
            val makassarDate = dateFormat.format(currentDate)
            date = makassarDate
        }

        return date
    }

    @SuppressLint("SimpleDateFormat")
    fun waktuSekarangZonaMakassar():String{
        var time = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val makassarZone  = ZoneId.of("Asia/Makassar")
            val makassarTime = LocalTime.now(makassarZone)
            val waktu = makassarTime.toString().split(".")
            time = waktu[0]

        } else {
            val makassarTimeZone = TimeZone.getTimeZone("Asia/Makassar")
            val timeFormat = SimpleDateFormat("HH:mm:ss")
            timeFormat.timeZone = makassarTimeZone
            val currentTime = Date()
            val makassarTime = timeFormat.format(currentTime)
            time = makassarTime
        }
        return time
    }

    fun konversiBulanSingkatan(bulan: String): String{
        val arrayBulan = arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "Mei",
            "Juni",
            "Juli",
            "Agust",
            "Sep",
            "Okt",
            "Nov",
            "Des"
        )

        val splitBulan = bulan.split("-")
        val valueBulan = "${splitBulan[2]} ${arrayBulan[(splitBulan[1].toInt()-1)]} ${splitBulan[0]}"

        return valueBulan
    }

    fun waktuNoSecond(waktu: String): String{
        val arrayWaktu = waktu.split(":")
        return "${arrayWaktu[0]}:${arrayWaktu[1]}"
    }
}