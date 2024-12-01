package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CompanyImage(companyName: String, imageUrl: String?, onBackPressed: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Box(modifier = Modifier.matchParentSize()) {
            if (imageUrl != null)
            {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Company Image",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            }
            else
            {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Gray)
                )
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )
        }
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Text(
            modifier = Modifier.padding(10.dp),
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