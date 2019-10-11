package com.example.plz_prepare_mng

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

class Menu(
    var Fname: String?,
    var Fprice: Int?,
    var Fexplain: String?
) : Parcelable {
    constructor(source: Parcel) : this(
    source.readString(),
    source.readValue(Int::class.java.classLoader) as Int?,
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(Fname)
        writeValue(Fprice)
        writeString(Fexplain)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Menu> = object : Parcelable.Creator<Menu> {
            override fun createFromParcel(source: Parcel): Menu = Menu(source)
            override fun newArray(size: Int): Array<Menu?> = arrayOfNulls(size)
        }
    }
}