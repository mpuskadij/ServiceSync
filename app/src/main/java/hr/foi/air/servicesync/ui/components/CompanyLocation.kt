package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.GeoPoint
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.MapProviderManager

@Composable
fun CompanyLocation(geoPoint: GeoPoint)
{
    val mapProvider = MapProviderManager.getCurrentMapProvider(context = LocalContext.current)
    if (mapProvider != null) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clipToBounds()
        ) {
            mapProvider.CreateMap(geoPoint.latitude,geoPoint.longitude)
        }
    }
    else {
        Text(text = stringResource(R.string.map_not_available))
    }

}