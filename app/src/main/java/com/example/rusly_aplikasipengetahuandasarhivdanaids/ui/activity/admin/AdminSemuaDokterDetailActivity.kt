package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter.AdminListSemuaUserAdapter
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.UsersModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityAdminSemuaDokterDetailBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityAdminSemuaUserDetailBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class AdminSemuaDokterDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminSemuaDokterDetailBinding
    lateinit var database: DatabaseReference
    private var idUser: String? = null
    lateinit var loading: LoadingAlertDialog
    lateinit var userArrayList : UsersModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSemuaDokterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = LoadingAlertDialog(this@AdminSemuaDokterDetailActivity)
        database = FirebaseService().firebase().child("users")

        setDataSebelumnya()
        setButton()
//        setData()
    }

    private fun setButton() {
        binding.apply {
            ivDrawerView.setOnClickListener {
                finish()
            }
            btnHapusData.setOnClickListener {
                dialogHapusdata()
            }
            btnUbahData.setOnClickListener {
                dialogUpdateData(
                    userArrayList.nama!!, userArrayList.umur!!.toInt(), userArrayList.email!!, userArrayList.username!!,
                    userArrayList.password!!, userArrayList.sebagai!!, userArrayList.token!!
                )
            }
        }
    }



    private fun dialogHapusdata() {
        val viewAlertDialog = View.inflate(this@AdminSemuaDokterDetailActivity, R.layout.alert_dialog_konfirmasi, null)

        val tvTitleKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvTitleKonfirmasi)
        val tvBodyKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvBodyKonfirmasi)
        val btnKonfirmasi = viewAlertDialog.findViewById<Button>(R.id.btnKonfirmasi)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        tvTitleKonfirmasi.text = "Hapus Akun?"
        tvBodyKonfirmasi.text = "Data yang terhapus tidak dapat dipulihkan"

        val alertDialog = AlertDialog.Builder(this@AdminSemuaDokterDetailActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnKonfirmasi.setOnClickListener {
            loading.alertDialogLoading()
            postDeleteData()
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun postDeleteData() {
        database.child(userArrayList.id!!).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.removeValue()
                Toast.makeText(this@AdminSemuaDokterDetailActivity, "Berhasil Hapus Data", Toast.LENGTH_SHORT).show()
                loading.alertDialogCancel()
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminSemuaDokterDetailActivity, "Berhasil Hapus Data", Toast.LENGTH_SHORT).show()
                loading.alertDialogCancel()
                finish()
            }
        })
    }

    private fun setDataSebelumnya() {
        val extras = intent.extras
        if(extras != null) {
            val list = intent.getParcelableExtra<UsersModel>("dataDokter")!!
            idUser = list.id

            fetchDokter(idUser!!)
        }
    }

    private fun fetchDokter(idUser: String){
        database.child(idUser).addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                val id = snapshot.child("id").value.toString()
                val nama = snapshot.child("nama").value.toString()
                val umur = snapshot.child("umur").value.toString()
                val email = snapshot.child("email").value.toString()
                val username = snapshot.child("username").value.toString()
                val password = snapshot.child("password").value.toString()
                val sebagai = snapshot.child("sebagai").value.toString()
                val token = snapshot.child("token").value.toString()
                val dibaca = snapshot.child("dibaca").value.toString()

                userArrayList = UsersModel(id, nama, umur, email, username, password, sebagai, token, dibaca)

                Log.d("FetchTAG", "id: $id")
                Log.d("FetchTAG", "nama: $nama")
                Log.d("FetchTAG", "umur: $umur")
                Log.d("FetchTAG", "email: $email")
                Log.d("FetchTAG", "username: $username")
                Log.d("FetchTAG", "password: $password")
                Log.d("FetchTAG", "sebagai: $sebagai")
                Log.d("FetchTAG", "token: $token")
                Log.d("FetchTAG", "dibaca: $dibaca")

//                binding.apply {
//                    etNama.setText(nama)
//                    etUmur.setText(umur)
//                    etUsername.setText(username)
//                    etPassword.setText(password)
//                }

                setData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminSemuaDokterDetailActivity, "Gagal Memuat Database", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setData() {
        binding.apply {
            etNama.setText(userArrayList.nama)
            etUmur.setText(userArrayList.umur.toString())
            etUsername.setText(userArrayList.username)
            etPassword.setText(userArrayList.password)
        }
    }

    fun dialogUpdateData(nama: String, umur: Int, email: String, username: String, password: String, sebagai: String, token: String){
        val viewAlertDialog = View.inflate(this@AdminSemuaDokterDetailActivity, R.layout.alert_dialog_update_akun, null)

        val etNama = viewAlertDialog.findViewById<TextView>(R.id.etNama)
        val etUmur = viewAlertDialog.findViewById<TextView>(R.id.etUmur)
        val etEmail = viewAlertDialog.findViewById<TextView>(R.id.etEmail)
        val etUsername = viewAlertDialog.findViewById<TextView>(R.id.etUsername)
        val etPassword = viewAlertDialog.findViewById<TextView>(R.id.etPassword)
        val btnShowPassword = viewAlertDialog.findViewById<ImageView>(R.id.btnShowPassword)
        var cekShow = false

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        etNama.text = nama
        etUmur.text = umur.toString()
        etEmail.text = email
        etUsername.text = username
        etPassword.text = password

        val alertDialog = AlertDialog.Builder(this@AdminSemuaDokterDetailActivity)
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
            postUpdateData(
                dialogInputan, idUser!!, etNama.text.toString(), etUmur.text.toString(),
                etEmail.text.toString(), etUsername.text.toString(), etPassword.text.toString(),
                sebagai, token, username
            )
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }


    fun postUpdateData(dialogInputan: AlertDialog, id:String, nama: String, umur: String, email: String, username: String, password: String, sebagai: String, token:String, usernameLama:String){
        database.addListenerForSingleValueEvent(object: ValueEventListener {
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
                        Toast.makeText(this@AdminSemuaDokterDetailActivity, "Username sudah dimiliki orang lain", Toast.LENGTH_LONG).show()
                        loading.alertDialogCancel()
                    }
                    else{
                        database.child(id).child("id").setValue(id)
                        database.child(id).child("nama").setValue(nama)
                        database.child(id).child("umur").setValue(umur)
                        database.child(id).child("email").setValue(email)
                        database.child(id).child("username").setValue(username)
                        database.child(id).child("password").setValue(password)
                        database.child(id).child("sebagai").setValue(sebagai)
                        database.child(id).child("token").setValue(token)

                        userArrayList = UsersModel(
                            id,
                            nama,
                            umur,
                            username,
                            password,
                            sebagai,
                            token
                        )

                        setData()

                        Toast.makeText(this@AdminSemuaDokterDetailActivity, "Berhasil Update data", Toast.LENGTH_SHORT).show()
                        dialogInputan.dismiss()
                        loading.alertDialogCancel()
                    }
                }
                else{
                    database.child(id).child("id").setValue(id)
                    database.child(id).child("nama").setValue(nama)
                    database.child(id).child("umur").setValue(umur)
                    database.child(id).child("email").setValue(email)
                    database.child(id).child("username").setValue(username)
                    database.child(id).child("password").setValue(password)
                    database.child(id).child("sebagai").setValue(sebagai)

                    userArrayList = UsersModel(
                        id,
                        nama,
                        umur,
                        email,
                        username,
                        password,
                        sebagai,
                        token
                    )
                    setData()

                    loading.alertDialogCancel()
                    Toast.makeText(this@AdminSemuaDokterDetailActivity, "Berhasil Update data", Toast.LENGTH_SHORT).show()
                    dialogInputan.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminSemuaDokterDetailActivity, "Gagal Upate Data", Toast.LENGTH_SHORT).show()
                loading.alertDialogCancel()
            }

        })
    }
}