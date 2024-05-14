package com.example.zad1

import android.os.Parcel
import android.os.Parcelable

data class Trail(val name: String, val difficulty: String, val description: String, val length: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()) {}

    companion object CREATOR : Parcelable.Creator<Trail> {
        override fun createFromParcel(parcel: Parcel): Trail {
            return Trail(parcel)
        }

        override fun newArray(size: Int): Array<Trail?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }
}