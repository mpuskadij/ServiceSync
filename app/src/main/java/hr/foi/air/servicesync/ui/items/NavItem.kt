package hr.foi.air.servicesync.ui.items

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label : String,
    val icon: ImageVector,
    val color: Color,
    val badgeCount: Int
)
