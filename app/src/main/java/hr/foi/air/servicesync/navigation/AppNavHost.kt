package hr.foi.air.servicesync.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hr.foi.air.servicesync.data.UserSession
import hr.foi.air.servicesync.ui.contents.CompanyDetailsContent
import hr.foi.air.servicesync.ui.screens.CalendarScreen
import hr.foi.air.servicesync.ui.screens.FavoriteScreen
import hr.foi.air.servicesync.ui.screens.LoginScreen
import hr.foi.air.servicesync.ui.screens.MainScreen
import hr.foi.air.servicesync.ui.screens.ProfileScreen
import hr.foi.air.servicesync.ui.screens.RegistrationScreen

@Composable
fun AppNavHost(startDestination: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login"){
            LoginScreen(
                onLoginClickSuccesfull = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegistrationClick = {
                    navController.navigate("registration")
                }
            )
        }
        composable("registration") {
            RegistrationScreen(
                onRegisterClickSuccesfull = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        }
        composable("main") {
            MainScreen(
                onLogoutClick = {
                    UserSession.logout()
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}


@Composable
fun MainNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    onLogoutClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "search",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("search") {
            CompanyDetailsContent(modifier = Modifier)
        }
        composable("calendar") {
            CalendarScreen()
        }
        composable("favorites") {
            FavoriteScreen()
        }
        composable("profile") {
            ProfileScreen(onLogoutClick = onLogoutClick)
        }
    }
}

