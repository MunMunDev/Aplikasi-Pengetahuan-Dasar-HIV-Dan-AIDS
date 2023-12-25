package com.example.rusly_aplikasipengetahuandasarhivdanaids.utils

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R

class LoadingAlertDialog(context: Context) {
    lateinit var dialog: AlertDialog
    val valueContext = context

    fun alertDialogLoading(){
        val view = View.inflate(valueContext, R.layout.alert_dialog_loading, null)
        val alertDialogBuilder = AlertDialog.Builder(valueContext)
        alertDialogBuilder.setView(view)

        dialog = alertDialogBuilder.create()
        dialog.setCancelable(false)
        dialog.show()
    }

    fun alertDialogCancel(){
        dialog.dismiss()
    }
}