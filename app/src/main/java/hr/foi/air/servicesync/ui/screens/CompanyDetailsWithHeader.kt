package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.compose.onBackgroundDark
import com.example.compose.onBackgroundLight
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.contents.CompanyDetailsContent
import hr.foi.air.servicesync.ui.contents.formatDate

@Composable
fun CompanyDetailsWithHeader(
    navController: NavHostController,
    companyName: String,
    serviceName: String,
    reservationDate: Long
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
//            Text(
//                text = companyName,
//                style = MaterialTheme.typography.headlineMedium,
//                color = isDark(onBackgroundDark, onBackgroundLight)
//            )
            Text(
                text = serviceName,
                style = MaterialTheme.typography.headlineMedium,
                color = isDark(onBackgroundDark, onBackgroundLight)
            )
            Text(
                text = formatDate(reservationDate),
                style = MaterialTheme.typography.headlineSmall,
                color = isDark(onBackgroundDark, onBackgroundLight)
            )
        }

        CompanyDetailsContent(
            context = LocalContext.current,
            modifier = Modifier.fillMaxHeight(1f),
            navController = navController,
            companyName = companyName
        )
    }
}

