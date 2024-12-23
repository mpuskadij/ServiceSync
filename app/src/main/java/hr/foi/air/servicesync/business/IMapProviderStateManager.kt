package hr.foi.air.servicesync.business

import android.content.Context
import com.example.maps.interfaces.IMapProvider

interface IMapProviderStateManager {
    fun getAllProviders() : List<IMapProvider>

    fun saveMapProvider(context: Context, mapProviderName: String)

    fun getCurrentMapProviderName(context: Context) : String

    fun getCurrentMapProvider(context: Context) : IMapProvider
}