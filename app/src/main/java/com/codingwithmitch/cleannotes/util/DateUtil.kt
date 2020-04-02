package com.codingwithmitch.cleannotes.util

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Singleton
class DateUtil {

    // dates from server look like this: "2019-07-23T03:28:01.406944Z"
    fun convertServerStringDateToLong(sd: String): Long{
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        try {
            val time = sdf.parse(sd).time
            return time
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    fun convertLongToStringDate(longDate: Long): String{
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        try {
            val date = sdf.format(Date(longDate))
            return date
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    // dates format looks like this: "2019-07-23"
    fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(Date())
    }

}














