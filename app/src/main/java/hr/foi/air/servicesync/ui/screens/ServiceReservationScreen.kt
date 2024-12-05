package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.backgroundDark
import com.example.compose.backgroundLight
import com.example.compose.inversePrimaryDarkMediumContrast
import com.example.compose.inversePrimaryLightMediumContrast
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.outlineVariantDark
import com.example.compose.outlineVariantLight
import com.example.compose.secondaryContainerDarkMediumContrast
import com.example.compose.secondaryContainerLightMediumContrast
import com.example.compose.surfaceContainerDark
import com.example.compose.surfaceContainerLight
import com.example.compose.surfaceVariantDark
import com.example.compose.surfaceVariantLight
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.backend.FirestoreService
import hr.foi.air.servicesync.business.PresentAndFutureSelectableDates
import hr.foi.air.servicesync.business.ReservationManager
import hr.foi.air.servicesync.data.UserSession
import hr.foi.air.servicesync.ui.components.BackButton
import hr.foi.air.servicesync.ui.components.ReservationCalendar
import hr.foi.air.servicesync.ui.components.isDark
import java.text.DateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceReservationScreen(serviceName: String, companyId: String, navController: NavController) {
    val reservationManager = remember { ReservationManager(FirestoreService()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var availableSlots by remember { mutableStateOf<List<Long>>(emptyList()) }
    var selectedSlot by remember { mutableStateOf<Long?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        BackButton(onBackPressed = { navController.popBackStack()})
        Text(
            text = serviceName,
            style = MaterialTheme.typography.headlineMedium,
            color = isDark(surfaceVariantLight, surfaceVariantDark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        TextField(
            value = selectedDateMillis?.let { DateFormat.getDateInstance().format(Date(it)) } ?: "",
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .clickable { showDatePicker = true }
                .fillMaxWidth(),
            label = { Text(text = stringResource(R.string.select_date)) }
        )

        if (showDatePicker) {
            ReservationCalendar(
                selectedDates = PresentAndFutureSelectableDates(),
                onDismiss = {
                    showDatePicker = false
                },
                onConfirm = {selectedDate ->
                    selectedDate?.let {
                        selectedDateMillis = it
                        fetchAvailableSlots(
                            reservationManager,
                            companyId,
                            it
                        ) { slots -> availableSlots = slots }
                    }
                    showDatePicker = false
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.available_appointments),
            color = isDark(surfaceVariantLight, surfaceVariantDark),
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(availableSlots) { slot ->
                val formattedTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(slot))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            selectedSlot = slot
                            println("Odabrani slot: $selectedSlot")
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedSlot == slot) isDark(
                            inversePrimaryDarkMediumContrast, inversePrimaryLightMediumContrast)
                        else isDark(surfaceVariantDark, surfaceVariantLight)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (selectedSlot == slot) 4.dp else 2.dp
                    )
                ) {
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selectedSlot == slot) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                println("Spremanje rezervacije, odabrani slot: $selectedSlot")
                if (selectedSlot != null) {
                    val userId = UserSession.username
                    reservationManager.saveReservation(
                        companyId = companyId,
                        serviceName = serviceName,
                        reservationDate = selectedSlot!!,
                        userId = userId,
                        onSuccess = {
                            println("Reservation saved successfully.")
                            availableSlots = availableSlots.filter { it != selectedSlot }
                            selectedSlot = null
                        },
                        onFailure = { exception ->
                            println("Error saving reservation: ${exception.message}")
                        }
                    )
                }
            },
            enabled = selectedSlot != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Spremi rezervaciju")
        }
    }
}

private fun fetchAvailableSlots(
    reservationManager: ReservationManager,
    companyId: String,
    dateMillis: Long,
    onSlotsFetched: (List<Long>) -> Unit
) {
    reservationManager.getAvailableTimeSlots(
        companyId = companyId,
        date = dateMillis,
        onSuccess = { slots -> onSlotsFetched(slots) },
        onFailure = { exception ->
            println("Error fetching available slots: ${exception.message}")
        }
    )
}

