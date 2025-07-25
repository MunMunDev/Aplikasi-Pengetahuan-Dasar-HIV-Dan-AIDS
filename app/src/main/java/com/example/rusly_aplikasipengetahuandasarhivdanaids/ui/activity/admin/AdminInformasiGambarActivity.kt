package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter.AdminListInformasiGambarAdapter
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseConfig
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.retrofit.ApiService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationGambarModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.ResponseModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityAdminInformasiGambarBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogAdminInformasiGambarBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogKeteranganBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogKonfirmasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogShowImageBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KataAcak
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.OnClickItem
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.TanggalDanWaktu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminInformasiGambarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminInformasiGambarBinding
    private var firebaseConfig = FirebaseConfig()
    private lateinit var adapter: AdminListInformasiGambarAdapter
    private var idInformation = ""
    private var idInformationGambar = ""

    private var STORAGE_PERMISSION_CODE = 10
    private var IMAGE_CODE = 10

    private var tempView: AlertDialogAdminInformasiGambarBinding? = null
    private var fileImage: MultipartBody.Part? = null
    private var tanggalDanWaktu = TanggalDanWaktu()
    private var kataAcak = KataAcak()

    private var image: String? = null
    private var keterangan: String? = null
    private var nameImage: String? = null
    private var waktu: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminInformasiGambarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setButton()
    }

    private fun setDataSebelumnya() {
        val bundle = intent.extras
        if(bundle != null){
            idInformation = bundle.getString("idInformation")!!
            val judul = bundle.getString("judul")!!
            binding.title.text = judul
            fetchData(idInformation)
        }
    }

    private fun setButton() {
        binding.apply {
            btnTambahData.setOnClickListener{
                showDialogTambahData()
            }
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun showDialogTambahData() {
        val view = AlertDialogAdminInformasiGambarBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminInformasiGambarActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view

        view.apply {
            etGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }


            btnSimpan.setOnClickListener{
                var check = true
                if(etKeterangan.text.toString().isEmpty()){
                    etKeterangan.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etGambar.text.toString().trim() == resources.getString(R.string.ket_klik_file)){
                    etGambar.error = "Masukkan Data"
                    check = false
                }

                if(check){
                    keterangan = etKeterangan.text.toString()
                    waktu = "${tanggalDanWaktu.tanggalSekarangZonaMakassar()}-${tanggalDanWaktu.waktuSekarangZonaMakassar()}"
                    idInformationGambar = "information-$idInformation-$waktu"
                    nameImage = "information-$idInformation-$waktu-${kataAcak.getHurufDanAngka(5)}.png"

                    Toast.makeText(this@AdminInformasiGambarActivity, "$waktu", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@AdminInformasiGambarActivity, "$nameImage", Toast.LENGTH_SHORT).show()

                    val post = convertStringToMultipartBody("post_gambar_chat")
                    postTambahInformasiGambar(post, convertStringToMultipartBody("$nameImage"), fileImage)
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahInformasiGambar(
        post: RequestBody, nameImage: RequestBody, fileImage: MultipartBody.Part?
    ) {
        ApiService.getRetrofitMySql().postAdminTambahGambarInformation(
            post, nameImage, fileImage!!
        ).enqueue(object: Callback<ArrayList<ResponseModel>>{
            override fun onResponse(
                call: Call<ArrayList<ResponseModel>>,
                response: Response<ArrayList<ResponseModel>>
            ) {
                if(response.body()!!.isNotEmpty()){
                    Toast.makeText(this@AdminInformasiGambarActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()
                    postFirebaseDataGambar()
                } else{
                    Toast.makeText(this@AdminInformasiGambarActivity, "Gagal Upload image", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<ResponseModel>>, t: Throwable) {
                Toast.makeText(this@AdminInformasiGambarActivity, "Gagal Upload image", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postFirebaseDataGambar() {
        adapter.notifyDataSetChanged()
        firebaseConfig.fetchInformationGambar().child("$idInformationGambar").apply {
            child("id_information").setValue("$idInformation")
            child("keterangan").setValue("$keterangan")
            child("gambar").setValue("$nameImage")
            child("waktu").setValue("$waktu")
        }
    }

    private fun fetchData(idInformation: String) {
        var listInformation : ArrayList<InformationGambarModel>

        firebaseConfig.fetchInformationGambar().addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                listInformation = arrayListOf<InformationGambarModel>()
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

                        if(valueIdInformation == idInformation){
                            childIdInformation = valueIdInformation
                            childKeterangan = valueKeterangan
                            childGambar = valueGambar
                            childWaktu = valueWaktu

                            Log.d("DetailTAG", "id: $childIdInformation, id2: $valueIdInformation")
                        }
                    }
                    if(childIdInformation!!.isNotEmpty()){
                        listInformation.add(
                            InformationGambarModel(
                                childIdInformation, childKeterangan, childGambar, childWaktu
                            )
                        )
                    }
                }

                val sortedFromUrutan = listInformation.sortedWith(compareBy { it.waktu })
                val list = arrayListOf<InformationGambarModel>()
                list.addAll(sortedFromUrutan)
                setAdapter(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminInformasiGambarActivity, "Maaf, data gagal diambil", Toast.LENGTH_SHORT).show()
            }

        })

//        Lothis@AdminInformasiGambarActivityTAG", "size: ${listInformation.size}")
//        Lothis@AdminInformasiGambarActivityTAG", "size: ${listInformation.size}")
    }

    private fun setAdapter(list: ArrayList<InformationGambarModel>) {
        adapter = AdminListInformasiGambarAdapter(list, object: OnClickItem.AdminInformationGambar{
            override fun clickItemSetting(information: InformationGambarModel, it: View) {
                val popupMenu = PopupMenu(this@AdminInformasiGambarActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                showDialogEditInformasi(information)
                                return true
                            }
                            R.id.hapus -> {
                                showDialogHapusInformasi(information)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

            override fun clickItemKeterangan(keterangan: String, it: View) {
                showClickText(keterangan, "Judul Gambar")
            }

            override fun clickItemGambar(gambar: String, keterangan:String, it: View) {
                setShowImage(gambar, keterangan)
            }

        })
        binding.apply {
            rvInformasi.layoutManager = LinearLayoutManager(
                this@AdminInformasiGambarActivity, LinearLayoutManager.VERTICAL, false
            )
            rvInformasi.adapter = adapter
        }
    }

    private fun showDialogEditInformasi(information: InformationGambarModel) {
        val view = AlertDialogAdminInformasiGambarBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminInformasiGambarActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view

        view.apply {
            etKeterangan.setText(information.keterangan)

            etGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

            btnSimpan.setOnClickListener{
                var check = true
                if(etKeterangan.text.toString().isEmpty()){
                    etKeterangan.error = "Tidak Boleh Kosong"
                    check = false
                }

                if(check){
                    keterangan = etKeterangan.text.toString()
                    nameImage = "information-${information.id_information}-${information.waktu}-${kataAcak.getHurufDanAngka(5)}.png"
                    idInformation = "information-${information.id_information!!}-${information.waktu!!}"

                    val post = convertStringToMultipartBody("post_gambar_chat")

                    if(etGambar.text.toString().trim() == resources.getString(R.string.ket_klik_file)){
                        postUpdateFirebaseData(idInformation, keterangan!!)
                    } else{
                        postUpdateFirebaseDataGambar(idInformation, keterangan!!, nameImage!!)
                        postUpdateInformasiGambar(post, convertStringToMultipartBody("$nameImage"), fileImage)
                    }
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateInformasiGambar(
        post: RequestBody, nameImage: RequestBody, fileImage: MultipartBody.Part?
    ) {
        ApiService.getRetrofitMySql().postAdminTambahGambarInformation(
            post, nameImage, fileImage!!
        ).enqueue(object: Callback<ArrayList<ResponseModel>>{
            override fun onResponse(
                call: Call<ArrayList<ResponseModel>>,
                response: Response<ArrayList<ResponseModel>>
            ) {
                if(response.body()!!.isNotEmpty()){
                    Toast.makeText(this@AdminInformasiGambarActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()
                } else{
                    Toast.makeText(this@AdminInformasiGambarActivity, "Gagal Upload image", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<ResponseModel>>, t: Throwable) {
                Toast.makeText(this@AdminInformasiGambarActivity, "Gagal Upload image", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postUpdateFirebaseData(idInfor:String, keterangan: String) {
        firebaseConfig.fetchInformationGambar().child("$idInfor").apply {
            child("keterangan").setValue("$keterangan")
        }
        adapter.notifyDataSetChanged()
    }

    private fun postUpdateFirebaseDataGambar(idInfor:String, keterangan: String, gambar: String) {
        firebaseConfig.fetchInformationGambar().child("$idInfor").apply {
//            child("id_information").setValue("$idInformation")
            child("keterangan").setValue("$keterangan")
            child("gambar").setValue("$gambar")
//            child("waktu").setValue("$waktu")
        }
        adapter.notifyDataSetChanged()
    }

    private fun showDialogHapusInformasi(information: InformationGambarModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminInformasiGambarActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Hapus Data Ini?"
            tvBodyKonfirmasi.text = "Setelah menghapus, data tidak dapat dikembalikan. "

            btnKonfirmasi.setOnClickListener{
                binding.apply {
                    val id = "information-${information.id_information}-${information.waktu}"
                    postDeleteFirebaseDataGambar(id)
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postDeleteFirebaseDataGambar(id:String) {
        firebaseConfig.fetchInformationGambar().child("$id").ref.removeValue().addOnSuccessListener{
            Toast.makeText(this@AdminInformasiGambarActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showClickText(keterangan:String, judul: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminInformasiGambarActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = keterangan
            tvBodyKeterangan.text = judul
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowImage(gambar: String, title:String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminInformasiGambarActivity)
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

        Glide.with(this@AdminInformasiGambarActivity)
            .load("${ApiService.BASE_URL_MYSQL}/rusly/gambar/$gambar") // URL Gambar
            .error(R.drawable.gambar_error_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()) {
                startActivity(Intent(this, AdminInformasiGambarActivity::class.java))
            } else { //request for the permission
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        } else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun pickImageFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
        }

        startActivityForResult(intent, IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Mendapatkan URI file PDF yang dipilih
            val fileUri = data.data!!

            val nameImage = getNameFile(fileUri)
            Log.d("DetailTAG", "data: $nameImage")

            tempView?.let {
                it.etGambar.text = nameImage
            }

            // Mengirim file PDF ke website menggunakan Retrofit
            fileImage = uploadImageToStorage(fileUri, nameImage, "gambar")
        }
    }

    private fun getNameFile(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor?.moveToFirst()
        val name = cursor?.getString(nameIndex!!)
        cursor?.close()
        return name!!
    }

    @SuppressLint("Recycle")
    private fun uploadImageToStorage(pdfUri: Uri?, pdfFileName: String, nameAPI:String): MultipartBody.Part? {
        var pdfPart : MultipartBody.Part? = null
        pdfUri?.let {
            val file = contentResolver.openInputStream(pdfUri)?.readBytes()

            if (file != null) {
//                // Membuat objek RequestBody dari file PDF
//                val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
//                // Membuat objek MultipartBody.Part untuk file PDF
//                pdfPart = MultipartBody.Part.createFormData("materi_pdf", pdfFileName, requestFile)

                pdfPart = convertFileToMultipartBody(file, pdfFileName, nameAPI)
            }
        }
        return pdfPart
    }

    private fun convertFileToMultipartBody(file: ByteArray, pdfFileName: String, nameAPI:String): MultipartBody.Part?{
        val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(nameAPI, pdfFileName, requestFile)

        return filePart
    }

    private fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        }
        else{
            //Android is below 11(R)
            val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun convertStringToMultipartBody(data: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), data)
    }

}