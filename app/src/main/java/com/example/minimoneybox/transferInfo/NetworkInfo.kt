package com.example.minimoneybox.transferInfo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NetworkInfo(val urlPath : String?,
                       val bodyInfo : String?,
                       val bearer : String?,
                        val requestMethod: RequestMethod?) : Parcelable{
}
