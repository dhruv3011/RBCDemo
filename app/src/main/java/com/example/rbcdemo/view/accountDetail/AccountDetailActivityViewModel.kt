package com.example.rbcdemo.view.accountDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rbcdemo.data.ItemTransaction
import com.example.rbcdemo.data.TransactionDataItem
import com.example.rbcdemo.data.TransactionHeader
import com.example.rbcdemo.mvvm.BaseViewModel
import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountProvider
import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import kotlinx.coroutines.*
import java.text.SimpleDateFormat

class AccountDetailActivityViewModel : BaseViewModel() {
    var job1: Job? = null
    val errorMessage = MutableLiveData<String>()

    override fun onCreate() {
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Error: ${throwable.localizedMessage}")
    }

    private fun onError(message: String) {
        showDialog.postValue(false)
        errorMessage.postValue(message)
    }

    private val transactionsLiveData = MutableLiveData<ArrayList<TransactionDataItem>>()

    fun getTransactions(): LiveData<ArrayList<TransactionDataItem>> {
        return transactionsLiveData
    }

    /**
     * Initiating to get Account Transactions
     */
    fun getAccountTransactions(account: Account) {
        showDialog.value = true
        job1 = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val allTransactions = mutableListOf<Transaction>()
                val transactions =
                    async(Dispatchers.IO) {
                        AccountProvider.getTransactions(account.number)
                        //println("getTransaction")
                    }
                if (account.type == AccountType.CREDIT_CARD) {
                    val creditCardTransaction =
                        async(Dispatchers.IO) {
                            AccountProvider.getAdditionalCreditCardTransactions(account.number)
                            //println("CREDIT")
                        }
                    val creditCardResult = creditCardTransaction.await()
                    allTransactions.addAll(creditCardResult)
                }
                val transactionResult = transactions.await()
                allTransactions.addAll(transactionResult)

                val format1 = SimpleDateFormat("yyyy-MM-dd")
                val myList = ArrayList<TransactionDataItem>()
                when (allTransactions) {
                    null -> myList.add(TransactionHeader("Loading"))
                    else -> {
                        val groupedList = allTransactions.groupBy { format1.format(it.date.time) }
                        for (i in groupedList.keys) {
                            myList.add(TransactionHeader(i))
                            for (v in groupedList.getValue(i)) {
                                myList.add(ItemTransaction(v))
                            }
                        }
                    }
                }
                Log.e("List Size", "" + myList.size);
                transactionsLiveData.postValue(myList)
                showDialog.postValue(false)
            } catch (e: Exception) {
                Log.e("Error", "" + e.message)
                showDialog.postValue(false)
                //errorMessage.postValue(e.message)
            }
        }
    }
}