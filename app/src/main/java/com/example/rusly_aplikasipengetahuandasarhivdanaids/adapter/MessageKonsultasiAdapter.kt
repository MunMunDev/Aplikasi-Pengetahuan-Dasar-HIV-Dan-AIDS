package com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.retrofit.ApiService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.MessageModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.TanggalDanWaktu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class MessageKonsultasiAdapter(
    val context: Context,
    var messageList: List<MessageModel>,
    var id:String,
    var numberOfDifferentDates: Int
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var tempMessageList : List<MessageModel> = messageList

    fun setData(data: List<MessageModel>, number: Int){
        numberOfDifferentDates = number
        tempMessageList = data
        notifyDataSetChanged()
    }

    val tanggalDanWaktu = TanggalDanWaktu()

    val TAG = "MessageKonsultasiAdapterTag"
    val ITEM_SENT = 1
    val ITEM_RECEIVED = 2
    val ITEM_SENT_IMAGE = 3
    val ITEM_RECEIVED_IMAGE = 4
    val ITEM_DATE_DELIMITER = 5

    var previousDate = ""
    var amountOfDataSkipped = 0

    var checkBoxMessage = false
    var nomorCheck = 0
    var valueDataIdMessage = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
            val view = LayoutInflater.from(context).inflate(R.layout.list_sent, parent, false)
            return SentViewHolder(view)
        }
        else if(viewType == 2){
            val view = LayoutInflater.from(context).inflate(R.layout.list_received, parent, false)
            return ReceivedViewHolder(view)
        }else if(viewType == 3){
            val view = LayoutInflater.from(context).inflate(R.layout.list_sent_gambar, parent, false)
            return SentViewHolderGambar(view)
        } else if(viewType == 4){
            val view = LayoutInflater.from(context).inflate(R.layout.list_received_gambar, parent, false)
            return ReceivedViewHolderGambar(view)
        } else{
            val view = LayoutInflater.from(context).inflate(R.layout.list_date_delimiter, parent, false)
            Log.d("CobaMessageTAG", "onCreateViewHolder: asd")
            return DateDelimiter(view)
        }
    }

    override fun getItemCount(): Int {
        return tempMessageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val pos = position-amountOfDataSkipped
        val list = tempMessageList[pos]

        val tanggal = if(list.tanggal.isNullOrEmpty()) "" else tanggalDanWaktu.konversiBulanSingkatan(list.tanggal!!)
        val waktu = if(list.waktu.isNullOrEmpty()) "" else tanggalDanWaktu.waktuNoSecond(list.waktu!!)
        val date = "$tanggal - $waktu"

        if(holder.javaClass == SentViewHolder::class.java){
            //Tampilkan Chat Pengirim
            val viewHolder = holder as SentViewHolder
            viewHolder.apply {
                sentMessage.text = list.message
                sentTime.text = waktu
            }

            viewHolder.sentMessage.setOnLongClickListener(object: OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {
                    val popupMenu = PopupMenu(context, v)
                    popupMenu.inflate(R.menu.popup_menu_hapus)
                    popupMenu.setOnMenuItemClickListener { v->
                        when(v.itemId){
                            R.id.hapus->
                                hapusPesan(list)
                        }

                        return@setOnMenuItemClickListener true
                    }
                    popupMenu.show()

                    return true
                }

            })

            if(checkBoxMessage){
                viewHolder.sentMessage.setOnClickListener {

                }
            }
        }
        else if(holder.javaClass == ReceivedViewHolder::class.java){
            //Tampilkan Chat Diterima
            val viewHolder = holder as ReceivedViewHolder
            viewHolder.apply {
                receivedMessage.text = list.message
                receivedTime.text = waktu
            }
        } else if(holder.javaClass == SentViewHolderGambar::class.java){
            //Tampilkan Gambar Dikirim
            val viewHolder = holder as SentViewHolderGambar
            viewHolder.sentTimeGambar.text = waktu
            Glide.with(holder.itemView)
                .load("${ApiService.BASE_URL_MYSQL}/rusly/gambar/${list.gambar}") // URL Gambar
                .error(R.drawable.gambar_error_image)
                .into(viewHolder.sentMessageGambar) // imageView mana yang akan diterapkan

            viewHolder.sentMessageGambar.setOnLongClickListener(object: OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {
                    var popupMenu = PopupMenu(context, v)
                    popupMenu.inflate(R.menu.popup_menu_hapus)
                    popupMenu.setOnMenuItemClickListener { v->
                        when(v.itemId){
                            R.id.hapus->
                                hapusPesan(list)
                        }

                        return@setOnMenuItemClickListener true
                    }
                    popupMenu.show()

                    return true
                }

            })
        } else if(holder.javaClass == ReceivedViewHolderGambar::class.java){
            //Tampilkan Gambar Diterima
            val viewHolder = holder as ReceivedViewHolderGambar
            viewHolder.receivedTimeGambar.text = waktu
            Glide.with(holder.itemView)
                .load("${ApiService.BASE_URL_MYSQL}/rusly/gambar/${list.gambar}") // URL Gambar
                .error(R.drawable.gambar_error_image)
                .into(viewHolder.receivedMessageGambar) // imageView mana yang akan diterapkan
        } else if(holder.javaClass == DateDelimiter::class.java){
            //Tampilkan Gambar Diterima
            val viewHolder = holder as DateDelimiter
            viewHolder.dateDelimiter.text = tanggal

            Log.d("CobaMessageTAG", "onBindViewHolder: $amountOfDataSkipped")
//            amountOfDataSkipped++
        }
    }

    fun hapusPesan(messageList: MessageModel){
        var view = View.inflate(context, R.layout.alert_dialog_hapus, null)
        var tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        var btnHapus = view.findViewById<Button>(R.id.btnHapus)
        var btnBatalHapus = view.findViewById<Button>(R.id.btnBatalHapus)

        var alertDialog = AlertDialog.Builder(context)
        alertDialog.setView(view)
        var dialog = alertDialog.create()
        dialog.show()

        btnHapus.setOnClickListener {
            var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("chats").child("message")

            database.addListenerForSingleValueEvent(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    database.child("${messageList.idMessage}").removeValue()
                    dialog.dismiss()
                    Toast.makeText(context, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Gagal Hapus", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }

            })
        }

        btnBatalHapus.setOnClickListener {
            dialog.dismiss()
        }

    }

    @Suppress("UNREACHABLE_CODE")
    override fun getItemViewType(position: Int): Int {
        val pos = position-amountOfDataSkipped
        val currentMessage = tempMessageList[pos]

        if(previousDate != currentMessage.tanggal!!){
            previousDate = currentMessage.tanggal!!
            Log.d("CobaMessageTAG", "getItemViewType: ")
            return ITEM_DATE_DELIMITER
        } else if(currentMessage.idSent == id){
//            if(currentMessage.message.toString().trim().isNotEmpty()){
//                return ITEM_SENT
//            } else{
//                return ITEM_SENT_IMAGE
//            }
            if(currentMessage.message.toString().trim().isEmpty()){
                return ITEM_SENT_IMAGE
            } else{
                return ITEM_SENT
            }
        }
        else{
            if(currentMessage.message.toString().trim().isEmpty()){
                return ITEM_RECEIVED_IMAGE
            } else{
                return ITEM_RECEIVED
            }
        }
    }

    class SentViewHolder(v: View): RecyclerView.ViewHolder(v){
        val sentMessage = v.findViewById<TextView>(R.id.tvMessageSent)
        val sentTime = v.findViewById<TextView>(R.id.tvWaktuSent)
    }
    class SentViewHolderGambar(v: View): RecyclerView.ViewHolder(v){
        val sentMessageGambar = v.findViewById<ImageView>(R.id.ivMessageSentImage)
        val sentTimeGambar = v.findViewById<TextView>(R.id.tvWaktuSentImage)
    }
    class ReceivedViewHolder(v: View): RecyclerView.ViewHolder(v){
        val receivedMessage = v.findViewById<TextView>(R.id.tvMessageReceived)
        val receivedTime = v.findViewById<TextView>(R.id.tvWaktuReceived)
    }
    class ReceivedViewHolderGambar(v: View): RecyclerView.ViewHolder(v){
        val receivedMessageGambar = v.findViewById<ImageView>(R.id.ivMessageReceivedImage)
        val receivedTimeGambar = v.findViewById<TextView>(R.id.tvWaktuReceivedImage)
    }

    class DateDelimiter(v: View): RecyclerView.ViewHolder(v){
        val dateDelimiter = v.findViewById<TextView>(R.id.tvDateDelimiter)
    }

}