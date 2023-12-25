package com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.UsersModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.ChatActivity
import java.util.ArrayList

class ListKonsultasiUserAdapter(var context:Context, var arrayList: ArrayList<UsersModel>): RecyclerView.Adapter<ListKonsultasiUserAdapter.KonsultasiViewHolder>() {
    class KonsultasiViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val tvNama: TextView
        val clListKonsultasi: ConstraintLayout
        init {
            tvNama = v.findViewById(R.id.tvNama)
            clListKonsultasi = v.findViewById(R.id.clListKonsultasi)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KonsultasiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_konsultasi, parent, false)

        return KonsultasiViewHolder(view)
    }

    override fun getItemCount(): Int {
        return (arrayList.size)
    }

    override fun onBindViewHolder(holder: KonsultasiViewHolder, position: Int) {
        holder.tvNama.text = arrayList[position].nama

        holder.clListKonsultasi.setOnClickListener {
//            Toast.makeText(context, "Pindah ${arrayList[position].nama} dan ${arrayList[position].id}", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("id", arrayList[position].id)
            intent.putExtra("nama", arrayList[position].nama)
            intent.putExtra("token", arrayList[position].token)
            context.startActivity(intent)
        }
    }

}