package com.codingwithmitch.cleannotes.util

import android.util.Log
import com.codingwithmitch.cleannotes.util.Constants.DEBUG
import com.codingwithmitch.cleannotes.util.Constants.TAG
import com.crashlytics.android.Crashlytics

var isUnitTest = false

fun printLogD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}

/*
    Priorities: Log.DEBUG, Log. etc....
 */
fun cLog(priority: Int, tag: String, msg: String?){
    if(!DEBUG){
        Crashlytics.log(priority, tag, msg)
    }
}
