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
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.UsersModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityKonsultasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user.MainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay

class KonsultasiActivity : Activity() {
    lateinit var binding: ActivityKonsultasiBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    var usersArrayList: ArrayList<UsersModel> = arrayListOf()
    var arrayCariChat: ArrayList<String> = arrayListOf()
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

        setSharedPref()
        setKontrolNavigationDrawer()
        fetchUser()
    }

    private fun setSharedPref() {
        sharedPref = SharedPreferencesLogin(this@KonsultasiActivity)
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@KonsultasiActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@KonsultasiActivity)
        }
    }

    private fun fetchUser(){
        database = FirebaseService().firebase().child("users")

        database.addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")

            var idSentTemp = ""
            var id = ""
            var nama = ""
            var umur = ""
            var email = ""
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
                    email = value.child("email").value.toString()
                    username = value.child("username").value.toString()
                    password = value.child("password").value.toString()
                    sebagai = value.child("sebagai").value.toString()
                    token = value.child("token").value.toString()

                    if(sebagai.trim() != sharedPref.getSebagai() && sebagai.trim()!="admin"){
                        usersArrayList.add(UsersModel(id, nama, umur, email, username, password, sebagai, token, adaChat))
                    }
                }

                for(values in usersArrayList){
                    Log.d("KonsultasiTAG", "onDataChange: id: $id")
                    Log.d("KonsultasiTAG", "onDataChange: nama: $nama")
                    Log.d("KonsultasiTAG", "onDataChange: umur: $umur")
                    Log.d("KonsultasiTAG", "onDataChange: email: $email")
                    Log.d("KonsultasiTAG", "onDataChange: username: $username")
                    Log.d("KonsultasiTAG", "onDataChange: password: $password")
                    Log.d("KonsultasiTAG", "onDataChange: token: $token")
                    Log.d("KonsultasiTAG", "onDataChange: adaChat: $adaChat")
                }

                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@KonsultasiActivity, "Gagal Memuat Database", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun fetchMessageUser(){
        databaseChat = FirebaseService().firebase().child("chats").child("message")
        databaseChat.addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                for(valuePertama in snapshot.children){

                    var valueIdSent: String? = ""
                    var valueIdReceived: String? = ""
                    var valueKet: String? = ""

                    for(valueKedua in valuePertama.children){
                        val childIdSent = valuePertama.child("idSent").value.toString()
                        val childIdReceived = valuePertama.child("idReceived").value.toString()
                        val childKet = valuePertama.child("ket").value.toString()

                        valueIdSent = childIdSent
                        valueIdReceived = childIdReceived
                        valueKet = childKet

                    }

//                    Log.d(TAG, "data: aaaa")

                    if(valueIdReceived == sharedPref.getId()){

                        if(valueKet == "belum dibaca"){
                            arrayCariChat.add(valueIdSent!!)
                        }
                    }
                }

                val sortData = arrayCariChat.toSet()
                for (value in sortData){
                    Log.d(TAG, "data: $value")
                }
                konsultasiUserAdapter.updateData(sortData)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@KonsultasiActivity, "Gagal", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapter(){
        konsultasiUserAdapter = ListKonsultasiUserAdapter(this@KonsultasiActivity, usersArrayList)
        binding.rvListKonsultasiUser.layoutManager = LinearLayoutManager(this@KonsultasiActivity)
        binding.rvListKonsultasiUser.adapter = konsultasiUserAdapter

        fetchMessageUser()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this@KonsultasiActivity, MainActivity::class.java))
        finish()

    }
}