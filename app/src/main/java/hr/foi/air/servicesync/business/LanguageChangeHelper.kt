package hr.foi.air.servicesync.business

import android.content.Context
import android.content.SharedPreferences
import hr.foi.air.servicesync.ui.items.LocalesFlags
import java.util.Locale

class LanguageChangeHelper {

    private val PREFS_NAME = "app_preferences"
    private val LANGUAGE_KEY = "selected_language"

    val locales = listOf(
        LocalesFlags("Croatian", "hr", "🇭🇷"),
        LocalesFlags("English", "en", "🇺🇸"),
        LocalesFlags("Spanish", "es", "🇪🇸"),
        LocalesFlags("German", "de", "🇩🇪")
    )

    fun setLanguage(context: Context, languageCode: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(LANGUAGE_KEY, languageCode).apply()
        updateResources(context, languageCode)
    }

    fun getSelectedLanguage(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(LANGUAGE_KEY, "en") ?: "en"
    }

    fun updateResources(context: Context, languageCode: String) {
        val resources = context.resources
        val config = resources.configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun getSelectedLocale(context: Context): LocalesFlags {
        val selectedLanguage = getSelectedLanguage(context)
        return locales.find { it.code == selectedLanguage } ?: locales.first()
    }
}
