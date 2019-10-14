package com.example.plz_prepare_mng

import android.os.Parcel
import android.os.Parcelable

class Order(
    var num: Int,
    var Fname: String?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(num)
        writeString(Fname)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Order> = object : Parcelable.Creator<Order> {
            override fun createFromParcel(source: Parcel): Order = Order(source)
            override fun newArray(size: Int): Array<Order?> = arrayOfNulls(size)
        }
    }
}