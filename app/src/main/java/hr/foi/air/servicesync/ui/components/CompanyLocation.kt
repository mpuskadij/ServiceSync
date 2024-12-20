package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.GeoPoint

@Composable
fun CompanyLocation(geoPoint: GeoPoint)
{
    Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
    }
}