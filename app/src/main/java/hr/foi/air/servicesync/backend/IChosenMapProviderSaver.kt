package hr.foi.air.servicesync.backend

import android.content.Context

interface IChosenMapProviderSaver {
    fun saveMapProvider(context: Context,key: String, value: String)

    fun getMapProvider(context: Context, key: String) : String?
}