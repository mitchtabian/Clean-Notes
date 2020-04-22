package com.codingwithmitch.cleannotes.framework.presentation.common


object TextUtils {

    fun handleTextOverflow(
        cutOffPercent: Int,
        c: Float,
        v: Float,
        originalString: String
    ): String{
        var updatedString = originalString
        val percentageOccupied = (c / v) * 100.0f
        if(percentageOccupied > cutOffPercent){
            val l = originalString.length
            val xp = v * cutOffPercent / 100
            val x: Int = (l - l * (c - xp) / c).toInt()
            updatedString = updatedString.substring(0, x) + "..."
        }
        return updatedString
    }
}








