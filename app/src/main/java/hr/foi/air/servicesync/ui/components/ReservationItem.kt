package hr.foi.air.servicesync.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.compose.backgroundDark
import com.example.compose.backgroundLight
import com.example.compose.errorLight
import com.example.compose.onErrorDark
import com.example.compose.onErrorLight
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import com.example.compose.surfaceContainerDark
import com.example.compose.surfaceContainerLight
import com.example.compose.tertiaryContainerDark
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.calculateDaysUntilReservation
import hr.foi.air.servicesync.business.formatDate

@Composable
fun ReservationItem(
    companyName: String,
    serviceName: String,
    reservationDate: Long,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val daysUntilReservation = calculateDaysUntilReservation(reservationDate)
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
                Spacer(modifier = Modifier.width(16.dp))
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = daysUntilReservation.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = isDark(primaryDark, primaryLight)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.days),
                        style = MaterialTheme.typography.bodyMedium,
                        color = isDark(primaryDark, primaryLight)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = isDark(backgroundDark, backgroundLight),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete Icon",
                        modifier = Modifier
                            .size(32.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        onDeleteClick()
                                    },
                                    onTap = {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.hold_for_delete),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            },
                        tint = errorLight
                    )
                }

            }
        }
    }
}