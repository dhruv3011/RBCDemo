package com.example.rbcdemo.mvvm

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.rbcdemo.utils.LoadingDialog

abstract class BaseActivity : AppCompatActivity() {
    lateinit var viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = setUpViewModel()
        setDataBindingLayout()
        setupObservers()
        setupView(savedInstanceState)
        viewModel.onCreate()
    }

    protected open fun setupObservers() {
        viewModel.showDialog.observe(this, Observer {
            showLoadingDialog(it)
        })
    }

    private fun showLoadingDialog(it: Boolean) {
        if (it) {
            LoadingDialog.showDialog()
        } else {
            LoadingDialog.dismissDialog()
        }
    }

    private fun showMessage(message: String) = show(applicationContext, message)

    private fun show(context: Context, text: CharSequence) {
        val toast = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    protected abstract fun setDataBindingLayout()

    protected abstract fun setupView(savedInstanceState: Bundle?)

    protected abstract fun setUpViewModel(): BaseViewModel
}