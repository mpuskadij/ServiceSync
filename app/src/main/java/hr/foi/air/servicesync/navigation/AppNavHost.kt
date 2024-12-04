package hr.foi.air.servicesync.navigation

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import hr.foi.air.servicesync.data.UserSession
import hr.foi.air.servicesync.ui.contents.CompanyDetailsContent
import hr.foi.air.servicesync.ui.screens.CalendarScreen
import hr.foi.air.servicesync.ui.screens.FavoriteScreen
import hr.foi.air.servicesync.ui.screens.LoginScreen
import hr.foi.air.servicesync.ui.screens.MainScreen
import hr.foi.air.servicesync.ui.screens.ProfileScreen
import hr.foi.air.servicesync.ui.screens.RegistrationScreen
import hr.foi.air.servicesync.ui.screens.SearchScreen


fun NavGraphBuilder.AppNavHost(navController: NavHostController) {
    composable("login")
    {
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
    composable("registration")
    {
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
        SearchScreen(modifier = Modifier, navController)
    }

    composable("company/{companyName}") { backStackEntry ->
        val companyName = backStackEntry.arguments?.getString("companyName") ?: "Unknown"
        CompanyDetailsContent(
            navController = navController,
            context = LocalContext.current,
            companyName = companyName
        )
    }
    composable("search")
    {
        SearchScreen(modifier = Modifier, navController)
    }
    composable("calendar")
    {
        CalendarScreen()
    }
    composable("favorites")
    {
        FavoriteScreen()
    }
    composable("profile")
    {
        ProfileScreen(
            onLogoutClick = {
                Log.d("MainScreen", "Logout initiated")
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login")
                {
                    popUpTo("main") { inclusive = true }
                }
            }
        )
    }
}