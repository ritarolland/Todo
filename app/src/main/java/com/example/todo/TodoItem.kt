package com.example.homework1

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class TodoItem(
    val id: String?,
    val text: String?,
    val importance: Importance,
    val deadline: Date? = null,
    var isDone: Boolean,
    val createdAt: Date,
    val updatedAt: Date? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        TODO("importance"),
        TODO("deadline"),
        parcel.readByte() != 0.toByte(),
        TODO("createdAt"),
        TODO("updatedAt")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(text)
        parcel.writeByte(if (isDone) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TodoItem> {
        override fun createFromParcel(parcel: Parcel): TodoItem {
            return TodoItem(parcel)
        }

        override fun newArray(size: Int): Array<TodoItem?> {
            return arrayOfNulls(size)
        }
    }

}
