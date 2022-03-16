package com.example.rbcdemo.view.accountDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rbcdemo.data.ItemTransaction
import com.example.rbcdemo.data.TransactionDataItem
import com.example.rbcdemo.data.TransactionHeader
import com.example.rbcdemo.databinding.TransactionHeaderBinding
import com.example.rbcdemo.databinding.TransactionItemBinding

class TransactionDetailListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list: ArrayList<TransactionDataItem> = ArrayList()
    val VIEW_TYPE_TRANSACTION_HEADER = 0
    val VIEW_TYPE_TRANSACTION_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_TRANSACTION_ITEM) {
            val binding =
                TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TransactionItemViewHolder(binding)
        } else {
            val binding = TransactionHeaderBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            TransactionHeaderViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is TransactionHeader -> VIEW_TYPE_TRANSACTION_HEADER
            is ItemTransaction -> VIEW_TYPE_TRANSACTION_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_TRANSACTION_ITEM -> {
                val transactionItemHolder = holder as TransactionItemViewHolder
                val transactionItem = list[position] as ItemTransaction
                transactionItemHolder.transactionItemBinding.tvTransactionName.text = transactionItem.transaction.description
                transactionItemHolder.transactionItemBinding.tvAccountBalance.text = "$${transactionItem.transaction.amount}"
            }

            VIEW_TYPE_TRANSACTION_HEADER -> {
                val transactionHeaderHolder = holder as TransactionHeaderViewHolder
                val transactionHeaderItem = list[position] as TransactionHeader
                transactionHeaderHolder.transactionHeaderBinding.tvTransactionDate.text = transactionHeaderItem.typeTransactionDate
            }
        }
    }

    override fun getItemCount(): Int = list.size

    inner class TransactionItemViewHolder(val transactionItemBinding: TransactionItemBinding) :
        RecyclerView.ViewHolder(transactionItemBinding.root)

    inner class TransactionHeaderViewHolder(val transactionHeaderBinding: TransactionHeaderBinding) :
        RecyclerView.ViewHolder(transactionHeaderBinding.root)
}