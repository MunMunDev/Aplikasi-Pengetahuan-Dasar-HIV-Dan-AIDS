package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityAdminMainBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminMainActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminMainActivity)
        }

        setButton()

    }

    private fun setButton() {
        binding.apply {
            btnSemuaDokter.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminSemuaDokterActivity::class.java))
                finish()
            }
            btnSemuaUser.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminSemuaUserActivity::class.java))
                finish()
            }
        }
    }
}