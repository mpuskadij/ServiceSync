package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.compose.surfaceContainerDark
import com.example.compose.surfaceContainerLight
import hr.foi.air.servicesync.navigation.MainNavHost
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
        MainNavHost(navController, innerPadding, onLogoutClick)
    }
}
