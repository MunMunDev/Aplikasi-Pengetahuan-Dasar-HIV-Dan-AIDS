package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.retrofit.ApiService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.ResponseModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityLupaPasswordBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class LupaPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLupaPasswordBinding
    lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLupaPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
    }

    private fun setButton() {
        binding.apply {
            btnKirim.setOnClickListener {
                if(etEmail.text.toString().isEmpty()){
                    etEmail.error = "Tidak boleh kosong"
                } else{
                    hitungMundur()
                    cekDataDiDatabase(etEmail.text.toString().trim())
                }
            }
        }
    }

    private fun cekDataDiDatabase(email: String) {
        database = FirebaseService().firebase().child("users")
        database.addListenerForSingleValueEvent(object: ValueEventListener {
//        database.addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                var cekEmail = ""
                var cekUsername = ""
                var cekPassword = ""
                for(value in snapshot.children){
                    var valueId: String? = ""
                    var valueEmail: String? = ""
                    var valueNama: String? = ""
                    var valueUsername: String? = ""
                    var valuePassword: String? = ""
                    var valueUmur: String? = ""
                    var valueSebagai: String? = ""

                    for(valueKedua in value.children){
                        val childId = value.child("id").value.toString()
                        val childEmail = value.child("email").value.toString()
                        val childNama = value.child("nama").value.toString()
                        val childUsername = value.child("username").value.toString()
                        val childPassword = value.child("password").value.toString()
                        val childUmur = value.child("umur").value.toString()
                        val childSebagai = value.child("sebagai").value.toString()

                        if(childEmail == email){
                            valueId = childId
                            valueEmail = childEmail
                            valueNama = childNama
                            valueUsername = childUsername
                            valuePassword = childPassword
                            valueUmur = childUmur
                            valueSebagai = childSebagai
                        }
                    }
                    if(valueId!!.isNotEmpty()){
//                        postData(email, valueUsername!!, valuePassword!!)
                        cekEmail = email
                        cekUsername = valueUsername!!
                        cekPassword = valuePassword!!
                    }
                }
                if(cekEmail.isNotEmpty()){
                    postData(email, cekUsername, cekPassword)
                } else{
                    Toast.makeText(this@LupaPasswordActivity, "Data tidak ditemukan di database", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LupaPasswordActivity, "Gagal database", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postData(email: String, username: String, password: String) {
        ApiService.getRetrofitMySql()
            .postLupaAkun("", email, username, password)
            .enqueue(object : Callback<ResponseModel>{
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    Toast.makeText(this@LupaPasswordActivity, "Silahkan Periksa Email Anda", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@LupaPasswordActivity, "gagal post data", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun hitungMundur(){
        binding.btnKirim.isEnabled = false
        val countDown = object : CountDownTimer(30000, 1000){
            override fun onTick(p0: Long) {
                val numberFormat = DecimalFormat("00")

                val number = numberFormat.format(p0 / 1000 % 60)
                binding.btnKirim.text = number.toString()
            }

            override fun onFinish() {
                binding.btnKirim.text = "KIRIM"
                binding.btnKirim.isEnabled = true
            }

        }

        countDown.start()
    }
}