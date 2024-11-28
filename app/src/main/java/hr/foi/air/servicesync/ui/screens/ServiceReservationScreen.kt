package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.foi.air.servicesync.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceReservationScreen(serviceName: String) {
        Text(text = serviceName, style = MaterialTheme.typography.headlineMedium, modifier = Modifier
                .fillMaxWidth()
                .padding((8.dp)))

        var date = remember { mutableStateOf("") }
        TextField(value = date.value, modifier = Modifier.fillMaxWidth(), onValueChange = {
                date.value = it
        }, readOnly = true
        )

        var showDatePicker = remember { mutableStateOf(true) }

        if (showDatePicker.value) {
                Box(modifier = Modifier.fillMaxWidth()) {
                        val currentDateMillis = System.currentTimeMillis()

                        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = currentDateMillis)
                        DatePickerDialog(onDismissRequest = {
                                showDatePicker.value = false
                        }, confirmButton = {
                                TextButton(onClick = {
                                        val dateFormat = DateFormat.getDateInstance()
                                        if (datePickerState.selectedDateMillis != null) {
                                                val selectedDate = Date(datePickerState.selectedDateMillis!!)
                                                val formattedDate = dateFormat.format(selectedDate)
                                                date.value = formattedDate
                                        }
                                        showDatePicker.value = false

                                },) {
                                        Text(text = stringResource(R.string.confirm))
                                }
                        },
                                dismissButton = {
                                        TextButton(
                                                onClick = {
                                                        showDatePicker.value = false

                                                }) {
                                                Text(text = stringResource(R.string.cancel))
                                        }
                                }
                        ) {
                                DatePicker(state = datePickerState)
                        }

                }
        }



}

@Preview
@Composable
fun PreviewServiceReservationScreen() {
        ServiceReservationScreen("Klasična masaža")
}