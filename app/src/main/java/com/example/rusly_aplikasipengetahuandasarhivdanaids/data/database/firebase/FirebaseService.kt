package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase

import android.content.Context
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseService(){
    lateinit var database : DatabaseReference
    lateinit var sharedPref : SharedPreferencesLogin
    fun firebase(): DatabaseReference{
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pengetahuan-dasar-hiv-aids-default-rtdb.firebaseio.com/")
        return database
    }
}