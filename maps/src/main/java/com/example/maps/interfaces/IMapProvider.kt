package com.example.maps.interfaces

import androidx.compose.runtime.Composable

interface IMapProvider {
    val longitude:  Double
    val latitude: Double

    @Composable
    fun CreateMap() {

    }

}