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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import hr.foi.air.servicesync.ui.components.isDark

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
        reservations = emptyList()
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
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (error != null) {
            Text(text = "Error: $error", color = isDark(primaryDark, primaryLight))
        } else {
            LazyColumn {
                items(reservations) { reservation ->
                    val companyName = reservation["companyId"] as String
                    val serviceName = reservation["serviceName"] as String
                    val reservationDate = reservation["reservationDate"] as Long
                    Text(
                        text = "$companyName - $serviceName\n${formatDate(reservationDate)}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
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

fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}