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

class KonsultasiActivity : Activity() {
    lateinit var binding: ActivityKonsultasiBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var usersArrayList: ArrayList<UsersModel>
    lateinit var konsultasiUserAdapter: ListKonsultasiUserAdapter
    lateinit var sharedPref: SharedPreferencesLogin
    lateinit var database : DatabaseReference
    val TAG = "KonsultasiActivity";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKonsultasiBinding.inflate(layoutInflater)
        val view = binding.root
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(view)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@KonsultasiActivity)

        database = FirebaseService().firebase().child("users")
        usersArrayList = ArrayList()

        sharedPref = SharedPreferencesLogin(this@KonsultasiActivity)
//        if(sharedPref.getSebagai()=="user"){
//            database = FirebaseDatabase.getInstance().getReference("dokter")
//        }
//        else if(sharedPref.getSebagai()=="dokter"){
//            database = FirebaseDatabase.getInstance().getReference("users")
//        }

        database.addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                usersArrayList = ArrayList()
                for(value in snapshot.children){
                    val id = value.child("id").value.toString()
                    val nama = value.child("nama").value.toString()
                    val umur = value.child("umur").value.toString()
                    val username = value.child("username").value.toString()
                    val password = value.child("password").value.toString()
                    val sebagai = value.child("sebagai").value.toString()
                    val token = value.child("token").value.toString()

                    if(sebagai.trim() != sharedPref.getSebagai() && sebagai.trim()!="admin"){
                        usersArrayList.add(UsersModel(id, nama, umur.toInt(), username, password, sebagai, token))
                    }
                }
                konsultasiUserAdapter = ListKonsultasiUserAdapter(this@KonsultasiActivity, usersArrayList)
                binding.rvListKonsultasiUser.layoutManager = LinearLayoutManager(this@KonsultasiActivity)
                binding.rvListKonsultasiUser.adapter = konsultasiUserAdapter
                konsultasiUserAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@KonsultasiActivity, "Gagal Memuat Database", Toast.LENGTH_SHORT).show()
            }

        })
        Log.d(TAG, "onDataChange: ${usersArrayList.size}")

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@KonsultasiActivity)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this@KonsultasiActivity, MainActivity::class.java))
        finish()

    }
}