package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter.ListKonsultasiUserAdapter
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.MessageModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.UsersModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityKonsultasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user.MainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class KonsultasiActivity2 : Activity() {
    lateinit var binding: ActivityKonsultasiBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var usersArrayList: ArrayList<UsersModel>
    lateinit var konsultasiUserAdapter: ListKonsultasiUserAdapter
    lateinit var sharedPref: SharedPreferencesLogin
    lateinit var database : DatabaseReference
    lateinit var databaseChat : DatabaseReference
    val TAG = "KonsultasiActivity";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKonsultasiBinding.inflate(layoutInflater)
        val view = binding.root
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(view)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@KonsultasiActivity2)

        database = FirebaseService().firebase().child("users")
        databaseChat = FirebaseService().firebase().child("chats").child("message")
        usersArrayList = ArrayList()

        sharedPref = SharedPreferencesLogin(this@KonsultasiActivity2)
//        if(sharedPref.getSebagai()=="user"){
//            database = FirebaseDatabase.getInstance().getReference("dokter")
//        }
//        else if(sharedPref.getSebagai()=="dokter"){
//            database = FirebaseDatabase.getInstance().getReference("users")
//        }

        database.addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            var idSentTemp = ""
            var id = ""
            var nama = ""
            var umur = ""
            var username = ""
            var password = ""
            var sebagai = ""
            var token = ""
            var adaChat = ""

            override fun onDataChange(snapshot: DataSnapshot) {
                usersArrayList = ArrayList()
                for(value in snapshot.children){
                    id = value.child("id").value.toString()
                    nama = value.child("nama").value.toString()
                    umur = value.child("umur").value.toString()
                    username = value.child("username").value.toString()
                    password = value.child("password").value.toString()
                    sebagai = value.child("sebagai").value.toString()
                    token = value.child("token").value.toString()

                    if(sebagai.trim() != sharedPref.getSebagai() && sebagai.trim()!="admin"){
                        idSentTemp = id

                        databaseChat.addValueEventListener(object: ValueEventListener{
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(valuePertama in snapshot.children){

                                    var valueIdMessage: String? = ""
                                    var valueMessage: String? = ""
                                    var valueGambar: String? = ""
                                    var valueIdSent: String? = ""
                                    var valueIdReceived: String? = ""
                                    var valueTanggal: String? = ""
                                    var valueWaktu: String? = ""
                                    var valueKet: String? = ""

                                    for(valueKedua in valuePertama.children){
                                        val childIdMessage = valuePertama.child("idMessage").value.toString()
                                        val childMessage = valuePertama.child("message").value.toString()
                                        val childGambar = valuePertama.child("gambar").value.toString()
                                        val childIdSent = valuePertama.child("idSent").value.toString()
                                        val childIdReceived = valuePertama.child("idReceived").value.toString()
                                        val childTanggal = valuePertama.child("tanggal").value.toString()
                                        val childWaktu = valuePertama.child("waktu").value.toString()
                                        val childKet = valuePertama.child("ket").value.toString()

                                        valueIdMessage = childIdMessage
                                        valueMessage = childMessage
                                        valueGambar = childGambar
                                        valueIdSent = childIdSent
                                        valueIdReceived = childIdReceived
                                        valueTanggal = childTanggal
                                        valueWaktu = childWaktu
                                        valueKet = childKet

                                    }

                                    if(valueIdReceived == sharedPref.getId() && idSentTemp == valueIdSent){
                                        if(valueKet == "belum dibaca"){
                                            adaChat = "ada"
                                            Log.d(TAG, "data: ketDalam: $valueKet ")
                                        }
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@KonsultasiActivity2, "Gagal", Toast.LENGTH_SHORT).show()
                            }

                        })

                        Log.d(TAG, "data: ket: $adaChat ")

                        usersArrayList.add(UsersModel(id, nama, umur, username, password, sebagai, token, adaChat))
                    }
                }
                konsultasiUserAdapter = ListKonsultasiUserAdapter(this@KonsultasiActivity2, usersArrayList)
                binding.rvListKonsultasiUser.layoutManager = LinearLayoutManager(this@KonsultasiActivity2)
                binding.rvListKonsultasiUser.adapter = konsultasiUserAdapter
                konsultasiUserAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@KonsultasiActivity2, "Gagal Memuat Database", Toast.LENGTH_SHORT).show()
            }

        })
        Log.d(TAG, "onDataChange: ${usersArrayList.size}")

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@KonsultasiActivity2)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this@KonsultasiActivity2, MainActivity::class.java))
        finish()

    }
}