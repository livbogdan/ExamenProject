package com.livbogdan.examenproject.models

import android.os.Parcel
import android.os.Parcelable

data class Task (
    val title: String = "",
    val createdBy: String = "",
    val cards: ArrayList<Card> = ArrayList()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Card.CREATOR)!!
    )
    override fun describeContents() = 0

    override fun writeToParcel(p0: Parcel, p1: Int)=with(p0) {
        writeString(title)
        writeString(createdBy)
        writeTypedList(cards)
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}