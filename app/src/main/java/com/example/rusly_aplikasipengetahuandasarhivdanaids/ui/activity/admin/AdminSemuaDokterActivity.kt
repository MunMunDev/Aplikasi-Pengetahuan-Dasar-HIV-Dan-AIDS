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
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityAdminSemuaDokterBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar

class AdminSemuaDokterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminSemuaDokterBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var database: DatabaseReference
    lateinit var usersArrayList: ArrayList<UsersModel>
    lateinit var semuaUserAdapter: AdminListSemuaUserAdapter
    lateinit var loading: LoadingAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSemuaDokterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminSemuaDokterActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminSemuaDokterActivity)
        }

        loading = LoadingAlertDialog(this@AdminSemuaDokterActivity)
        database = FirebaseService().firebase().child("users")
        usersArrayList = ArrayList()

        binding.btnTambahData.setOnClickListener{
            dialogTambahData()
        }

        database.addValueEventListener(object: ValueEventListener {
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

                    if(sebagai.trim() == "dokter"){
                        usersArrayList.add(UsersModel(id, nama, umur, username, password, sebagai, token))
                    }
                }
                Log.d("AdminSemuaUserTAG", "onDataChange: $usersArrayList")

                semuaUserAdapter = AdminListSemuaUserAdapter(usersArrayList, object : AdminListSemuaUserAdapter.onClick{
                    override fun ClickItem(data: UsersModel, it: View) {
                        val i = Intent(this@AdminSemuaDokterActivity, AdminSemuaDokterDetailActivity::class.java)
                        i.putExtra("dataDokter", data)
                        startActivity(i)
                    }
                })
                binding.rvListUser.layoutManager = LinearLayoutManager(this@AdminSemuaDokterActivity)
                binding.rvListUser.adapter = semuaUserAdapter
                semuaUserAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminSemuaDokterActivity, "Gagal Memuat Database", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun dialogTambahData() {
        val viewAlertDialog = View.inflate(this@AdminSemuaDokterActivity, R.layout.alert_dialog_update_akun, null)

        val etNama = viewAlertDialog.findViewById<TextView>(R.id.etNama)
        val etUmur = viewAlertDialog.findViewById<TextView>(R.id.etUmur)
        val etUsername = viewAlertDialog.findViewById<TextView>(R.id.etUsername)
        val etPassword = viewAlertDialog.findViewById<TextView>(R.id.etPassword)
        val btnShowPassword = viewAlertDialog.findViewById<ImageView>(R.id.btnShowPassword)
        var cekShow = false

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val alertDialog = AlertDialog.Builder(this@AdminSemuaDokterActivity)
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

        val str = "1234567890"
        var nama = "dokter"
        val strId = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        var id = "dokter"
        for(i in 0..7){
            nama+=str.random()
            id+=strId.random()
        }
        id+="-${tanggalSekarang()}${waktuSekarang()}"

        btnSimpan.setOnClickListener {
            loading.alertDialogLoading()
            postTambahData(dialogInputan, id, etNama.text.toString(), etUmur.text.toString().toInt(), etUsername.text.toString(), etPassword.text.toString(), "dokter", "")
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun postTambahData(
        dialogInputan: AlertDialog,
        id: String,
        nama: String,
        umur: Int,
        username: String,
        password: String,
        sebagai: String,
        token: String,
    ) {
        database.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var cekUsernameTerdaftar = false
                for(value in snapshot.children){
                    val valueUsername = value.child("username").value.toString()

                    if(valueUsername.toLowerCase() == username.toLowerCase()){
                        cekUsernameTerdaftar=true
                    }
                }

                if(cekUsernameTerdaftar){
                    Toast.makeText(this@AdminSemuaDokterActivity, "Gagal Membuat Akun \nUsername sudah dimiliki orang lain", Toast.LENGTH_LONG).show()
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

                    Toast.makeText(this@AdminSemuaDokterActivity, "Berhasil Membuat Akun", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }
                dialogInputan.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminSemuaDokterActivity, "Gagal Upate Data", Toast.LENGTH_SHORT).show()
                loading.alertDialogCancel()
                dialogInputan.dismiss()
            }

        })
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
        startActivity(Intent(this@AdminSemuaDokterActivity, AdminMainActivity::class.java))
        finish()
    }
}