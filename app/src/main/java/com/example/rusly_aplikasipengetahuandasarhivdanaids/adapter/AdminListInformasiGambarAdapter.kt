package com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.retrofit.ApiService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationDataModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationGambarModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ListAdminInformasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ListAdminInformasiGambarBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.OnClickItem

class AdminListInformasiGambarAdapter(
    private var listInformation: ArrayList<InformationGambarModel>,
    private var onClick: OnClickItem.AdminInformationGambar
): RecyclerView.Adapter<AdminListInformasiGambarAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListAdminInformasiGambarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListAdminInformasiGambarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(listInformation.size>0){
            listInformation.size+1
        } else{
            0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvKeterangan.text = "Keterangan Gambar "
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvKeterangan.setBackgroundResource(R.drawable.bg_table_title)
                ivGambar.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvKeterangan.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvKeterangan.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val information = listInformation[(position-1)]

                tvNo.text = "$position"
                tvKeterangan.text = information.keterangan
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvKeterangan.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvKeterangan.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvKeterangan.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvKeterangan.gravity = Gravity.CENTER_VERTICAL

                tvGambar.visibility = View.GONE
                ivGambar.visibility = View.VISIBLE

                Glide.with(holder.itemView)
                    .load("${ApiService.BASE_URL_MYSQL}/rusly/gambar/${information.gambar}") // URL Gambar
                    .error(R.drawable.gambar_error_image)
                    .into(holder.binding.ivGambar) // imageView mana yang akan diterapkan

                tvKeterangan.setOnClickListener{
                    onClick.clickItemKeterangan(information.keterangan!!, it)
                }
                ivGambar.setOnClickListener{
                    onClick.clickItemGambar(information.gambar!!, information.keterangan!!, it)
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(information, it)
                }
            }
        }
    }
}