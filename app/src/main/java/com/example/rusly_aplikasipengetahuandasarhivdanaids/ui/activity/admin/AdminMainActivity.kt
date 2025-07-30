package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityAdminMainBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminMainActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminMainActivity)
        }

        sharedPreferencesLogin = SharedPreferencesLogin(this@AdminMainActivity)

        setButton()

    }

    private fun setButton() {
        binding.apply {
            btnDataInformasiHivAids.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminInformasiHivAidsActivity::class.java))
                finish()
            }
            btnPertanyaanOtomatis.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminPertanyaanOtomatisActivity::class.java))
                finish()
            }
            btnSemuaDokter.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminSemuaDokterActivity::class.java))
                finish()
            }
            btnSemuaUser.setOnClickListener {
//                Toast.makeText(this@AdminMainActivity, "Fitur masih terkunci", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@AdminMainActivity, AdminSemuaUserActivity::class.java))
                finish()
            }
        }
    }
}