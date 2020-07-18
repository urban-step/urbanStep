package com.spa.urbanstep.model.response

import android.os.Parcel
import android.os.Parcelable

class SubmitResponse() : Parcelable {

    var unique_no: String? = null
    var authority_name: String? = null
    var authority_email: String? = null
    var authority_title: String? = null

    constructor(parcel: Parcel) : this() {
        unique_no = parcel.readString()
        authority_name = parcel.readString()
        authority_email = parcel.readString()
        authority_title = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(unique_no)
        parcel.writeString(authority_name)
        parcel.writeString(authority_email)
        parcel.writeString(authority_title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubmitResponse> {
        override fun createFromParcel(parcel: Parcel): SubmitResponse {
            return SubmitResponse(parcel)
        }

        override fun newArray(size: Int): Array<SubmitResponse?> {
            return arrayOfNulls(size)
        }
    }
}