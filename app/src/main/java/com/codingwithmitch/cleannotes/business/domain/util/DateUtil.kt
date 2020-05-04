package com.codingwithmitch.cleannotes.business.domain.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Singleton
class DateUtil
constructor(
    private val dateFormat: SimpleDateFormat
)
{

    // dates from server look like this: "2019-07-23 HH:mm:ss"
    fun convertServerStringDateToLong(sd: String): Long{
        try {
            val time = dateFormat.parse(sd).time
            return time
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    fun convertLongToStringDate(longDate: Long): String{
        try {
            val date = dateFormat.format(Date(longDate))
            return date
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    fun removeTimeFromDateString(sd: String): String{
        return sd.substring(0, sd.indexOf(" "))
    }

    fun convertFirebaseTimestampToStringData(timestamp: Timestamp): String{
        return dateFormat.format(timestamp.toDate())
    }

    fun convertLongDateToFirebaseTimestamp(date: Long): Timestamp{
        return Timestamp(date/1000, 0)
    }

    fun convertStringDateToFirebaseTimestamp(date: String): Timestamp{
        return Timestamp(
            convertServerStringDateToLong(date) / 1000, // convert to seconds from ms
            0
        )
    }

    // dates format looks like this: "2019-07-23 HH:mm:ss"
    fun getCurrentTimestamp(): String {
        return dateFormat.format(Date())
    }

}














