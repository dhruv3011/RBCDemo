package com.example.rbcdemo.view.accountDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rbcdemo.R
import com.example.rbcdemo.databinding.ActivityAccountDetailBinding
import com.example.rbcdemo.mvvm.BaseActivity
import com.example.rbcdemo.mvvm.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.rbc.rbcaccountlibrary.Account

class AccountDetailActivity : BaseActivity() {
    var dataBinding: ActivityAccountDetailBinding? = null
    private val accountDetailActivityViewModel: AccountDetailActivityViewModel by viewModels()

    private lateinit var account: Account
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: TransactionDetailListAdapter

    override fun setDataBindingLayout() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_account_detail)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        // set toolbar
        setSupportActionBar(dataBinding?.toolBar)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)

            // show back button on toolbar
            // on back button press, it will navigate to Main Activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        // Fetch account information from Intent
        val itemAccount = intent.getStringExtra("itemAccount")
        val gson = Gson()
        account = gson.fromJson(itemAccount, Account::class.java)
        dataBinding?.account = account

        // Setup Recyclerview
        dataBinding?.rvTransactions?.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        dataBinding?.rvTransactions?.layoutManager = linearLayoutManager
        adapter = TransactionDetailListAdapter()
        // Call API to get transactions
        accountDetailActivityViewModel.getAccountTransactions(account)
    }

    override fun setUpViewModel(): BaseViewModel = accountDetailActivityViewModel

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("ShowToast")
    override fun setupObservers() {
        super.setupObservers()
        // Handle Error Message
        accountDetailActivityViewModel.errorMessage.observe(this) {
            val snackbar = Snackbar.make(dataBinding?.layout!!, it, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY") {
                    accountDetailActivityViewModel.getAccountTransactions(account)
                }
            snackbar.show()
        }
        // Handle Transaction Observer
        accountDetailActivityViewModel.getTransactions().observe(this) {
            if(it.size != 0) {
                dataBinding?.tvNoDataFound?.visibility = View.INVISIBLE
                adapter.list = it
                dataBinding?.rvTransactions?.adapter = adapter
            } else {
                dataBinding?.tvNoDataFound?.visibility = View.VISIBLE
            }
        }
    }
}