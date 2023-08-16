package co.mbznetwork.gamesforyou.util

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    fun formatDate(date: Date, format: String): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }

    fun formatDate(date: Date): String {
        return SimpleDateFormat.getDateInstance().format(date)
    }

    fun formatDate(millis: Long) = formatDate(Date(millis))

}
