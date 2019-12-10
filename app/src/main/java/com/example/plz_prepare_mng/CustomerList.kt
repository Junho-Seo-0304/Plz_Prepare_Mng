package com.example.plz_prepare_mng

import android.os.Parcel
import android.os.Parcelable

// 주문자의 주문 번호와 주문자의 주문 리스트를 가지는 class
class CustomerList(
    val Cnumber: Int,
    val orderList: String?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(Cnumber)
        writeString(orderList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CustomerList> = object : Parcelable.Creator<CustomerList> {
            override fun createFromParcel(source: Parcel): CustomerList = CustomerList(source)
            override fun newArray(size: Int): Array<CustomerList?> = arrayOfNulls(size)
        }
    }
}