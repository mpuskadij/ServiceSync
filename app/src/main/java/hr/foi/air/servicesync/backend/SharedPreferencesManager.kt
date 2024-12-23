package hr.foi.air.servicesync.backend

import android.content.Context
import androidx.preference.PreferenceManager

object SharedPreferencesManager : IChosenMapProviderSaver {

    fun saveStringPreference(context: Context, key: String, value: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        with(sharedPreferences.edit()) {
            putString(key,value)
            apply()
        }
    }

    fun getStringPreference(context: Context,key: String) : String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        return sharedPreferences.getString(key,null)
    }

    override fun saveMapProvider(context: Context, key: String, value: String) {
        saveStringPreference(context,key,value)
    }

    override fun getMapProvider(context: Context,key: String) : String? {
        return getStringPreference(context,key)
    }
}