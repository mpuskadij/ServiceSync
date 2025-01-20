package hr.foi.air.servicesync.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BackButton(onBackPressed : () ->Unit, modifier: Modifier = Modifier, color: Color = Color.White) {
    IconButton(
        onClick = onBackPressed,
        modifier= modifier
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = color
        )
    }
}