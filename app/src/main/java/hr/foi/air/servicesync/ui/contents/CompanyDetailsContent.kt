package hr.foi.air.servicesync.ui.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.ui.items.ProvidedServicesListItem

@Composable
fun CompanyDetailsContent(id: Number) {
    Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {
        Text(text = "Company A", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.displayMedium)
        Text(text = "Description of company A" , color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyLarge)
        ListItem(headlineContent =  {
            Text(text = stringResource(id = R.string.services), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.headlineMedium)
        }, leadingContent = {
            ProvidedServicesListItem("Service A")
            ProvidedServicesListItem("Service B")
        }
        )
    }
}