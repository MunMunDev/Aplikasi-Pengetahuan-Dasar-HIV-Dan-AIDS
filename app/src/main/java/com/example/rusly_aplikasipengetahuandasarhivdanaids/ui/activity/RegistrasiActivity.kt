package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityRegistrasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.text.SimpleDateFormat
import java.util.Calendar


class RegistrasiActivity : Activity() {
    lateinit var binding: ActivityRegistrasiBinding
    lateinit var loading: LoadingAlertDialog
    lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        val view = binding.root
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(view)

        database = FirebaseService().firebase().child("users")

        loading = LoadingAlertDialog(this@RegistrasiActivity)
        binding.apply {
            btnRegistrasi.setOnClickListener {
                loading.alertDialogLoading()
                if(etUmur.text.isNotEmpty() && etEmail.text.isNotEmpty() && etUsername.text.isNotEmpty() && etPassword.text.isNotEmpty()){
//                    postRegistrasi(etUmur.text.toString().trim().toInt(), etUsername.text.toString(), etPassword.text.toString())
                    if(Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
                        val str = "1234567890"
                        var nama = "user"
                        val strId = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
                        var id = "user"
                        for(i in 0..7){
                            nama+=str.random()
                            id+=strId.random()
                        }
                        id+="-${tanggalSekarang()}${waktuSekarang()}"
                        val email = etEmail.text.toString()
                        val username = etUsername.text.toString()
                        val umur = etUmur.text.toString()
                        val password = etPassword.text.toString()
                        val sebagai = "user"
                        var token = ""
                        FirebaseMessaging.getInstance().token.apply {
                            addOnSuccessListener { p0 ->
                                token = p0
                                Log.d("messagingToken", "onSuccess: $p0")

                            }
                            addOnCanceledListener {
                                Toast.makeText(this@RegistrasiActivity, "Gagal Mendapatkan Token", Toast.LENGTH_SHORT).show()
                            }
                        }

//                    database.addListenerForSingleValueEvent(object: ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            if(snapshot.child(id).exists()){
//                                Toast.makeText(this@RegistrasiActivity, "Username Sudah Ada", Toast.LENGTH_SHORT).show()
//                            }
//                            else{
//                                loading.alertDialogCancel()
//                                database.child(id).child("id").setValue(id)
//                                database.child(id).child("nama").setValue(nama)
//                                database.child(id).child("umur").setValue(umur)
//                                database.child(id).child("username").setValue(username)
//                                database.child(id).child("password").setValue(password)
//                                database.child(id).child("sebagai").setValue(sebagai)
//                                Toast.makeText(this@RegistrasiActivity, "Berhasil Membuat Akun", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            Toast.makeText(this@RegistrasiActivity, "Database Error "+error.message, Toast.LENGTH_SHORT).show()
//                        }
//                    })

                        database.addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var cekUsernameTerdaftar = false
                                for(value in snapshot.children){
                                    val valueUsername = value.child("username").value.toString()

                                    if(valueUsername.toLowerCase() == username.toLowerCase()){
                                        cekUsernameTerdaftar=true
                                    }
                                }

                                if(cekUsernameTerdaftar){
                                    Toast.makeText(this@RegistrasiActivity, "Gagal Membuat Akun \nUsername sudah dimiliki orang lain", Toast.LENGTH_LONG).show()
                                    loading.alertDialogCancel()
                                }
                                else{
                                    database.child(id).child("id").setValue(id)
                                    database.child(id).child("nama").setValue(nama)
                                    database.child(id).child("umur").setValue(umur)
                                    database.child(id).child("username").setValue(username)
                                    database.child(id).child("email").setValue(email)
                                    database.child(id).child("password").setValue(password)
                                    database.child(id).child("sebagai").setValue(sebagai)
                                    database.child(id).child("token").setValue(token)

                                    Toast.makeText(this@RegistrasiActivity, "Berhasil Membuat Akun", Toast.LENGTH_SHORT).show()
                                    loading.alertDialogCancel()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@RegistrasiActivity, "Gagal Upate Data", Toast.LENGTH_SHORT).show()
                                loading.alertDialogCancel()
                            }

                        })
                    } else{
                        Toast.makeText(this@RegistrasiActivity, "Masukkan email dengan benar", Toast.LENGTH_SHORT).show()
                        loading.alertDialogCancel()
                    }
                }
                else if(etUmur.text.isEmpty()){
                    etUmur.error = "Tidak Boleh Kosong"
                    loading.alertDialogCancel()
                }
                else if(etEmail.text.isEmpty()){
                    etEmail.error = "Tidak Boleh Kosong"
                    loading.alertDialogCancel()
                }
                else if(etUsername.text.isEmpty()){
                    etUsername.error = "Tidak Boleh Kosong"
                    loading.alertDialogCancel()
                }
                else if(etPassword.text.isEmpty()){
                    etPassword.error = "Tidak Boleh Kosong"
                    loading.alertDialogCancel()
                }
            }

            tvLogin.setOnClickListener {
//                startActivity(Intent(this@RegistrasiActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    fun tanggalSekarang():String{
        val calendar: Calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd")
        val dateTime = simpleDateFormat.format(calendar.time)

        return dateTime
    }

    fun waktuSekarang():String{
        val calendar: Calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("HHmmss")
        val time = simpleDateFormat.format(calendar.time)

        return time
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        startActivity(Intent(this@RegistrasiActivity, LoginActivity::class.java))
        finish()
    }
}