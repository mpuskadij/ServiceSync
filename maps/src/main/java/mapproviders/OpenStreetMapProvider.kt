package mapproviders
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.maps.interfaces.IMapProvider
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import org.osmdroid.util.GeoPoint


class OpenStreetMapProvider() : IMapProvider {

    @Composable
    override fun CreateMap(latitude: Double, longitude: Double) {
        val markerState  = rememberMarkerState(geoPoint = GeoPoint(latitude,longitude))
        val cameraState = rememberCameraState {
            geoPoint = GeoPoint(latitude, longitude)
            zoom = 10.0
        }

        OpenStreetMap(
            modifier = Modifier.fillMaxSize(),
            cameraState = cameraState,
        ) {
            Marker(
                state = markerState
            )
        }
    }
}