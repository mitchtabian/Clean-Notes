package com.codingwithmitch.cleannotes.core.util

import android.util.Log
import com.codingwithmitch.cleannotes.core.util.Constants.DEBUG
import com.codingwithmitch.cleannotes.core.util.Constants.TAG

fun printLogD(className: String?, message: String ) {
    if (DEBUG) {
        Log.d(TAG, "$className: $message")
    }
}
