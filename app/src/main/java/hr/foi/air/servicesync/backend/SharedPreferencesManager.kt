package hr.foi.air.servicesync.backend

import android.content.Context
import androidx.preference.PreferenceManager

object SharedPreferencesManager {

    fun savePreference(context: Context, preference : Pair<String,String>) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        with(sharedPreferences.edit()) {
            putString(preference.first,preference.second)
            apply()
        }
    }
}