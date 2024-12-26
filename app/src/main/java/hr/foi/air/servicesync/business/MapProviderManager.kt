package hr.foi.air.servicesync.business

import android.content.Context
import com.example.maps.interfaces.IMapProvider
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.backend.SharedPreferencesManager
import java.util.ServiceLoader

object MapProviderManager : IMapProviderStateManager {
    private const val KEY = "MAP_PROVIDER"
    private val mapProviders by lazy {
        ServiceLoader.load(IMapProvider::class.java).toList()
    }

    private fun getFirstMapProviderOrErrorMessage(context: Context) : String {
        return mapProviders.firstOrNull()?.getName() ?: context.getString(R.string.no_available_maps)
    }

    override fun getAllProviders() : List<IMapProvider> {
        return mapProviders
    }
    //TODO should check if the name even exists in the list
    override fun saveMapProvider(context: Context, mapProviderName: String) {
        SharedPreferencesManager.saveStringPreference(context,KEY,mapProviderName)
    }

    override fun getCurrentMapProviderName(context: Context) : String {
        val storedMapProvider = SharedPreferencesManager.getMapProvider(context, KEY) ?: getFirstMapProviderOrErrorMessage(context)
        val foundProvider = mapProviders.any { provider -> provider.getName() == storedMapProvider }
        return if (foundProvider) {
            storedMapProvider
        } else {
            getFirstMapProviderOrErrorMessage(context)
        }
    }

    override fun getCurrentMapProvider(context: Context): IMapProvider? {
        return mapProviders.find {mapProvider -> mapProvider.getName() == getCurrentMapProviderName(context) }
    }

}