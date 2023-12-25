package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityMainBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.InformasiHivAidsActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.KonsultasiActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.TentangAplikasiActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin

class MainActivity : Activity() {
    lateinit var binding: ActivityMainBinding
    lateinit var sharedPref: SharedPreferencesLogin
    lateinit var loading: LoadingAlertDialog
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(view)

        sharedPref = SharedPreferencesLogin(this@MainActivity)
        loading = LoadingAlertDialog(this@MainActivity)
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@MainActivity)

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, igDrawerView, this@MainActivity)

            btnInformasiHivAids.setOnClickListener {
                startActivity(Intent(this@MainActivity, InformasiHivAidsActivity::class.java))
                finish()
            }
            btnKonsultasiDokter.setOnClickListener {
                startActivity(Intent(this@MainActivity, KonsultasiActivity::class.java))
                finish()
            }
            btnTentangAplikasi.setOnClickListener {
                startActivity(Intent(this@MainActivity, TentangAplikasiActivity::class.java))
                finish()
            }
            btnAkun.setOnClickListener {
                startActivity(Intent(this@MainActivity, UpdateAkunActivity::class.java))
                finish()
            }

            if(sharedPref.getSebagai() == "dokter"){
                tvTitleKonsultasai.text = "Konsultasi Pasien"
            }
            else if(sharedPref.getSebagai() == "user"){
                tvTitleKonsultasai.text = "Konsultasi Dokter"
            }

//            getToken.setOnClickListener {
//                val server = "c7qrm-kKQJiU4FbuJer-9N:APA91bHAH0TNor3cxU4e0a4J_of3QfSbHzaA8X-vTvOtaB-7YbTkxJyKYJF6WHtkVrk7F6nyTuACiKRX3p4aeqDqNPJPndR2ThLa6X94XHQr9lofdRrCRgW6WAqF43Dq4F0DFlUrcKlj"
//                try {
//                    ApiService.getRetrofit().postChat(PushNotificationModel(NotificationModel("Dokter ini","Halo pasien apa kabar"), server))
//                        .enqueue(object: Callback<PushNotificationModel> {
//                            override fun onResponse(
//                                call: Call<PushNotificationModel>,
//                                response: Response<PushNotificationModel>
//                            ) {
//                                Toast.makeText(this@MainActivity, "Berhasil", Toast.LENGTH_SHORT).show()
//                            }
//
//                            override fun onFailure(
//                                call: Call<PushNotificationModel>,
//                                t: Throwable
//                            ) {
//                                Toast.makeText(this@MainActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
//                            }
//
//                        })
//                }catch (ex: Exception){
//                    Log.d("messagingToken", "onCreate: $ex")
//                }
//                FirebaseMessaging.getInstance().token.apply {
//                    addOnSuccessListener { p0 ->
//                        Log.d("messagingToken", "onSuccess: $p0")
//
//                    }
//                    addOnCanceledListener {
//                        Toast.makeText(this@MainActivity, "Gagal Mendapatkan Token", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
        }
    }

    var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@MainActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)

    }
}