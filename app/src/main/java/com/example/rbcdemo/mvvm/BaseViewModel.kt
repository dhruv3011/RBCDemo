package com.example.rbcdemo.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {
    abstract fun onCreate()
    var job: Job? = null

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    val showDialog: MutableLiveData<Boolean> = MutableLiveData()
}