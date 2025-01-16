package hr.foi.air.servicesync.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.ui.components.QRScannerContent
import hr.foi.air.servicesync.ui.contents.CompanyDetailsContent
import hr.foi.air.servicesync.ui.screens.AddReviewScreen
import hr.foi.air.servicesync.ui.screens.CalendarScreen
import hr.foi.air.servicesync.ui.screens.CompanyDetailsWithHeader
import hr.foi.air.servicesync.ui.screens.FavoriteScreen
import hr.foi.air.servicesync.ui.screens.LoginScreen
import hr.foi.air.servicesync.ui.screens.ProfileScreen
import hr.foi.air.servicesync.ui.screens.RegistrationScreen
import hr.foi.air.servicesync.ui.screens.SearchScreen
import hr.foi.air.servicesync.ui.screens.ServiceReservationScreen


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
        SearchScreen(modifier = Modifier, navController, onQRCameraClick = {
            navController.navigate("qr_scanner") {
            }
        },)
    }

    composable("company/{companyName}") { backStackEntry ->
        val companyName = backStackEntry.arguments?.getString("companyName") ?: "Unknown"
        CompanyDetailsContent(
            navController = navController,
            context = LocalContext.current,
            companyName = companyName
        )
    }
    composable("company/{companyName}/{serviceName}") {
        backStackEntry ->
        val companyName = backStackEntry.arguments?.getString("companyName") ?: "Unknown"
        val serviceName = backStackEntry.arguments?.getString("serviceName") ?: "Unknown"
        ServiceReservationScreen(serviceName = serviceName, companyName, navController)

    }

    composable(
        "companyDetails/{companyName}/{serviceName}/{reservationDate}",
        arguments = listOf(
            navArgument("companyName") { type = NavType.StringType },
            navArgument("serviceName") { type = NavType.StringType },
            navArgument("reservationDate") { type = NavType.LongType }
        )
    ) { backStackEntry ->
        val companyName = backStackEntry.arguments?.getString("companyName") ?: "Unknown"
        val serviceName = backStackEntry.arguments?.getString("serviceName") ?: "Unknown"
        val reservationDate = backStackEntry.arguments?.getLong("reservationDate") ?: 0L

        CompanyDetailsWithHeader(
            navController = navController,
            companyName = companyName,
            serviceName = serviceName,
            reservationDate = reservationDate
        )
    }


    composable("search")
    {
        SearchScreen(modifier = Modifier, navController, onQRCameraClick = {
                navController.navigate("qr_scanner") {
                }
            }
        )
    }
    composable("calendar")
    {
        CalendarScreen(navController = navController)
    }
    composable("favorites")
    {
        FavoriteScreen(navController = navController)
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
    composable("addReview/{companyId}/{userId}") { backStackEntry ->
        val context = LocalContext.current
        val companyId = backStackEntry.arguments?.getString("companyId") ?: ""
        val userId = backStackEntry.arguments?.getString("userId") ?: ""

        AddReviewScreen(
            navController = navController,
            companyId = companyId,
            userId = userId,
            onReviewSubmit = { success ->
                if (success) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.adding_review),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Failed to add review. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }
    composable("qr_scanner") {
        QRScannerContent(
            onCodeScanned = { code ->
                navController.navigate("company/$code")
            }
        )
    }
}