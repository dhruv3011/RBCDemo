package com.example.rbcdemo.data

import android.os.Parcelable
import com.rbc.rbcaccountlibrary.Transaction
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class TransactionDataItem : Parcelable {
    abstract val transactionDate: Long
}

@Parcelize
data class ItemTransaction(val transaction: @RawValue Transaction) : TransactionDataItem() {
    override val transactionDate = transaction.date.hashCode().toLong()
}

@Parcelize
data class TransactionHeader(val typeTransactionDate: String) : TransactionDataItem() {
    override val transactionDate = typeTransactionDate.hashCode().toLong()
}