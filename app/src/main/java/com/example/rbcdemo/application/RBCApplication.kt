package com.example.rbcdemo.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.rbcdemo.utils.LoadingDialog

class RBCApplication : Application(), Application.ActivityLifecycleCallbacks {


    override fun onCreate() {
        super.onCreate()
        /*Register Activity Lifecycle callbacks*/
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        LoadingDialog.bindWith(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        LoadingDialog.unbind()
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}