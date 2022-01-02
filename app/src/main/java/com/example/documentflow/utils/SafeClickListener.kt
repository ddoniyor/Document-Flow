package com.example.documentflow.utils

import android.os.SystemClock
import android.view.View

abstract class SafeClickListener @JvmOverloads constructor(private var defaultInterval: Int = 1000) :
    View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }

    abstract fun onSafeCLick(v: View?)
}
