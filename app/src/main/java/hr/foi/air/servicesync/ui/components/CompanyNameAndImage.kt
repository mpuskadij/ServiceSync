package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import hr.foi.air.servicesync.R

@Composable
fun CompanyNameAndImage(companyName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Box(modifier = Modifier.matchParentSize()) {
            Image(
                painter = painterResource(id = R.drawable.businessorganisation),
                contentDescription = "Company image",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )
        }
        Text(
            text = companyName,
            color = Color.White,
            style = TextStyle(
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                letterSpacing = MaterialTheme.typography.displayMedium.letterSpacing,
                lineHeight = MaterialTheme.typography.displayMedium.lineHeight
            )
        )
    }
}