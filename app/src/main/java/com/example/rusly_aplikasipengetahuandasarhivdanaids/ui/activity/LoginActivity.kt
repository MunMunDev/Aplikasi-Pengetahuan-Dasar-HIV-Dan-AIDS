package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.UsersModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityLoginBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin.AdminMainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user.MainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : Activity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var loading: LoadingAlertDialog
    lateinit var sharedPref : SharedPreferencesLogin
    lateinit var database : DatabaseReference
    var usersArrayList: ArrayList<UsersModel> = arrayListOf()
    val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(view)

        sharedPref = SharedPreferencesLogin(this@LoginActivity)
        loading = LoadingAlertDialog(this@LoginActivity)

        binding.apply {
            tvLupaPassword.setOnClickListener {
                setShowLupaPassword()
            }
            btnLogin.setOnClickListener {
                if(etUsername.text.isNotEmpty() && etPassword.text.isNotEmpty()){
                    loading.alertDialogLoading()
                    login(etUsername.text.toString().trim(), etUsername.text.toString().trim(), etPassword.text.toString().trim())
                }
                else if(etUsername.text.isEmpty()){
                    etUsername.error = "Tidak Boleh Kosong !"
                }
                else if(etPassword.text.isEmpty()){
                    etPassword.error = "Tidak Boleh Kosong !"
                }
            }

            tvDaftar.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegistrasiActivity::class.java))
//                finish()
            }

            tvLupaPassword.setOnClickListener {
                startActivity(Intent(this@LoginActivity, LupaPasswordActivity::class.java))
            }
        }
    }

    private fun setShowLupaPassword() {

    }

    fun login(valueEmail: String, valueUsername: String, valuePassword: String){
        database = FirebaseService().firebase().child("users")

        var id: String?
        var nama: String?
        var umur: String?
        var email: String?
        var username: String?
        var password: String?
        var sebagai: String?
        var token: String?

        database.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(value in snapshot.children){
                    id = value.child("id").value.toString()
                    nama = value.child("nama").value.toString()
                    umur = value.child("umur").value.toString()
                    email = value.child("email").value.toString()
                    username = value.child("username").value.toString()
                    password = value.child("password").value.toString()
                    sebagai = value.child("sebagai").value.toString()
                    token = value.child("token").value.toString()

                    if(username!!.trim() == valueUsername.trim() && password!!.trim() == valuePassword.trim()){
                        usersArrayList.add(UsersModel(id, nama, umur, email, username, password, sebagai, token))
                    } else if(email!!.trim() == valueEmail.trim() && password!!.trim() == valuePassword.trim()){
                        usersArrayList.add(UsersModel(id, nama, umur, email, username, password, sebagai, token))
                    }
                }
                if(usersArrayList.size>0){
                    val user = usersArrayList[0]
//                    LoginBerhasil(UsersModel(user.id, user.nama, user.umur, user.username, user.password, user.sebagai, user.token))
                    LoginBerhasil(user)
                } else{
                    Toast.makeText(this@LoginActivity, "Username dan Password tidak ditemukan", Toast.LENGTH_LONG).show()
                    loading.alertDialogCancel()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database Error "+error.message, Toast.LENGTH_SHORT).show()
                loading.alertDialogCancel()
            }
        })

//        if(usersArrayList.size>0){
//            val user = usersArrayList[0]
////            LoginBerhasil(UsersModel(user.id, user.nama, user.umur, user.username, user.password, user.sebagai, user.token))
//            LoginBerhasil(user)
//        } else{
//            Toast.makeText(this@LoginActivity, "Username dan Password tidak ditemukan", Toast.LENGTH_LONG).show()
//            loading.alertDialogCancel()
//        }
    }

    private fun LoginBerhasil(user: UsersModel){
        FirebaseMessaging.getInstance().token.apply {
            addOnSuccessListener { p0 ->

                sharedPref.setLogin(
                    user.id!!,
                    user.nama!!,
                    user.umur!!.toInt(),
                    user.email!!,
                    user.username!!,
                    user.password!!,
                    user.sebagai!!,
                    p0
                )

                database.child(user.id!!).child("id").setValue(user.id)
                database.child(user.id!!).child("nama").setValue(user.nama)
                database.child(user.id!!).child("umur").setValue(user.umur)
                database.child(user.id!!).child("email").setValue(user.email)
                database.child(user.id!!).child("username").setValue(user.username)
                database.child(user.id!!).child("password").setValue(user.password)
                database.child(user.id!!).child("sebagai").setValue(user.sebagai)
                database.child(user.id!!).child("token").setValue(p0)

                if(user.sebagai == "admin"){
                    Toast.makeText(this@LoginActivity, "Berhasil Login", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@LoginActivity, AdminMainActivity::class.java))
                    loading.alertDialogCancel()
                    finish()
                } else{
                    Toast.makeText(this@LoginActivity, "Berhasil Login", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    loading.alertDialogCancel()
                    finish()
                }
            }
            addOnCanceledListener {
                Toast.makeText(this@LoginActivity, "Gagal Mendapatkan Token", Toast.LENGTH_SHORT).show()
            }
        }
    }


    var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@LoginActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)

    }
}