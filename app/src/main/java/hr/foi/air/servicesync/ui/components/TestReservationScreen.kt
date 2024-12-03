package hr.foi.air.servicesync.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import hr.foi.air.servicesync.ui.screens.ServiceReservationScreen

@Composable
fun TestReservationScreen() {
    var showReservationScreen = remember { mutableStateOf(false) }

    if (showReservationScreen.value) {
        ServiceReservationScreen(serviceName = "resTestService", companyId = "company")
    } else {
        Button(onClick = {
            showReservationScreen.value = true
        }) {
            Text(text = "Open Service Reservation")
        }
    }
}
