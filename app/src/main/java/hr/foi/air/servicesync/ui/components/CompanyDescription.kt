package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CompanyDescription(description: String)
{
    Text(modifier = Modifier.padding(10.dp), text = description , color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyLarge)
}