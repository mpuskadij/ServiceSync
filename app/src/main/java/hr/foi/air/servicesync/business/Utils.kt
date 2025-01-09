package hr.foi.air.servicesync.business

import java.util.concurrent.TimeUnit

fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd.MM.yyyy @ HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}

fun calculateDaysUntilReservation(reservationDate: Long): Long {
    val currentDate = System.currentTimeMillis()
    val differenceInMillis = reservationDate - currentDate
    return TimeUnit.MILLISECONDS.toDays(differenceInMillis)
}
