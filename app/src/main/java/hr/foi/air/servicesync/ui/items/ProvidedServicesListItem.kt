package hr.foi.air.servicesync.ui.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ProvidedServicesListItem(serviceName: String) {
    Text(text  =serviceName, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyLarge)
    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = Icons.AutoMirrored.Filled.ArrowForward.name)
}