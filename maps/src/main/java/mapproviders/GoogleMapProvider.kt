package mapproviders

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.maps.interfaces.IMapProvider
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

class GoogleMapProvider(override val longitude: Double, override val latitude: Double) : IMapProvider {

    private val companyCoordinates = LatLng(latitude,longitude)


    @Composable
    override fun CreateMap() {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(companyCoordinates,10f)
        }
        val markerState = rememberMarkerState(position = companyCoordinates)
        GoogleMap(modifier = Modifier.fillMaxSize(),cameraPositionState = cameraPositionState ) {
            Marker(state = markerState, title = "Lokacija tvrte")
        }
    }

}