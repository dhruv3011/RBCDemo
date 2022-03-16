package com.example.rbcdemo.view.dashboard

import androidx.lifecycle.MutableLiveData
import com.example.rbcdemo.data.DataItem
import com.example.rbcdemo.data.Header
import com.example.rbcdemo.data.ItemAccount
import com.example.rbcdemo.mvvm.BaseViewModel
import com.rbc.rbcaccountlibrary.AccountProvider
import kotlinx.coroutines.*

class MainActivityViewModel : BaseViewModel() {
    var job1: Job? = null
    val accountListMutableLiveData = MutableLiveData<ArrayList<DataItem>>()
    val errorMessage = MutableLiveData<String>()

    override fun onCreate() {
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private fun onError(message: String) {
        showDialog.value = false
        errorMessage.value = message
    }

    /**
     * Initiating to get All Accounts
     */
    fun getAccountsList() {
        showDialog.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = AccountProvider.getAccountsList()
            val myList = ArrayList<DataItem>()
            when (response) {
                null -> myList.add(Header("Loading"))
                else -> {
                    val groupedList = response.groupBy { it.type.name }
                    for (i in groupedList.keys) {
                        myList.add(Header(i))
                        for (v in groupedList.getValue(i)) {
                            myList.add(ItemAccount(v))
                        }
                    }
                }
            }
            withContext(Dispatchers.Main) {
                accountListMutableLiveData.postValue(myList)
                showDialog.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job1?.cancel()
    }
}