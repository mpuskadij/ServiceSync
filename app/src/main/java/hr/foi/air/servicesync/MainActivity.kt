package hr.foi.air.servicesync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.AppTheme
import hr.foi.air.servicesync.business.LanguageChangeHelper
import hr.foi.air.servicesync.ui.components.Greeting
import hr.foi.air.servicesync.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val languageChangeHelper = LanguageChangeHelper()
        val selectedLanguage = languageChangeHelper.getSelectedLanguage(this)
        languageChangeHelper.updateResources(this, selectedLanguage)

        enableEdgeToEdge()
        setContent {

            AppTheme {
                MainScreen(onLogoutClick = {})
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting("Android")
    }
}

