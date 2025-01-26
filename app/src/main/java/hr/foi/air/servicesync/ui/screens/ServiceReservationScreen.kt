package hr.foi.air.servicesync.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.surfaceDimLightHighContrast
import com.example.compose.surfaceVariantDark
import com.example.compose.surfaceVariantLight
import com.example.compose.tertiaryDark
import com.example.compose.tertiaryLight
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.backend.FirestoreService
import hr.foi.air.servicesync.business.PresentAndFutureSelectableDates
import hr.foi.air.servicesync.business.ReservationManager
import hr.foi.air.servicesync.data.UserSession
import hr.foi.air.servicesync.ui.components.BackButton
import hr.foi.air.servicesync.ui.components.ReservationCalendar
import hr.foi.air.servicesync.ui.components.isDark
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceReservationScreen(serviceName: String, companyId: String, navController: NavController) {
    val reservationManager = remember { ReservationManager(FirestoreService()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var availableSlots by remember { mutableStateOf<List<Long>>(emptyList()) }
    var selectedSlot by remember { mutableStateOf<Long?>(null) }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        BackButton(onBackPressed = { navController.popBackStack()}, color = isDark(onSurfaceDark, onSurfaceLight))
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
                onConfirm = { selectedDate ->
                    selectedDate?.let { dateMillis ->
                        selectedDateMillis = dateMillis
                        reservationManager.fetchAvailableSlots(
                            companyId,
                            serviceName,
                            dateMillis
                        ) { slots ->
                            val currentTime = System.currentTimeMillis()
                            availableSlots = slots.filter { it > currentTime }
                            if (availableSlots.isEmpty()) {
                                Toast.makeText(context, context.getString(R.string.no_slots), Toast.LENGTH_SHORT).show()
                            }
                        }
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
                            tertiaryLight, tertiaryDark)
                        else isDark(surfaceVariantDark, surfaceDimLightHighContrast)
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
                            println("Spremanje rezervacije: companyId=$companyId, serviceName=$serviceName, reservationDate=$selectedSlot, userId=$userId")
                            availableSlots = availableSlots.filter { it != selectedSlot }
                            Toast.makeText(context, context.getString(R.string.reservation_successful), Toast.LENGTH_SHORT).show()
                            selectedSlot = null
                            navController.popBackStack()
                        },
                        onFailure = { exception ->
                            println("Error saving reservation: ${exception.message}")
                            Toast.makeText(context, context.getString(R.string.reservation_error), Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            },
            enabled = selectedSlot != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_reservation))
        }
    }
}



