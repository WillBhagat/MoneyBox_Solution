package com.example.minimoneybox.transferInfo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionData(val accountInfo: ArrayList<AccountInfo?>, val loginInfo: LoginInfo?, val activeAccount : AccountInfo?, val networkInfo: NetworkInfo?) : Parcelable {
}