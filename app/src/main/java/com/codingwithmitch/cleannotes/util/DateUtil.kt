package com.codingwithmitch.cleannotes.util

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Singleton
class DateUtil {

    // dates from server look like this: "2019-07-23T03:28:01.406944Z"
    fun convertServerStringDateToLong(sd: String): Long{
        var stringDate = sd.removeRange(sd.indexOf("T") until sd.length)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        try {
            val time = sdf.parse(stringDate).time
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


}