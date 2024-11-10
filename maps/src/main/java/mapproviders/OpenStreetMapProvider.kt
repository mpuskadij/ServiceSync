package mapproviders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.maps.BuildConfig
import com.example.maps.interfaces.IMapProvider
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView

class OpenStreetMapProvider() : IMapProvider {

    @Composable
    override fun CreateMap(latitude  :Double, longitude: Double) {
        val companyCoordinates = LatLng(latitude,longitude)
        val context = LocalContext.current
        val mapView  = remember { MapView(context) }
        AndroidView({mapView}) {view ->
            view.getMapAsync {map ->
                map.cameraPosition = CameraPosition.Builder().target(companyCoordinates).zoom(10.0).build()
            }
        }
    }
}