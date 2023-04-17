package com.livbogdan.examenproject.models

import android.os.Parcel
import android.os.Parcelable

/* About this data Class
This is a data class named User which implements the Parcelable interface.
It has several properties such as id, name, email, image, mobile, and fcmToken.
The @Parcelize annotation is not used here, so it implements the Parcelable interface manually.
The Parcelable interface is used to serialize and deserialize objects for inter-process communication (IPC) in Android.
This allows objects to be passed between different components of an app, such as Activities or Services.

The writeToParcel function is used to write the object's properties to a Parcel, which can be sent across processes.
The createFromParcel function is used to recreate the object from a Parcel on the receiving end.
The describeContents function returns 0, which is a bitmask indicating the type of object being written to the parcel.
 */

data class User (

    val id: String = "",
    val name: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val fcmToken: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!
    ) {
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, p1: Int) = with(dest){

        writeString(id)
        writeString(name)
        writeString(email)
        writeString(image)
        writeLong(mobile)
        writeString(fcmToken)

    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
