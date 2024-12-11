package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.foi.air.servicesync.R

@Composable
fun CompanyWorkingHours()
{
    val headlineTextStyle =  MaterialTheme.typography.headlineMedium
    val headlineModifier = Modifier.fillMaxWidth().padding(8.dp)

    Text(
        text = stringResource(id = R.string.working_hours),
        color = MaterialTheme.colorScheme.onSurface,
        style =headlineTextStyle,
        modifier = headlineModifier
    )

    Text(stringResource(R.string.mon_fri),modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp))
    Text(stringResource(R.string.saturday),modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp))

}