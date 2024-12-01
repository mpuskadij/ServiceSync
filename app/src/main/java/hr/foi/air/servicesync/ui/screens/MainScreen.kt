package hr.foi.air.servicesync.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.surfaceContainerDark
import com.example.compose.surfaceContainerLight
import com.google.firebase.auth.FirebaseAuth
import hr.foi.air.servicesync.navigation.AppNavHost
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.contents.CompanyDetailsContent
import hr.foi.air.servicesync.ui.contents.SearchContent
import hr.foi.air.servicesync.ui.items.navItems

@Composable
fun MainScreen(
    onLogoutClick: (() -> Unit)? = null
) {
    val user = FirebaseAuth.getInstance().currentUser
    val startDestination = if (user != null) "main" else "login"

    val navController = rememberNavController()
    val navItemList = navItems()
    Scaffold(
        containerColor = isDark(onSurfaceLight, onSurfaceDark),
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom).asPaddingValues()),
        bottomBar = {
            NavigationBar{
                navItemList.forEach { navItem ->
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            navController.navigate(navItem.route)
                        },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = "${navItem.icon}")
                        },
                        label = { Text(text = navItem.label) }
                    )
                }
            }
        }
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .padding(innerPadding)
                .background(isDark(surfaceContainerDark, surfaceContainerLight))
        ) {
            AppNavHost(navController)
        }
    }
}
