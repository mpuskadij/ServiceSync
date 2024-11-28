package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.PresentAndFutureSelectableDates
import java.text.DateFormat
import java.util.Date
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceReservationScreen(serviceName: String) {
        Column {
                Text(text = serviceName, style = MaterialTheme.typography.headlineMedium, modifier = Modifier
                        .fillMaxWidth()
                        .padding((8.dp)))


                var showDatePicker by remember { mutableStateOf(false) }

                Spacer(modifier = Modifier.height(25.dp))


                var date by remember { mutableStateOf("") }

                TextField(value = date, onValueChange = {
                        date = it
                }, readOnly = true, enabled = false,  modifier = Modifier
                        .clickable {
                                showDatePicker = true
                        }.fillMaxWidth(),
                        label = {
                                Text(text = stringResource(R.string.select_date))
                        }
                )


                if (showDatePicker) {
                        val selectedDateInMiliseconds : Long
                        val dateFormat = DateFormat.getDateInstance()
                        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

                        if (date.isNotEmpty()) {
                                selectedDateInMiliseconds = dateFormat.parse(date)!!.time
                        }
                        else {
                                selectedDateInMiliseconds = System.currentTimeMillis()
                        }
                        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateInMiliseconds, selectableDates = PresentAndFutureSelectableDates())
                        DatePickerDialog(onDismissRequest = {
                                        showDatePicker = false
                                }, confirmButton = {
                                        TextButton(onClick = {
                                                if (datePickerState.selectedDateMillis != null) {
                                                        val selectedDate = Date(datePickerState.selectedDateMillis!!)
                                                        val formattedDate = dateFormat.format(selectedDate)
                                                        date = formattedDate
                                                }
                                                showDatePicker = false

                                                //TODO add further logic when connecting to firebase

                                        },) {
                                                Text(text = stringResource(R.string.confirm))
                                        }
                                },
                                        dismissButton = {
                                                TextButton(
                                                        onClick = {
                                                                showDatePicker = false

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