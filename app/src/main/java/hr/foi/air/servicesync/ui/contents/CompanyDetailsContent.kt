package hr.foi.air.servicesync.ui.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.ui.items.ProvidedServicesListItem

@Composable
fun CompanyDetailsContent(modifier: Modifier, id: Number) {
    Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top, modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Text(text = "Tvrtka A", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.size(50.dp))
        Text(text = "Opis tvrtke A" , color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.size(25.dp))
        ListItem(headlineContent =  {
                Text(text = stringResource(id = R.string.services),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp,)
                )

        }, supportingContent = {
            Column {
                ProvidedServicesListItem("Service A")
                ProvidedServicesListItem("Service B")
            }
        },
        )
        Text(
            text = "Radno vrijeme",

        )
    }
}