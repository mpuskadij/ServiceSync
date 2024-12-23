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
    //TODO should check if the name even exists in the list
    override fun saveMapProvider(context: Context, mapProviderName: String) {
        SharedPreferencesManager.saveStringPreference(context,KEY,mapProviderName)
    }

    //TODO this will crash the app if there are no map providers. To remove that possibility, consider checking if there are any providers and if not, dont try to draw the map
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
    //TODO should return null if no map provider exists so that a map will not be drawn
    override fun getCurrentMapProvider(context: Context): IMapProvider {
        return mapProviders.find {mapProvider -> mapProvider.getName() == getCurrentMapProviderName(context) }!!
    }

}