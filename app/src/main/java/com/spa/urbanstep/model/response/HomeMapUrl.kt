package com.spa.urbanstep.model.response

import android.os.Parcel
import android.os.Parcelable

class HomeMapUrl() : Parcelable {

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(home_url)
        dest!!.writeString(area_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    var home_url: String? = null
    var area_url: String? = null

    constructor(parcel: Parcel) : this() {
        home_url = parcel.readString()
        area_url = parcel.readString()
    }

    companion object CREATOR : Parcelable.Creator<HomeMapUrl> {
        override fun createFromParcel(parcel: Parcel): HomeMapUrl {
            return HomeMapUrl(parcel)
        }

        override fun newArray(size: Int): Array<HomeMapUrl?> {
            return arrayOfNulls(size)
        }
    }
}