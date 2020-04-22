package com.codingwithmitch.cleannotes.util

import android.util.Log
import com.codingwithmitch.cleannotes.util.Constants.DEBUG
import com.codingwithmitch.cleannotes.util.Constants.TAG

fun printLogD(className: String?, message: String ) {
    if (DEBUG) {
        Log.d(TAG, "$className: $message")
    }
}
