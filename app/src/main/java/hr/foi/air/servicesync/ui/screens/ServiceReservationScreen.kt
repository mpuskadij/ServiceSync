package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.backend.FirestoreService
import hr.foi.air.servicesync.business.PresentAndFutureSelectableDates
import hr.foi.air.servicesync.business.ReservationManager
import hr.foi.air.servicesync.data.UserSession
import java.text.DateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceReservationScreen(serviceName: String, companyId: String) {
    val reservationManager = remember { ReservationManager(FirestoreService()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var availableSlots by remember { mutableStateOf<List<Long>>(emptyList()) }
    var selectedSlot by remember { mutableStateOf<Long?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = serviceName,
            style = MaterialTheme.typography.headlineMedium,
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
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = System.currentTimeMillis(),
                selectableDates = PresentAndFutureSelectableDates()
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDateMillis = it
                            fetchAvailableSlots(
                                reservationManager,
                                companyId,
                                it
                            ) { slots -> availableSlots = slots }
                        }
                        showDatePicker = false
                    }) {
                        Text(text = stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "slobodni termini",
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn (
            modifier = Modifier.weight(0.5f)
        ) {
            items(availableSlots) { slot ->
                val formattedTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(slot))
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { selectedSlot = slot
                            println("Odabrani slot: $selectedSlot")
                        }
                        .then(
                            if (selectedSlot == slot) Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                            else Modifier
                        )
                )
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
            Text(text = "spremi rezervaciju")
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

@Preview
@Composable
fun PreviewServiceReservationScreen() {
    ServiceReservationScreen("Klasična masaža", "company")
}
