package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityTentangAplikasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user.MainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin

class TentangAplikasiActivity : Activity() {
    private lateinit var binding: ActivityTentangAplikasiBinding
    private lateinit var sharedPref: SharedPreferencesLogin
    private lateinit var loading: LoadingAlertDialog
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTentangAplikasiBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)

        sharedPref = SharedPreferencesLogin(this@TentangAplikasiActivity)
        loading = LoadingAlertDialog(this@TentangAplikasiActivity)
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@TentangAplikasiActivity)

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@TentangAplikasiActivity)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this@TentangAplikasiActivity, MainActivity::class.java))
        finish()

    }
}