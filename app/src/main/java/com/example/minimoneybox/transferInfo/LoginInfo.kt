package com.example.minimoneybox.transferInfo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class LoginInfo(val name: String,
                       val password: String,
                       val email: String) : Parcelable


