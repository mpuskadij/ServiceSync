package mapproviders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.maps.interfaces.IMapProvider
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView

class OpenStreetMapProvider(override val longitude: Double, override val latitude: Double) : IMapProvider {
    private val companyCoordinates = LatLng(latitude,longitude)

    @Composable
    override fun CreateMap() {
        val context = LocalContext.current
        val mapView  = remember { MapView(context) }
        AndroidView({mapView}) {view ->
            view.getMapAsync {map ->
                map.cameraPosition = CameraPosition.Builder().target(companyCoordinates).zoom(10.0).build()
            }
        }
    }
}