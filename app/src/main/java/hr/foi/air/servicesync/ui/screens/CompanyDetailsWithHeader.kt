package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.compose.onBackgroundDark
import com.example.compose.onBackgroundLight
import com.example.compose.tertiaryContainerDark
import com.example.compose.tertiaryContainerLight
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.formatDate
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.contents.CompanyDetailsContent

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
                .clip(RoundedCornerShape(16.dp))
                .background(isDark(tertiaryContainerDark, tertiaryContainerLight)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f).padding( 16.dp, 0.dp)
                ) {
                    Text(
                        text = serviceName,
                        style = MaterialTheme.typography.titleMedium,
                        color = isDark(onBackgroundDark, onBackgroundLight),
                    )
                    Text(
                        text = formatDate(reservationDate),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = isDark(onBackgroundDark, onBackgroundLight)
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.baseline_access_time_filled_24),
                    contentDescription = "Clock Icon",
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(6.dp)
                        .aspectRatio(1f)
                        .align(Alignment.CenterVertically),
                    tint = isDark(onBackgroundDark, onBackgroundLight)
                )
            }
        }

        CompanyDetailsContent(
            context = LocalContext.current,
            modifier = Modifier.fillMaxHeight(1f),
            navController = navController,
            companyName = companyName
        )
    }
}

