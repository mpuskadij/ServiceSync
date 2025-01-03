package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import com.example.compose.surfaceContainerDark
import com.example.compose.surfaceContainerLight
import hr.foi.air.servicesync.R
import java.util.concurrent.TimeUnit

@Composable
fun ReservationItem(
    companyName: String,
    serviceName: String,
    reservationDate: Long,
    onClick: () -> Unit
) {
    val daysUntilReservation = calculateDaysUntilReservation(reservationDate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            //.background(isDark(surfaceDark, surfaceLight))
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = isDark(surfaceContainerDark, surfaceContainerLight)
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = serviceName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = isDark(primaryDark, primaryLight)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formatDate(reservationDate),
                    style = MaterialTheme.typography.bodyMedium,
                    color = isDark(onSurfaceDark, onSurfaceLight)
                )
            }
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_access_time_filled_24),
                    contentDescription = "Clock Icon",
                    modifier = Modifier.size(24.dp),
                    tint = isDark(primaryDark, primaryLight)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = daysUntilReservation.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = isDark(primaryDark, primaryLight)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.days),
                        style = MaterialTheme.typography.bodyMedium,
                        color = isDark(primaryDark, primaryLight)
                    )
                }
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}

fun calculateDaysUntilReservation(reservationDate: Long): Long {
    val currentDate = System.currentTimeMillis()
    val differenceInMillis = reservationDate - currentDate
    return TimeUnit.MILLISECONDS.toDays(differenceInMillis)
}