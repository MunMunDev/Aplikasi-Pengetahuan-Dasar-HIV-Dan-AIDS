package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityInformasiHivAidsBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user.MainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin

class InformasiHivAidsActivity : Activity() {
    lateinit var binding: ActivityInformasiHivAidsBinding
    lateinit var sharedPref: SharedPreferencesLogin
    lateinit var loading: LoadingAlertDialog
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformasiHivAidsBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)

        sharedPref = SharedPreferencesLogin(this@InformasiHivAidsActivity)
        loading = LoadingAlertDialog(this@InformasiHivAidsActivity)
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@InformasiHivAidsActivity)

        binding.apply{
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@InformasiHivAidsActivity)

            // Definisi HIV AIDS
            clDefinisiHivAids.setOnClickListener {
                if(isiDefinisiHivAids.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiDefinisiHivAids.visibility = View.VISIBLE
                    arrowDefinisiHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(isiDefinisiHivAids.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiDefinisiHivAids.visibility = View.GONE
                    arrowDefinisiHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }

            // Sejarah HIV AIDS
            clSejarahHivAids.setOnClickListener {
                if(isiSejarahHivAids.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiSejarahHivAids.visibility = View.VISIBLE
                    arrowSejarahHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(isiSejarahHivAids.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiSejarahHivAids.visibility = View.GONE
                    arrowSejarahHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }

            // Gejala HIV AIDS
            clGejalaHivAids.setOnClickListener {
                if(isiGejalaHivAids.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiGejalaHivAids.visibility = View.VISIBLE
                    arrowGejalaHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(isiGejalaHivAids.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiGejalaHivAids.visibility = View.GONE
                    arrowGejalaHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }

            // Penularan HIV AIDS
            clPenularanHivAids.setOnClickListener {
                if(isiPenularanHivAids.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiPenularanHivAids.visibility = View.VISIBLE
                    arrowPenularanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(isiPenularanHivAids.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiPenularanHivAids.visibility = View.GONE
                    arrowPenularanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }

            // Metode Tes HIV AIDS
            clMetodeTesHivAids.setOnClickListener {
                if(isiMetodeTesHivAids.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiMetodeTesHivAids.visibility = View.VISIBLE
                    arrowMetodeTesHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(isiMetodeTesHivAids.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiMetodeTesHivAids.visibility = View.GONE
                    arrowMetodeTesHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }

            // Pencegahan HIV AIDS
            clPencegahanHivAids.setOnClickListener {
                if(isiPencegahanHivAids.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiPencegahanHivAids.visibility = View.VISIBLE
                    arrowPencegahanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(isiPencegahanHivAids.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiPencegahanHivAids.visibility = View.GONE
                    arrowPencegahanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }

            // Pengobatan HIV AIDS
            clPengobatanHivAids.setOnClickListener {
                if(isiPengobatanHivAids.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiPengobatanHivAids.visibility = View.VISIBLE
                    arrowPengobatanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(isiPengobatanHivAids.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiPengobatanHivAids.visibility = View.GONE
                    arrowPengobatanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }

            // Mencegah HIV AIKomplikasiDS
            clMencegahHivAidsKomplikasi.setOnClickListener {
                if(isiMencegahHivAidsKomplikasi.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiMencegahHivAidsKomplikasi.visibility = View.VISIBLE
                    arrowMencegahHivAidsKomplikasi.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(isiMencegahHivAidsKomplikasi.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiMencegahHivAidsKomplikasi.visibility = View.GONE
                    arrowMencegahHivAidsKomplikasi.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }

            // UndangUndangKerahasiaanDataPasien HIV AIDS
            clUndangUndangKerahasiaanDataPasienHivAids.setOnClickListener {
                if(isiUndangUndangKerahasiaanDataPasienHivAids.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiUndangUndangKerahasiaanDataPasienHivAids.visibility = View.VISIBLE
                    arrowUndangUndangKerahasiaanDataPasienHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(isiUndangUndangKerahasiaanDataPasienHivAids.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiUndangUndangKerahasiaanDataPasienHivAids.visibility = View.GONE
                    arrowUndangUndangKerahasiaanDataPasienHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }

            // Data Hiv Aids Di Indonesia HIV AIDS
            clDataHivAidsDiParepare.setOnClickListener {
                if(isiDataHivAidsDiParepare.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiDataHivAidsDiParepare.visibility = View.VISIBLE
                    arrowDataHivAidsDiParepare.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(isiDataHivAidsDiParepare.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    isiDataHivAidsDiParepare.visibility = View.GONE
                    arrowDataHivAidsDiParepare.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }

            // Data Hiv Aids Di Asia Pasific HIV AIDS
            clDataHivAidsDiIndonesia.setOnClickListener {
                if(llDataHivAidsDiIndonesia.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    llDataHivAidsDiIndonesia.visibility = View.VISIBLE
                    arrowDataHivAidsDiIndonesia.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(llDataHivAidsDiIndonesia.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
                    llDataHivAidsDiIndonesia.visibility = View.GONE
                    arrowDataHivAidsDiIndonesia.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this@InformasiHivAidsActivity, MainActivity::class.java))
        finish()
    }
}