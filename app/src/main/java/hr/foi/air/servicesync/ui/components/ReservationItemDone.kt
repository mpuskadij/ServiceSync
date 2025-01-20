package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.onPrimaryContainerDark
import com.example.compose.onPrimaryContainerLight
import com.example.compose.onPrimaryDark
import com.example.compose.onPrimaryLight
import com.example.compose.onSecondaryDark
import com.example.compose.onSecondaryLight
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.onSurfaceVariantDark
import com.example.compose.onSurfaceVariantLight
import com.example.compose.surfaceContainerDark
import com.example.compose.surfaceContainerLight
import com.example.compose.surfaceContainerLowDark
import com.example.compose.surfaceContainerLowLight
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.formatDate
import hr.foi.air.servicesync.data.UserSession

@Composable
fun ReservationItemDone(
    companyName: String,
    serviceName: String,
    buttonEnabled: Boolean = true,
    navController: NavController,
    reservationDate: Long,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
                    color = isDark(onSurfaceVariantDark, onSurfaceVariantLight)
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
                Button(
                    onClick = {
                        navController.navigate("addReview/$companyName/${UserSession.username}")
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = isDark(onPrimaryContainerDark, onPrimaryContainerLight)
                    ),
                    enabled = buttonEnabled
                ) {
                    Text(
                        text = if (buttonEnabled)
                        stringResource(R.string.leave_review) else stringResource(
                        R.string.review_submited),
                        color = isDark(onPrimaryDark, onPrimaryLight)
                    )
                }
            }
        }
    }
}
