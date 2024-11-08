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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.compose.errorDark
import com.example.compose.errorLight
import com.example.compose.primaryDarkHighContrast
import com.example.compose.primaryLightHighContrast
import com.example.compose.surfaceContainerDark
import com.example.compose.surfaceContainerLight
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.items.NavItem

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit
) {

    val navItemList = navItems()

    var selectedIndex by remember {
        mutableStateOf(0)
    }

    Scaffold(
        containerColor = if (isSystemInDarkTheme()) surfaceContainerDark else surfaceContainerLight,
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar() {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            BadgedBox(badge = {
                                if (navItem.badgeCount > 0)
                                {
                                    Badge(containerColor = isDark(errorDark, errorLight))
                                    {
                                        Text(text = navItem.badgeCount.toString(), color = Color.White)
                                    }

                                }
                            }) {
                                Icon(
                                    imageVector = navItem.icon,
                                    contentDescription = "${navItem.icon}"
                                )
                            }
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex, onLogoutClick = onLogoutClick)
    }
}

@Composable
private fun navItems(): List<NavItem> {
    val navItemList = listOf(
        NavItem(
            "Pretraži",
            Icons.Default.Search,
            isDark(primaryDarkHighContrast, primaryLightHighContrast),
            badgeCount = 0
        ),
        NavItem(
            "Kalendar",
            Icons.Default.DateRange,
            isDark(primaryDarkHighContrast, primaryLightHighContrast),
            badgeCount = 0
        ),
        NavItem(
            "Favoriti",
            Icons.Default.FavoriteBorder,
            isDark(primaryDarkHighContrast, primaryLightHighContrast),
            badgeCount = 0
        ),
        NavItem(
            "Profil",
            Icons.Default.AccountCircle,
            isDark(primaryDarkHighContrast, primaryLightHighContrast),
            badgeCount = 0
        ),
    )
    return navItemList
}


