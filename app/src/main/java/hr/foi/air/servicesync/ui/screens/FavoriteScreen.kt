package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.contents.FavoriteContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(modifier: Modifier = Modifier, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.favorites), style = MaterialTheme.typography.titleLarge) },
                windowInsets = WindowInsets(
                    top = 0.dp,
                    bottom = 0.dp
                ),
                modifier = Modifier
                    .padding(top = 0.dp)
                )
        },
        modifier = modifier
    ) { innerPadding ->
        FavoriteContent(modifier = Modifier.padding(innerPadding), navController)
    }
    Text(
        text = "",
        color = isDark(primaryDark, primaryLight
    )
    )
}