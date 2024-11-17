package com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationGambarModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ListGambarBinding

class ImageAdapter(
    private var listGambar: ArrayList<InformationGambarModel>,
    var onClick: OnClickItem
): RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    class ViewHolder(var binding: ListGambarBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListGambarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listGambar.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = listGambar[position]
        holder.binding.apply {
            Glide.with(holder.itemView)
                .load("https://aplikasi-tugas.my.id/rusly/gambar/${list.gambar}") // URL Gambar
                .error(R.drawable.gambar_error_image)
                .into(ivGambarInformation) // imageView mana yang akan diterapkan

            tvKeteranganGambar.text = list.keterangan

            ivGambarInformation.setOnClickListener {
                onClick.clickGambar(list.gambar!!, list.keterangan!!, it)
            }
        }
    }

    interface OnClickItem{
        fun clickGambar(image:String, keterangan: String, it:View)
    }
}