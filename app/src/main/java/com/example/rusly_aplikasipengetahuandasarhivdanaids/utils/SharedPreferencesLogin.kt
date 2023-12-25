package com.example.rusly_aplikasipengetahuandasarhivdanaids.utils

import android.content.Context

class SharedPreferencesLogin(val context: Context) {
    val keyId = "keyId"
    val keyNama = "keyNama"
    val keyUmur = "keyUmur"
    val keyUsername = "keyUsername"
    val keyPassword = "keyPassword"
    val keySebagai = "keySebagai"
    val keyToken = "keyToken"

    var sharedPref = context.getSharedPreferences("shared_login", Context.MODE_PRIVATE)
    var editPref = sharedPref.edit()

//    fun setLogin(id:Int, umur:Int, username:String, password:String, sebagai:String){
//        editPref.apply{
//            putInt(keyId, id)
//            putInt(keyUmur, umur)
//            putString(keyUsername, username)
//            putString(keyPassword, password)
//            putString(keySebagai, sebagai)
//            apply()
//        }
//    }

    fun setLogin(id:String, nama: String, umur:Int, username:String, password:String, sebagai:String, token:String){
        editPref.apply{
            putString(keyId, id)
            putString(keyNama, nama)
            putInt(keyUmur, umur)
            putString(keyUsername, username)
            putString(keyPassword, password)
            putString(keySebagai, sebagai)
            putString(keyToken, token)
            apply()
        }
    }

    fun getId(): String{
        val id = sharedPref.getString(keyId, "").toString()
        return id
    }
    fun getNama(): String{
        val nama = sharedPref.getString(keyNama, "").toString()
        return nama
    }
    fun getUmur(): Int{
        val umur = sharedPref.getInt(keyUmur, 0)
        return umur
    }
    fun getUsername():String{
        val username = sharedPref.getString(keyUsername, "").toString()
        return username
    }
    fun getPassword(): String{
        val password = sharedPref.getString(keyPassword, "").toString()
        return password
    }

    fun getSebagai(): String{
        val sebagai = sharedPref.getString(keySebagai, "").toString()
        return sebagai
    }

    fun getToken(): String{
        val token = sharedPref.getString(keyToken, "").toString()
        return token
    }
}