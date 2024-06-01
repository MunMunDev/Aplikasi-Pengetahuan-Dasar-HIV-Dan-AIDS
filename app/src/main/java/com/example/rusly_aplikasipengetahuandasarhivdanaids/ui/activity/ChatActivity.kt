package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter.MessageKonsultasiAdapter
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.retrofit.ApiService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.MessageModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.NotificationModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.PushNotificationModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityChatBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogKeteranganBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class ChatActivity : Activity() {
    lateinit var binding : ActivityChatBinding
    lateinit var sharedPref: SharedPreferencesLogin
    lateinit var database: DatabaseReference
    lateinit var messageAdapter: MessageKonsultasiAdapter
    lateinit var messageArrayList : ArrayList<MessageModel>
    var idSent: String? = null
    var idReceived: String? = null
    var senderRoom: String? = null
    var receivedRoom: String? = null
    var nama: String? = null
    var token: String? = null
    val TAG = "ChatActivity";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(view)

        val bundle = intent.extras
        sharedPref = SharedPreferencesLogin(this@ChatActivity)
        database = FirebaseService().firebase().child("information")

        idSent = sharedPref.getId()
        idReceived = bundle!!.getString("id").toString()
        nama = bundle!!.getString("nama").toString()
        token = bundle!!.getString("token").toString()

        hurufAcak()

        val waktuZonaMakassar = waktuSekarangZonaMakassar().split(":")
        val jam = waktuZonaMakassar[0].trim().toInt()
        if(jam.toLong() >= 8.toLong() && jam.toLong() <= 16.toLong()){
            binding.apply {
                llMessage.visibility = View.VISIBLE
                llDontSendMessage.visibility = View.GONE

                etMessage.requestFocus()
            }
        } else{
            binding.apply {
                llMessage.visibility = View.GONE
                llDontSendMessage.visibility = View.VISIBLE
            }
        }

        binding.apply {
            btnInfoDontSendMessage.setOnClickListener{
                llMessage.visibility = View.VISIBLE
                llDontSendMessage.visibility = View.GONE

                etMessage.requestFocus()
            }
        }

//        binding.ivInfoSaranJamChat.setOnClickListener {
//            setShowJudul("08:00 - 16.00 WITA")
//        }

//        binding.etMessage.requestFocus()

        database.addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                messageArrayList = ArrayList()
                for(value in snapshot.children){
                    var valueIdMessage: String? = ""
                    var valueMessage: String? = ""
                    var valueIdSent: String? = ""
                    var valueIdReceived: String? = ""
                    var valueTanggal: String? = ""
                    var valueWaktu: String? = ""
                    var valueKet: String? = ""

                    for(valueKedua in value.children){
                        val childIdMessage = value.child("idMessage").value.toString()
                        val childMessage = value.child("message").value.toString()
                        val childIdSent = value.child("idSent").value.toString()
                        val childIdReceived = value.child("idReceived").value.toString()
                        val childTanggal = value.child("tanggal").value.toString()
                        val childWaktu = value.child("waktu").value.toString()

                        if(childIdSent == idSent && childIdReceived == idReceived){
                            valueIdMessage = childIdMessage
                            valueMessage = childMessage
                            valueIdSent = childIdSent
                            valueIdReceived = childIdReceived
                            valueTanggal = childTanggal
                            valueWaktu = childWaktu
                            valueKet = childWaktu
                        }
                        else if(childIdSent == idReceived && childIdReceived == idSent){
                            valueIdMessage = childIdMessage
                            valueMessage = childMessage
                            valueIdSent = childIdSent
                            valueIdReceived = childIdReceived
                            valueTanggal = childTanggal
                            valueWaktu = childWaktu
                            valueKet = childWaktu
                        }

//                        val currentChat = value.getValue(MessageModel::class.java)
//
//                        if(currentChat!!.idSent == idSent && currentChat.idReceived==idReceive){
//                            valueMessage = currentChat.message
//                            valueIdSent = currentChat.idSent
//                            valueIdReceived = currentChat.idReceived
//
////                            Log.d(TAG, "onDataChange: Berhasil: ${currentChat!!.idSent} dan ${currentChat!!.idReceived}")
////                            messageArrayList.add(currentChat!!)
//                        }
//                        else if(currentChat!!.idSent == idReceive && currentChat.idReceived==idSent){
//                            valueMessage = currentChat.message
//                            valueIdSent = currentChat.idSent
//                            valueIdReceived = currentChat.idReceived
//
//                            Log.d(TAG, "onDataChange: Message: ${valueMessage}")
//                            Log.d(TAG, "onDataChange: Username Send: ${valueIdSent}")
//                            Log.d(TAG, "onDataChange: Username Received: ${valueIdReceived}")
//                        }
                    }
                    if(valueMessage!!.isNotEmpty() && valueIdSent!!.isNotEmpty() && valueIdReceived!!.isNotEmpty()){
                        messageArrayList.add(
                            MessageModel(
                                valueIdMessage.toString(),
                                valueMessage.toString(),
                                valueIdSent.toString(),
                                valueIdReceived.toString(),
                                valueTanggal.toString(),
                                valueWaktu.toString(),
                                valueKet.toString()
                            )
                        )
                    }
//                    messageArrayList.add(MessageModel(valueMessage!!, valueIdSent!!, valueIdReceived!!))
//                        messageArrayList[0] = MessageModel(valueMessage!!, valueIdSent!!, valueIdReceived!!)
                    Log.d(TAG, "onDataChange 1: ${messageArrayList.size}")
                    Log.d(TAG, "onDataChange 2: ${messageArrayList.size}")

//                    val currentChat = value.getValue(MessageModel::class.java)
//                    if(currentChat!!.idSent == sharedPref.getUsername() && currentChat!!.idSent == bundle!!.getString("id")){
//                        messageArrayList.add(currentChat!!)
//                        Log.d(TAG, "onData: ${currentChat.message} : ${currentChat.idSent} : ${currentChat.idReceived}")
//                    }
//                    Log.d(TAG, "onDataChange: ${messageArrayList.size} : ${messageArrayList}")
                }
                Log.d(TAG, "onDataChange: size: ${messageArrayList.size}")

                val sorted = messageArrayList.sortedWith(compareBy { it.idMessage })
                Log.d(TAG, "sorted: ")
                for(i in sorted){
                    Log.d(TAG, "sorted data: ${i.idMessage}, ${i.idReceived}, ${i.idSent}, ${i.message} ")
                }

//                messageAdapter = MessageKonsultasiAdapter(this@ChatActivity, messageArrayList, sharedPref.getId())
                messageAdapter = MessageKonsultasiAdapter(this@ChatActivity, sorted, sharedPref.getId())

                binding.rvListKonsultasiChatDokter.layoutManager = LinearLayoutManager(this@ChatActivity)
                binding.rvListKonsultasiChatDokter.adapter = messageAdapter
                binding.rvListKonsultasiChatDokter.scrollToPosition(messageArrayList.size-1)
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Gagal", Toast.LENGTH_SHORT).show()
            }

        })
        binding.apply {
            ivBack.setOnClickListener{
                finish()
            }
            tvNamaDokter.text = nama

            btnSendMessage.setOnClickListener {
                if(etMessage.text.trim().isNotEmpty()){
                    // Kirim data
//                    Toast.makeText(this@ChatActivity, "Terkirim", Toast.LENGTH_SHORT).show()
                    database.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            database.child("$senderRoom").child("idMessage").setValue(senderRoom)
                            database.child("$senderRoom").child("message").setValue(etMessage.text.toString().trim())
                            database.child("$senderRoom").child("idSent").setValue(idSent)
                            database.child("$senderRoom").child("idReceived").setValue(idReceived)
//                            database.child("$senderRoom").child("tanggal").setValue(tanggalSekarang())
                            database.child("$senderRoom").child("tanggal").setValue(tanggalSekarangZonaMakassar())
//                            database.child("$senderRoom").child("waktu").setValue(waktuSekarang())
                            database.child("$senderRoom").child("waktu").setValue(waktuSekarangZonaMakassar())
                            database.child("$senderRoom").child("ket").setValue("belum dibaca")

                            postMessage(sharedPref.getNama(), etMessage.text.toString(), token.toString())

                            etMessage.text = null
                            hurufAcak()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@ChatActivity, "Gagal", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }
        }
    }

    private fun setShowJudul(jam: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@ChatActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Jam Operasional"
            tvBodyKeterangan.text = "Saran chat Dokter pada jam $jam"
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    fun hurufAcak(){

//        val dateTime = "${tanggalSekarang()}-${waktuSekarang()}"
        val dateTime = "${tanggalSekarangZonaMakassar()}-${waktuSekarangZonaMakassar()}"

//        var str = "abcdefghijklmnopqrstuvwxyz1234567890"
//        var hurufAcak = "1"
//        for(i in 1..10){
//            hurufAcak+=str.random()
//        }
//        this.senderRoom = "$idSent-$idReceive-$hurufAcak"
//        this.receivedRoom = "$idReceive-$idSent-$hurufAcak"

        this.senderRoom = "${dateTime}--$idSent--$idReceived"
        this.receivedRoom = "${dateTime}--$idReceived--$idSent"
    }

    fun tanggalSekarang():String{
//        val calendar: Calendar = Calendar.getInstance()
//        calendar.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
//        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
//        simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
//        val dateTime = simpleDateFormat.format(calendar.time)

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val dateTime = simpleDateFormat.format(calendar.time)

        return dateTime
    }

    fun waktuSekarang():String{
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val time = simpleDateFormat.format(calendar.time)

        return time
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

    fun idMessage(): String{
        var str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        var hurufAcak = ""
        for(i in 1..20){
            hurufAcak+=str.random()
        }

        return hurufAcak
    }

    fun postMessage(valueNama:String, valueMessage:String, token: String){
        ApiService.getRetrofit().postChat(PushNotificationModel(NotificationModel(valueNama,valueMessage), token))
            .enqueue(object: Callback<PushNotificationModel> {
                override fun onResponse(
                    call: Call<PushNotificationModel>,
                    response: Response<PushNotificationModel>
                ) {
                    Toast.makeText(this@ChatActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(
                    call: Call<PushNotificationModel>,
                    t: Throwable
                ) {
                    Toast.makeText(this@ChatActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }
}