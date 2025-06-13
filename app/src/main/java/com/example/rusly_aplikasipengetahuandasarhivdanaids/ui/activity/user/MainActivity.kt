package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityMainBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.InformasiHivAidsActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.KonsultasiActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.TentangAplikasiActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.google.auth.oauth2.GoogleCredentials
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Date


class MainActivity : Activity() {
    lateinit var binding: ActivityMainBinding
    lateinit var sharedPref: SharedPreferencesLogin
    lateinit var loading: LoadingAlertDialog
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private var NOTIFICATION_PERMISSION_CODE = 11
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

        getNotificationPermission()


        Log.d("messagingTokenData", "onCreate: ${sharedPref.getToken()}")
//        Toast.makeText(this@MainActivity, "${sharedPref.getToken()}", Toast.LENGTH_SHORT).show()

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
                tvTitleKonsultasai.text = "Konsultasi Konselor"
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

//            Toast.makeText(this@MainActivity, "${sharedPref.getToken()}", Toast.LENGTH_SHORT).show()
        }
    }

    //Permission notifikasi android 13+
    private fun getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        } catch (e: Exception) {
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NOTIFICATION_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // allow
                } else {
                    Toast.makeText(this@MainActivity, "Tidak dapat menerima notification", Toast.LENGTH_SHORT).show()
                    //deny
                }
                return
            }
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