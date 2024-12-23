package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.GeoPoint
import hr.foi.air.servicesync.business.MapProviderManager

@Composable
fun CompanyLocation(geoPoint: GeoPoint)
{
    Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
        MapProviderManager.getCurrentMapProvider(context = LocalContext.current).CreateMap(geoPoint.latitude,geoPoint.longitude)
    }
}