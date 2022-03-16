package com.example.rbcdemo.view.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rbcdemo.data.DataItem
import com.example.rbcdemo.data.Header
import com.example.rbcdemo.data.ItemAccount
import com.example.rbcdemo.databinding.HeaderBinding
import com.example.rbcdemo.databinding.ItemBinding

class AccountListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list: ArrayList<DataItem> = ArrayList()
    val VIEW_TYPE_HEADER = 0
    val VIEW_TYPE_ITEM = 1

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(itemAccount: ItemAccount)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        } else {
            val binding = HeaderBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            HeaderViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is Header -> VIEW_TYPE_HEADER
            is ItemAccount -> VIEW_TYPE_ITEM
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_ITEM -> {
                val itemBinding = holder as ItemViewHolder
                val accountItem = list[position] as ItemAccount
                itemBinding.binding.tvAccountNameAndNumber.text =
                    "${accountItem.account.name} (${accountItem.account.number})"
                itemBinding.binding.tvAccountBalance.text = "$${accountItem.account.balance}"
            }

            VIEW_TYPE_HEADER -> {
                val headerHolder = holder as HeaderViewHolder
                val headerItem = list[position] as Header
                headerHolder.binding.tvHeader.text = headerItem.typeName
            }
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ItemViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val accountItem = list[adapterPosition] as ItemAccount
                mListener.onItemClick(accountItem) }
        }
    }
    inner class HeaderViewHolder(val binding: HeaderBinding) : RecyclerView.ViewHolder(binding.root)
}