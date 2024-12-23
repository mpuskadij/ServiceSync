package hr.foi.air.servicesync.business

import android.content.Context
import com.example.maps.interfaces.IMapProvider
import hr.foi.air.servicesync.backend.SharedPreferencesManager
import java.util.ServiceLoader

object MapProviderManager : IMapProviderStateManager {
    private const val KEY = "MAP_PROVIDER"
    private val mapProviders by lazy {
        ServiceLoader.load(IMapProvider::class.java).toList()
    }

    override fun getAllProviders() : List<IMapProvider> {
        return mapProviders
    }

    override fun saveMapProvider(context: Context, mapProviderName: String) {
        SharedPreferencesManager.saveStringPreference(context,KEY,mapProviderName)
    }

    override fun getCurrentMapProviderName(context: Context) : String {
        val storedMapProvider = SharedPreferencesManager.getMapProvider(context, KEY) ?: mapProviders.first().getName()
        val foundProvider = mapProviders.any { provider -> provider.getName() == storedMapProvider }
        if (foundProvider) {
            return storedMapProvider
        }
        else {
            return mapProviders.first().getName()
        }
    }

    override fun getCurrentMapProvider(context: Context): IMapProvider {
        return mapProviders.find {mapProvider -> mapProvider.getName() == getCurrentMapProviderName(context) }!!
    }

}