package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.surfaceContainerDark
import com.example.compose.surfaceContainerLight
import hr.foi.air.servicesync.ui.items.NavItem

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
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            navController.navigate(navItem.route)
                        },
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = "${navItem.icon}"
                            )
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "search", modifier = Modifier.padding(innerPadding)) {
            composable("search") {
                SearchScreen()
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
            composable("company_details") {

            }
        }
    }
}

@Composable
private fun navItems(): List<NavItem> {
    return listOf(
        NavItem("Pretraži", Icons.Default.Search, route = "search"),
        NavItem("Kalendar", Icons.Default.DateRange, route = "calendar"),
        NavItem("Favoriti", Icons.Default.FavoriteBorder, route = "favorites"),
        NavItem("Profil", Icons.Default.AccountCircle, route = "profile")
    )
}
