package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.foi.air.servicesync.R

@Composable
fun CompanyLocation()
{
    val headlineModifier = Modifier.fillMaxWidth().padding(8.dp)
    val headlineTextStyle =  MaterialTheme.typography.headlineMedium
    Text(text = stringResource(R.string.location), modifier = headlineModifier, style = headlineTextStyle)

    //TODO remove this box and add GoogleMaps map
    Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.Black))
}