package com.example.rickmorty.model

import android.os.Parcel
import android.os.Parcelable

data class Character(
    val id: String? = null,
    val name: String? = null,
    val status: String? = null,
    val imageUrl: String? = null,
    val origin: String? = null,
    val location: String? = null,
    var isFavorite: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readString(),
        name = parcel.readString(),
        status = parcel.readString(),
        imageUrl = parcel.readString(),
        origin = parcel.readString(),
        location = parcel.readString(),
        isFavorite = parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(status)
        parcel.writeString(imageUrl)
        parcel.writeString(origin)
        parcel.writeString(location)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Character> {
        override fun createFromParcel(parcel: Parcel): Character {
            return Character(parcel)
        }

        override fun newArray(size: Int): Array<Character?> {
            return arrayOfNulls(size)
        }
    }
}
