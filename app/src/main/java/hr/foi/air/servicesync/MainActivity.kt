package hr.foi.air.servicesync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.AppTheme
import com.google.firebase.auth.FirebaseAuth
import hr.foi.air.servicesync.data.UserSession
import hr.foi.air.servicesync.navigation.AppNavHost
import hr.foi.air.servicesync.ui.components.Greeting
import hr.foi.air.servicesync.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        val startDestination = if (user != null) "main" else "login"

        enableEdgeToEdge()
        setContent {
            AppTheme {
                AppNavHost(startDestination = startDestination)
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
