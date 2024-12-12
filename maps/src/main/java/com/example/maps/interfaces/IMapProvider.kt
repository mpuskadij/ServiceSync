package com.example.maps.interfaces

import androidx.compose.runtime.Composable

interface IMapProvider {

    @Composable
    fun CreateMap(latitude: Double, longitude: Double)

}