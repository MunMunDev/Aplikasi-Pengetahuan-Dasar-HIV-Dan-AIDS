package com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationDataModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationDataWithImageModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ListInformasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.OnClickItem

class ListInformasiAdapter(
    private var listInformation: ArrayList<InformationDataWithImageModel>,
    var onClick: ListInformasiAdapter.OnClickItem
): RecyclerView.Adapter<ListInformasiAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListInformasiBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListInformasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listInformation.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val information = listInformation[position]
        holder.binding.apply {
            tvHeader.text = information.judul
            tvText.text = information.isi

            val adapter = ImageAdapter(information.arrayListImage!!, object : ImageAdapter.OnClickItem{
                override fun clickGambar(image: String, keterangan: String, it: View) {
                    onClick.clickGambar(image, keterangan, it)
                }
            })
            rvImage.layoutManager = GridLayoutManager(holder.itemView.context, 2)
            rvImage.adapter = adapter

            clHeader.setOnClickListener {
                if(llBody.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(llBody, AutoTransition())
                    llBody.visibility = View.VISIBLE
                    arrowHeader.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(llBody.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(llBody, AutoTransition())
                    llBody.visibility = View.GONE
                    arrowHeader.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }
        }
    }

    interface OnClickItem{
        fun clickGambar(image:String, keterangan: String, it:View)
    }
}