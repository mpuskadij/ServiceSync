package hr.foi.air.servicesync.business

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
class PresentAndFutureSelectableDates : SelectableDates {
    private val now = LocalDate.now()
    private val dayStart = now.atTime(0, 0, 0, 0).toEpochSecond(ZoneOffset.UTC) * 1000
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= dayStart
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= LocalDate.now().year
    }
}