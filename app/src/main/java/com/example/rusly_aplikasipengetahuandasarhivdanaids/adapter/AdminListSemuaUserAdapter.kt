package com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.UsersModel

class AdminListSemuaUserAdapter(
    private val list: ArrayList<UsersModel>,
    private val click: onClick
): RecyclerView.Adapter<AdminListSemuaUserAdapter.ViewHolder>() {
    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val clBody: ConstraintLayout
        val tvNama: TextView
        val tvUmur: TextView
        init {
            clBody = v.findViewById(R.id.clBody)
            tvNama = v.findViewById(R.id.tvNama)
            tvUmur = v.findViewById(R.id.tvUmur)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val valueList = list[position]
        holder.apply {
            tvNama.text = valueList.nama
            tvUmur.text = "Umur: ${valueList.umur} Tahun"
            clBody.setOnClickListener {
                click.ClickItem(valueList, it)
            }
        }
    }

    interface onClick{
        fun ClickItem(data: UsersModel, it:View)
    }
}