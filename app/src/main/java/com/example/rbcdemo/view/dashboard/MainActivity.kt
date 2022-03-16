package com.example.rbcdemo.view.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rbcdemo.R
import com.example.rbcdemo.data.ItemAccount
import com.example.rbcdemo.databinding.ActivityMainBinding
import com.example.rbcdemo.mvvm.BaseActivity
import com.example.rbcdemo.mvvm.BaseViewModel
import com.example.rbcdemo.view.accountDetail.AccountDetailActivity
import com.google.gson.Gson

class MainActivity : BaseActivity() {
    var dataBinding: ActivityMainBinding? = null
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: AccountListAdapter

    override fun setDataBindingLayout() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        dataBinding?.rvAccounts?.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        dataBinding?.rvAccounts?.layoutManager = linearLayoutManager
        adapter = AccountListAdapter()
        mainActivityViewModel.getAccountsList()
        adapter.setOnItemClickListener(object : AccountListAdapter.OnItemClickListener{
            override fun onItemClick(itemAccount: ItemAccount) {
                val intent = Intent(this@MainActivity, AccountDetailActivity::class.java)
                val gson = Gson()
                val accountString = gson.toJson(itemAccount.account)
                intent.putExtra("itemAccount", accountString)
                startActivity(intent)
            }
        })
    }

    override fun setUpViewModel(): BaseViewModel = mainActivityViewModel

    override fun setupObservers() {
        super.setupObservers()

        mainActivityViewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        mainActivityViewModel.accountListMutableLiveData.observe(this) {
            adapter.list = it
            dataBinding?.rvAccounts?.adapter = adapter
        }
    }
}