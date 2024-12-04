package hr.foi.air.servicesync.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProvidedServicesListItem(serviceName: String, onServiceClicked: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable {

        onServiceClicked
    }, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text  =serviceName, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyLarge)

        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = Icons.AutoMirrored.Filled.ArrowForward.name,

        )
    }
}