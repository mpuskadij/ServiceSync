package com.example.maps.interfaces

import androidx.compose.runtime.Composable

interface IMapProvider {

    fun getName() : String

    @Composable
    fun CreateMap(latitude: Double, longitude: Double)

}