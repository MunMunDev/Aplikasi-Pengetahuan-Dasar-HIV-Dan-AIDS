package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter.ListInformasiAdapter
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseConfig
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationDataModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationDataWithImageModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationGambarModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityInformasiHivAidsBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogShowImageBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user.MainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class InformasiHivAidsActivity : Activity() {
    lateinit var binding: ActivityInformasiHivAidsBinding
    lateinit var sharedPref: SharedPreferencesLogin
    lateinit var loading: LoadingAlertDialog
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private var firebaseConfig = FirebaseConfig()
    private lateinit var adapter: ListInformasiAdapter
    private lateinit var listInformationGambar : ArrayList<InformationGambarModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformasiHivAidsBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)

        sharedPref = SharedPreferencesLogin(this@InformasiHivAidsActivity)
        loading = LoadingAlertDialog(this@InformasiHivAidsActivity)
        setKontrolNavigationDrawer()
        fetchImageInformation()
//        viewSelengkapnya()
        fetchData()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@InformasiHivAidsActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@InformasiHivAidsActivity)

        }
    }

    private fun fetchData(){
        var listInformation : ArrayList<InformationDataModel>
        firebaseConfig.fetchInformation().addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listInformation = arrayListOf<InformationDataModel>()
                for(value in snapshot.children){
                    var id: String? = ""
                    var urutan: String? = ""
                    var judul: String? = ""
                    var isi: String? = ""

                    id = value.child("id").value.toString()
                    urutan = value.child("urutan").value.toString()
                    judul = value.child("judul").value.toString()
                    isi = value.child("isi").value.toString()

                    listInformation.add(
                        InformationDataModel(
                            id, urutan, judul, isi
                        )
                    )
                }

                var listInformationWithImage: ArrayList<InformationDataWithImageModel> = arrayListOf()
                for((no, value) in listInformation.withIndex()){
                    val data = searchImageInformation(value.id!!)

                    listInformationWithImage.add(
                        InformationDataWithImageModel(
                            value.id, value.urutan, value.judul, value.isi, data
                        )
                    )
                }

                for ((no, value) in listInformationWithImage.withIndex()){
                    Log.d("DetailTAG", "id: ${value.id}")
                    Log.d("DetailTAG", "urutan: ${value.urutan}")
                    Log.d("DetailTAG", "judul: ${value.judul}")
                    Log.d("DetailTAG", "isi: ${value.isi}")
                    Log.d("DetailTAG", "data: ${value.arrayListImage!!.size}")
                }

//                val sortedFromUrutan = listInformation.sortedWith(compareBy { it.urutan })
//                val list = arrayListOf<InformationDataModel>()
//                list.addAll(sortedFromUrutan)
//                setAdapter(list)

                val sortedFromUrutan = listInformationWithImage.sortedWith(compareBy { it.urutan })
                val list = arrayListOf<InformationDataWithImageModel>()
                list.addAll(sortedFromUrutan)
                setAdapter(list)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InformasiHivAidsActivity, "Maaf, data gagal diambil", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapter(list: ArrayList<InformationDataWithImageModel>) {
        adapter = ListInformasiAdapter(list, object : ListInformasiAdapter.OnClickItem{
            override fun clickGambar(image: String, keterangan: String, it: View) {
                setShowImage(image, keterangan)
            }
        })
        binding.apply {
            rvInformation.layoutManager = LinearLayoutManager(
                this@InformasiHivAidsActivity, LinearLayoutManager.VERTICAL, false
            )
            rvInformation.adapter = adapter
        }
    }
    private fun setShowImage(gambar: String, title:String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@InformasiHivAidsActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitle.text = "$title"
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

        Glide.with(this@InformasiHivAidsActivity)
            .load("https://aplikasi-tugas.my.id/rusly/gambar/$gambar") // URL Gambar
            .error(R.drawable.gambar_error_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun fetchImageInformation(){
        firebaseConfig.fetchInformationGambar().addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                listInformationGambar = arrayListOf<InformationGambarModel>()
                for(value in snapshot.children){
                    var childIdInformation: String? = ""
                    var childKeterangan: String? = ""
                    var childGambar: String? = ""
                    var childWaktu: String? = ""

//                    id = value.child("id").value.toString()
//                    keterangan = value.child("keterangan").value.toString()
//                    gambar = value.child("gambar").value.toString()
//                    waktu = value.child("waktu").value.toString()

                    for(valueKedua in value.children){
                        val valueIdInformation = value.child("id_information").value.toString()
                        val valueKeterangan = value.child("keterangan").value.toString()
                        val valueGambar = value.child("gambar").value.toString()
                        val valueWaktu = value.child("waktu").value.toString()

//                        if(valueIdInformation == id){
//                            childIdInformation = valueIdInformation
//                            childKeterangan = valueKeterangan
//                            childGambar = valueGambar
//                            childWaktu = valueWaktu
//
//                            Log.d("DetailTAG", "id: $childIdInformation, id2: $valueIdInformation")
//                        }

                        childIdInformation = valueIdInformation
                        childKeterangan = valueKeterangan
                        childGambar = valueGambar
                        childWaktu = valueWaktu
                    }
                    if(childIdInformation!!.isNotEmpty()){
                        listInformationGambar.add(
                            InformationGambarModel(
                                childIdInformation, childKeterangan, childGambar, childWaktu
                            )
                        )
                    }
                }

                val sortedFromUrutan = listInformationGambar.sortedWith(compareBy { it.waktu })
                val list = arrayListOf<InformationGambarModel>()
                list.addAll(sortedFromUrutan)
                listInformationGambar = arrayListOf()
                listInformationGambar.addAll(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InformasiHivAidsActivity, "Maaf, data gagal diambil", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchImageInformation(id:String):ArrayList<InformationGambarModel>{
        val array : ArrayList<InformationGambarModel> = arrayListOf()
        for(value in listInformationGambar){
            if(value.id_information == id){
                array.add(
                    InformationGambarModel(
                        value.id_information,
                        value.keterangan,
                        value.gambar,
                        value.waktu
                    )
                )
            }
        }

        return array
    }

//    private fun viewSelengkapnya() {
//        binding.apply{
//            // Definisi HIV AIDS
//            clDefinisiHivAids.setOnClickListener {
//                if(isiDefinisiHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiDefinisiHivAids.visibility = View.VISIBLE
//                    arrowDefinisiHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiDefinisiHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiDefinisiHivAids.visibility = View.GONE
//                    arrowDefinisiHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Sejarah HIV AIDS
//            clSejarahHivAids.setOnClickListener {
//                if(isiSejarahHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiSejarahHivAids.visibility = View.VISIBLE
//                    arrowSejarahHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiSejarahHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiSejarahHivAids.visibility = View.GONE
//                    arrowSejarahHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Gejala HIV AIDS
//            clGejalaHivAids.setOnClickListener {
//                if(isiGejalaHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiGejalaHivAids.visibility = View.VISIBLE
//                    arrowGejalaHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiGejalaHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiGejalaHivAids.visibility = View.GONE
//                    arrowGejalaHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Penularan HIV AIDS
//            clPenularanHivAids.setOnClickListener {
//                if(isiPenularanHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiPenularanHivAids.visibility = View.VISIBLE
//                    arrowPenularanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiPenularanHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiPenularanHivAids.visibility = View.GONE
//                    arrowPenularanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Metode Tes HIV AIDS
//            clMetodeTesHivAids.setOnClickListener {
//                if(isiMetodeTesHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiMetodeTesHivAids.visibility = View.VISIBLE
//                    arrowMetodeTesHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiMetodeTesHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiMetodeTesHivAids.visibility = View.GONE
//                    arrowMetodeTesHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Pencegahan HIV AIDS
//            clPencegahanHivAids.setOnClickListener {
//                if(isiPencegahanHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiPencegahanHivAids.visibility = View.VISIBLE
//                    arrowPencegahanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiPencegahanHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiPencegahanHivAids.visibility = View.GONE
//                    arrowPencegahanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Pengobatan HIV AIDS
//            clPengobatanHivAids.setOnClickListener {
//                if(isiPengobatanHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiPengobatanHivAids.visibility = View.VISIBLE
//                    arrowPengobatanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiPengobatanHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiPengobatanHivAids.visibility = View.GONE
//                    arrowPengobatanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Mencegah HIV AIKomplikasiDS
//            clMencegahHivAidsKomplikasi.setOnClickListener {
//                if(isiMencegahHivAidsKomplikasi.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiMencegahHivAidsKomplikasi.visibility = View.VISIBLE
//                    arrowMencegahHivAidsKomplikasi.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiMencegahHivAidsKomplikasi.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiMencegahHivAidsKomplikasi.visibility = View.GONE
//                    arrowMencegahHivAidsKomplikasi.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // UndangUndangKerahasiaanDataPasien HIV AIDS
//            clUndangUndangKerahasiaanDataPasienHivAids.setOnClickListener {
//                if(isiUndangUndangKerahasiaanDataPasienHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiUndangUndangKerahasiaanDataPasienHivAids.visibility = View.VISIBLE
//                    arrowUndangUndangKerahasiaanDataPasienHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiUndangUndangKerahasiaanDataPasienHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiUndangUndangKerahasiaanDataPasienHivAids.visibility = View.GONE
//                    arrowUndangUndangKerahasiaanDataPasienHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Data Hiv Aids Di Indonesia HIV AIDS
//            clDataHivAidsDiParepare.setOnClickListener {
//                if(isiDataHivAidsDiParepare.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiDataHivAidsDiParepare.visibility = View.VISIBLE
//                    arrowDataHivAidsDiParepare.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiDataHivAidsDiParepare.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiDataHivAidsDiParepare.visibility = View.GONE
//                    arrowDataHivAidsDiParepare.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Data Hiv Aids Di Asia Pasific HIV AIDS
//            clDataHivAidsDiIndonesia.setOnClickListener {
//                if(llDataHivAidsDiIndonesia.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    llDataHivAidsDiIndonesia.visibility = View.VISIBLE
//                    arrowDataHivAidsDiIndonesia.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(llDataHivAidsDiIndonesia.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    llDataHivAidsDiIndonesia.visibility = View.GONE
//                    arrowDataHivAidsDiIndonesia.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//        }
//    }


    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this@InformasiHivAidsActivity, MainActivity::class.java))
        finish()
    }
}