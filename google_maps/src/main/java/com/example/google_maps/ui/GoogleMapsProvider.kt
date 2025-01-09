package com.example.google_maps.ui

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

class GoogleMapsProvider : IMapProvider {
    override fun getName(): String {
        return "Google Maps"
    }

    @Composable
    override fun CreateMap(latitude: Double, longitude: Double) {
        val companyCoordinates = LatLng(latitude,longitude)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(companyCoordinates,10f)
        }
        val markerState = rememberMarkerState(position = companyCoordinates)
        GoogleMap(modifier = Modifier.fillMaxSize(),cameraPositionState = cameraPositionState ) {
            Marker(state = markerState, title = "Lokacija tvrte")
        }
    }
}