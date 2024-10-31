package com.example.project1763.Model
import android.os.Parcel
import android.os.Parcelable


data class OrderModel(
    var orderId: String = "",
    var items: List<OrderModelList> = listOf(),
    var city: String = "",
    var diachi: String = "",
    var fullname: String = "",
    var Total : Double =0.0 ,
    var date: String = ""
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.createTypedArrayList(OrderModelList) ?: listOf(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(), // Đọc trường date từ Parcel
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(orderId)
        dest.writeTypedList(items)
        dest.writeString(city)
        dest.writeString(diachi)
        dest.writeString(fullname)
        dest.writeDouble(Total)
        dest.writeString(date)
    }

    companion object CREATOR : Parcelable.Creator<OrderModel> {
        override fun createFromParcel(parcel: Parcel): OrderModel {
            return OrderModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderModel?> {
            return arrayOfNulls(size)
        }
    }
}
