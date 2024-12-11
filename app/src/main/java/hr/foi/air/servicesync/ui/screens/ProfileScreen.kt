package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.compose.onPrimaryDark
import com.example.compose.onPrimaryLight
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import com.example.compose.secondaryDark
import com.example.compose.secondaryLight
import com.example.compose.surfaceDark
import com.example.compose.surfaceLight
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.contents.ProfileContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier, onLogoutClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(
                    top = 0.dp,
                    bottom = 0.dp
                ),
                modifier = Modifier
                    .padding(top = 0.dp),
                title = { Text(text = stringResource(R.string.profile), style = MaterialTheme.typography.titleLarge) },
                actions = {
                    Box(modifier = Modifier.width(100.dp)) {
                        Card(
                            onClick = { onLogoutClick() },
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(
                                containerColor = isDark(primaryDark, primaryLight)
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.logout),
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                color = isDark(onPrimaryDark, onPrimaryLight),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        ProfileContent(modifier = Modifier.padding(innerPadding))
    }
}


@Composable
fun ProfileInfoBox(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = isDark(surfaceDark, surfaceLight)
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = isDark(secondaryDark, secondaryLight),
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = isDark(onSurfaceDark, onSurfaceLight),
                )
            )
        }
    }
}