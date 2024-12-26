package hr.foi.air.servicesync.business

import android.content.Context
import android.util.Log
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

    override fun saveMapProvider(context: Context, mapProviderName: String) {
        val mapProviderExists = mapProviders.any { mp -> mp.getName() == mapProviderName }
        if (mapProviderExists) {
            SharedPreferencesManager.saveStringPreference(context,KEY,mapProviderName)
        }
        else {
            Log.e("NONEXISTANT_MAP_PROVIDER", "Korisnik je pokušao spremiti karte koje ne postoje u aplikaciji trenutno!")
        }
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