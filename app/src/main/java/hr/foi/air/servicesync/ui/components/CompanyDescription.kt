package hr.foi.air.servicesync.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CompanyDescription(description: String)
{
    Text(text = description , color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyLarge)
}