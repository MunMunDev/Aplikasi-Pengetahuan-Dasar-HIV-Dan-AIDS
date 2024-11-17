package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
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
    lateinit var list: UsersModel
    private var idUser: String? = null
    lateinit var loading: LoadingAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSemuaDokterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = LoadingAlertDialog(this@AdminSemuaDokterDetailActivity)
        database = FirebaseService().firebase().child("users")

        setDataSebelumnya()
        setButton()
        setData()
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
                dialogUpdateData(list.nama!!, list.umur!!.toInt(), list.username!!, list.password!!, list.sebagai!!, list.token!!)
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
        database.child(list.id!!).addListenerForSingleValueEvent(object: ValueEventListener {
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
            list = intent.getParcelableExtra<UsersModel>("dataDokter")!!
            idUser = list.id
        }
    }


    private fun setData() {
        binding.apply {
            etNama.setText(list.nama)
            etUmur.setText(list.umur.toString())
            etUsername.setText(list.username)
            etPassword.setText(list.password)
        }
    }

    fun dialogUpdateData(nama: String, umur: Int, username: String, password: String, sebagai: String, token: String){
        val viewAlertDialog = View.inflate(this@AdminSemuaDokterDetailActivity, R.layout.alert_dialog_update_akun, null)

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
            postUpdateData(dialogInputan, idUser!!, etNama.text.toString(), etUmur.text.toString(), etUsername.text.toString(), etPassword.text.toString(), sebagai, token, username)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }


    fun postUpdateData(dialogInputan: AlertDialog, id:String, nama: String, umur: String, username: String, password: String, sebagai: String, token:String, usernameLama:String){
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
                        database.child(id).child("username").setValue(username)
                        database.child(id).child("password").setValue(password)
                        database.child(id).child("sebagai").setValue(sebagai)
                        database.child(id).child("token").setValue(token)

                        list = UsersModel(
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
                    database.child(id).child("username").setValue(username)
                    database.child(id).child("password").setValue(password)
                    database.child(id).child("sebagai").setValue(sebagai)

                    list = UsersModel(
                        id,
                        nama,
                        umur,
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