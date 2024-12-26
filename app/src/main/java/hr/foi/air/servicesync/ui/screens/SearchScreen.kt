@file:OptIn(ExperimentalMaterial3Api::class)

package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.contents.SearchContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier, navController: NavController) {

    Scaffold(
        modifier = modifier.padding()
    ) { innerPadding ->
        val adjustedPadding = innerPadding
        Column(
            modifier = modifier
                .windowInsetsPadding(WindowInsets(
                    top = 5.dp,
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

