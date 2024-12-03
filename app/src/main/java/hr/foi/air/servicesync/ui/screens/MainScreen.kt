package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.surfaceContainerDark
import com.example.compose.surfaceContainerLight
import com.google.firebase.auth.FirebaseAuth
import hr.foi.air.servicesync.navigation.AppNavHost
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.items.navItems

@Composable
fun MainScreen(
    onLogoutClick: (() -> Unit)? = null
) {
    val user = FirebaseAuth.getInstance().currentUser
    val startDestination = if (user != null) "main" else "login"

    val navController = rememberNavController()
    val navItemList = navItems()

    val (currentRoute, setCurrentRoute) = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setCurrentRoute(destination.route)
        }
    }

    Scaffold(
        containerColor = isDark(onSurfaceLight, onSurfaceDark),
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            if (currentRoute !in listOf("login", "registration")) {
                NavigationBar {
                    navItemList.forEach { navItem ->
                        NavigationBarItem(
                            selected = currentRoute == navItem.route,
                            onClick = {
                                navController.navigate(navItem.route) {
                                    popUpTo("main") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = navItem.icon,
                                    contentDescription = "${navItem.icon}"
                                )
                            },
                            label = { Text(text = navItem.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
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
