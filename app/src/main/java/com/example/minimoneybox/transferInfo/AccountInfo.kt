package com.example.minimoneybox.transferInfo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountInfo(val id: Int, val planValue : Double, val moneybox : Double, val name : String) : Parcelable{
}