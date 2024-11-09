package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.surfaceContainerDark
import com.example.compose.surfaceContainerLight
import hr.foi.air.servicesync.ui.contents.CompanyDetailsContent
import hr.foi.air.servicesync.ui.items.navItems

@Composable
fun MainScreen(
    onLogoutClick: () -> Unit
) {
    val navController = rememberNavController()

    val navItemList = navItems()
    Scaffold(
        containerColor = if (isSystemInDarkTheme()) surfaceContainerDark else surfaceContainerLight,
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar() {
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "search",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("search") {
                CompanyDetailsContent(modifier = Modifier, context = LocalContext.current)
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
}
