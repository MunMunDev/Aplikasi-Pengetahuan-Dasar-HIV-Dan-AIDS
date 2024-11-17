package com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.UsersModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.ChatActivity
import kotlin.collections.ArrayList

class ListKonsultasiUserAdapter(
    var context:Context,
    var arrayList: ArrayList<UsersModel>
): RecyclerView.Adapter<ListKonsultasiUserAdapter.KonsultasiViewHolder>() {
    var arrayListTemp: ArrayList<UsersModel> = arrayList
    fun updateData(cariData: Set<String>){
        for(value in cariData){
            val index = arrayListTemp.indexOfLast {
                it.id == value
            }
            if(index != -1){
                val data = arrayList[index]
                data.apply {
                    val model = UsersModel(
                        id, nama, umur, username, password, sebagai, token, "ada"
                    )
                    arrayListTemp[index] = model
                }
            }
        }
        notifyDataSetChanged()
    }

    class KonsultasiViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val tvNama: TextView
        val ivAdaPesan: ImageView
        val clListKonsultasi: ConstraintLayout
        init {
            tvNama = v.findViewById(R.id.tvNama)
            ivAdaPesan = v.findViewById(R.id.ivAdaPesan)
            clListKonsultasi = v.findViewById(R.id.clListKonsultasi)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KonsultasiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_konsultasi, parent, false)

        return KonsultasiViewHolder(view)
    }

    override fun getItemCount(): Int {
        return (arrayListTemp.size)
    }

    override fun onBindViewHolder(holder: KonsultasiViewHolder, position: Int) {
        holder.apply {
            tvNama.text = arrayListTemp[position].nama
            if(arrayListTemp[position].dibaca == "ada"){
                ivAdaPesan.visibility = View.VISIBLE
            } else{
                ivAdaPesan.visibility = View.GONE
            }

            clListKonsultasi.setOnClickListener {
//            Toast.makeText(context, "Pindah ${arrayListTemp[position].nama} dan ${arrayListTemp[position].id}", Toast.LENGTH_SHORT).show()

                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("id", arrayListTemp[position].id)
                intent.putExtra("nama", arrayListTemp[position].nama)
                intent.putExtra("token", arrayListTemp[position].token)
                context.startActivity(intent)
            }
        }
    }

}