package com.example.rbcdemo.data

import android.os.Parcelable
import com.rbc.rbcaccountlibrary.Account
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class DataItem : Parcelable {
    abstract val accountType: Long
}

@Parcelize
data class ItemAccount(val account: @RawValue Account) : DataItem() {
    override val accountType = account.type.name.hashCode().toLong()
}

@Parcelize
data class Header(val typeName: String) : DataItem() {
    override val accountType = typeName.hashCode().toLong()
}
