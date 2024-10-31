package com.example.project1763.Model

import android.os.Parcel
import android.os.Parcelable

data class OrderModelList(
    var title: String = "",
    var price: Int = 0,
    var numberInCart: Int = 0,
    var selectedSize: String = "",
    var urlPic: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeInt(price)
        dest.writeInt(numberInCart)
        dest.writeString(selectedSize)
        dest.writeString(urlPic)
    }

    companion object CREATOR : Parcelable.Creator<OrderModelList> {
        override fun createFromParcel(parcel: Parcel): OrderModelList {
            return OrderModelList(parcel)
        }

        override fun newArray(size: Int): Array<OrderModelList?> {
            return arrayOfNulls(size)
        }
    }
}
