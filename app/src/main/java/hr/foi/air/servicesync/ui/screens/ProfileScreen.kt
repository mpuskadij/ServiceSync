package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import com.example.compose.secondaryDark
import com.example.compose.secondaryLight
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.contents.ProfileContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier, onLogoutClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profil", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    Box(modifier = Modifier.width(100.dp)) {
                        TextButton(
                            onClick = { onLogoutClick() },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Text(
                                text = "Logout",
                                color = isDark(primaryDark, primaryLight)
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
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
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
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    }
}