package hr.foi.air.servicesync.ui.contents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.foi.air.servicesync.backend.FirestoreService
import hr.foi.air.servicesync.business.ReservationManager
import hr.foi.air.servicesync.data.UserSession
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.compose.errorDark
import com.example.compose.errorLight
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import com.example.compose.surfaceContainerDark
import com.example.compose.surfaceContainerLight
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.ui.components.isDark
import kotlinx.coroutines.delay

@Composable
fun CalendarContent(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val reservationManager = remember { ReservationManager(FirestoreService()) }
    val userId = UserSession.username
    var reservations by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = Unit) {
        //reservations = emptyList()
        reservationManager.fetchUserReservations(
            userId = userId,
            onReservationsFetched = { reservations = it },
            onFailure = { error = it.message }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (error != null) {
            Text(
                text = "Error: $error",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
        } else if (reservations.isEmpty()) {
            var showNoAppointmentsMessage by remember { mutableStateOf(false) }

            LaunchedEffect(reservations) {
                delay(1000L)
                if (reservations.isEmpty()) {
                    showNoAppointmentsMessage = true
                }
            }

            if (showNoAppointmentsMessage) {
                Text(
                    text = stringResource(R.string.no_future_appointments),
                    style = MaterialTheme.typography.bodyLarge,
                    color = isDark(errorDark, errorLight),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }else {
            Text(
                text = stringResource(R.string.future_appointments),
                style = MaterialTheme.typography.headlineSmall,
                color = isDark(primaryDark, primaryLight),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(reservations) { reservation ->
                    val companyName = reservation["companyId"] as String
                    val serviceName = reservation["serviceName"] as String
                    val reservationDate = reservation["reservationDate"] as Long
                    ReservationItem(
                        companyName = companyName,
                        serviceName = serviceName,
                        reservationDate = reservationDate,
                        onClick = {
                            navController.navigate(
                                "companyDetails/$companyName/$serviceName/$reservationDate"
                            ) {
                                popUpTo("calendar") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ReservationItem(
    companyName: String,
    serviceName: String,
    reservationDate: Long,
    onClick: () -> Unit
) {
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatDate(reservationDate),
                    style = MaterialTheme.typography.bodyMedium,
                    color = isDark(onSurfaceDark, onSurfaceLight)
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.baseline_access_time_filled_24),
                contentDescription = "Clock Icon",
                modifier = Modifier.size(24.dp),
                tint = isDark(primaryDark, primaryLight)
            )
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}