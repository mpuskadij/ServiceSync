package hr.foi.air.servicesync.backend

import android.content.Context
import androidx.preference.PreferenceManager

object SharedPreferencesManager {

    fun saveStringPreference(context: Context, key: String, value: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        with(sharedPreferences.edit()) {
            putString(key,value)
            apply()
        }
    }
}