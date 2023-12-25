package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model

class DokterModel{
    var nama: String? = null
    var umur: Int? = null
    var username: String? = null
    var password: String? = null
    var sebagai: String? = null

    constructor(){}

    constructor(nama:String, umur:Int, username:String, password:String, sebagai: String){
        this.nama = nama
        this.umur = umur
        this.username = username
        this.password = password
        this.sebagai = sebagai
    }
}