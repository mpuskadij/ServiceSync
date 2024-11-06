package hr.foi.air.servicesync.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import hr.foi.air.servicesync.ui.screens.LoginScreen
import hr.foi.air.servicesync.ui.screens.MainScreen
import hr.foi.air.servicesync.ui.screens.RegistrationScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login"){
            LoginScreen(
                onLoginClickSuccesfull = {
                    navController.navigate("main")
                },
                onRegistrationClick = {
                    navController.navigate("registration")
                }
            )
        }
        composable("registration"){
            RegistrationScreen(
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        }
        composable("main") {
            MainScreen()
        }
    }
}
