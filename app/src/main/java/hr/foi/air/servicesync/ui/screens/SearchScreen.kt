package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.contents.CompanyDetailsContent
import hr.foi.air.servicesync.ui.contents.SearchContent

@Composable
fun SearchScreen(modifier: Modifier = Modifier, navController: NavController) {

    Scaffold(
        modifier = modifier.padding()
    ) {
        Column(
            modifier = modifier
                .windowInsetsPadding(WindowInsets(
                    top = 30.dp,
                    bottom = 0.dp
                ))
        ) {
            SearchContent(modifier = Modifier, navController)
        }
    }
    Text(
        text = "",
        color = isDark(primaryDark, primaryLight)
    )
}

