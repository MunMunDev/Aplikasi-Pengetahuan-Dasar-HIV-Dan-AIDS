package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class UsersModel(
    @SerializedName("id")
    var id: String? = null,

    @SerializedName("nama")
    var nama: String? = null,

    @SerializedName("umur")
    var umur: Int? = null,

    @SerializedName("username")
    var username: String? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("sebagai")
    var sebagai: String? = null,

    @SerializedName("token")
    var token: String? = null,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nama)
        parcel.writeValue(umur)
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(sebagai)
        parcel.writeString(token)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UsersModel> {
        override fun createFromParcel(parcel: Parcel): UsersModel {
            return UsersModel(parcel)
        }

        override fun newArray(size: Int): Array<UsersModel?> {
            return arrayOfNulls(size)
        }
    }
}
