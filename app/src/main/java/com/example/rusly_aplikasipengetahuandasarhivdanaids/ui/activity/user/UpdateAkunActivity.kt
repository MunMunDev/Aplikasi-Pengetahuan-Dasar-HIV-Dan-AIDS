package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityUpdateAkunBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class UpdateAkunActivity : Activity() {
    lateinit var binding: ActivityUpdateAkunBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var sharedPref : SharedPreferencesLogin
    lateinit var database : DatabaseReference
    lateinit var loading: LoadingAlertDialog
    val TAG = "UpdateAkunActivityCek"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAkunBinding.inflate(layoutInflater)
        val mainView = binding.root
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(mainView)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@UpdateAkunActivity)
        sharedPref = SharedPreferencesLogin(this@UpdateAkunActivity)
        database = FirebaseService().firebase().child("users")
        loading = LoadingAlertDialog(this@UpdateAkunActivity)

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@UpdateAkunActivity)

            var id = sharedPref.getId().trim()
            var nama = ""
            var umur = ""
            var username = ""
            var password = ""
            var sebagai = sharedPref.getSebagai()
            var token = sharedPref.getToken()

            database.child(id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    nama = snapshot.child("nama").value.toString()
                    umur = snapshot.child("umur").value.toString()
                    username = snapshot.child("username").value.toString()
                    password = snapshot.child("password").value.toString()

                    etNama.setText(nama)
                    etUmur.setText(umur)
                    etUsername.setText(username)
                    etPassword.setText(password)
                    Log.d(TAG, "onDataChange: nama: ${nama}, umur: $umur, username: $username, password: $password")
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@UpdateAkunActivity, "Gagal Mengembil data", Toast.LENGTH_SHORT).show()
                }
            })

            btnUbahData.setOnClickListener {
                dialogUpdateData(nama, umur.toInt(), username, password, sebagai, token)
            }
        }
    }

    fun dialogUpdateData(nama: String, umur: Int, username: String, password: String, sebagai: String, token: String){
        val viewAlertDialog = View.inflate(this@UpdateAkunActivity, R.layout.alert_dialog_update_akun, null)

        val etNama = viewAlertDialog.findViewById<TextView>(R.id.etNama)
        val etUmur = viewAlertDialog.findViewById<TextView>(R.id.etUmur)
        val etUsername = viewAlertDialog.findViewById<TextView>(R.id.etUsername)
        val etPassword = viewAlertDialog.findViewById<TextView>(R.id.etPassword)
        val btnShowPassword = viewAlertDialog.findViewById<ImageView>(R.id.btnShowPassword)
        var cekShow = false

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        etNama.text = nama
        etUmur.text = umur.toString()
        etUsername.text = username
        etPassword.text = password

        val alertDialog = AlertDialog.Builder(this@UpdateAkunActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnShowPassword.setOnClickListener {
            if (cekShow){
                cekShow = false
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnShowPassword.setImageResource(R.drawable.no_eye)
            } else{
                cekShow = true
                etPassword.inputType = InputType.TYPE_CLASS_TEXT
                btnShowPassword.setImageResource(R.drawable.eye)
            }
        }

        btnSimpan.setOnClickListener {
            loading.alertDialogLoading()
            postUpdateData(dialogInputan, sharedPref.getId(), etNama.text.toString(), etUmur.text.toString().toInt(), etUsername.text.toString(), etPassword.text.toString(), sebagai, token, username)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }


    fun postUpdateData(dialogInputan:AlertDialog, id:String, nama: String, umur: Int, username: String, password: String, sebagai: String, token:String, usernameLama:String){
        val map: HashMap<String, Any> = HashMap()
        map.put("id", id)
        map.put("nama", nama)
        map.put("umur", umur)
        map.put("username", username)
        map.put("password", password)
        map.put("sebagai", sebagai)

//        database.child(id).setValue(map).addOnSuccessListener {
//            Toast.makeText(this@UpdateAkunActivity, "Berhasil", Toast.LENGTH_SHORT).show()
//            dialogInputan.dismiss()
//        }
        database.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var cekUsernameTerdaftar = false
                for(value in snapshot.children){
                    val valueUsername = value.child("username").value.toString()

                    if(valueUsername == username){
                        cekUsernameTerdaftar=true
                    }
                }

                if(cekUsernameTerdaftar){
                    if(usernameLama != username){
                        Toast.makeText(this@UpdateAkunActivity, "Username sudah dimiliki orang lain", Toast.LENGTH_LONG).show()
                        loading.alertDialogCancel()
                    }
                    else{
                        database.child(id).child("id").setValue(id)
                        database.child(id).child("nama").setValue(nama)
                        database.child(id).child("umur").setValue(umur)
                        database.child(id).child("username").setValue(username)
                        database.child(id).child("password").setValue(password)
                        database.child(id).child("sebagai").setValue(sebagai)
                        database.child(id).child("token").setValue(token)

                        sharedPref.setLogin(id, nama, umur, username, password, sebagai, token)
                        Toast.makeText(this@UpdateAkunActivity, "Berhasil Update data", Toast.LENGTH_SHORT).show()
                        dialogInputan.dismiss()
                        loading.alertDialogCancel()
                    }
                }
                else{
                    database.child(id).child("id").setValue(id)
                    database.child(id).child("nama").setValue(nama)
                    database.child(id).child("umur").setValue(umur)
                    database.child(id).child("username").setValue(username)
                    database.child(id).child("password").setValue(password)
                    database.child(id).child("sebagai").setValue(sebagai)

                    loading.alertDialogCancel()
                    Toast.makeText(this@UpdateAkunActivity, "Berhasil Update data", Toast.LENGTH_SHORT).show()
                    dialogInputan.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UpdateAkunActivity, "Gagal Upate Data", Toast.LENGTH_SHORT).show()
                loading.alertDialogCancel()
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this@UpdateAkunActivity, MainActivity::class.java))
        finish()

    }
}