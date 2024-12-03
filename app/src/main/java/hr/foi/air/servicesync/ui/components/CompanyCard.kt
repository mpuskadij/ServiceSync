package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight

@Composable
fun CompanyCard(
    companyName: String,
    companyCategory: String,
    imageUrl: String?,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clipToBounds()
            .padding(top = 16.dp, bottom = 0.dp, start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
        onClick = onCardClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            imageUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "$companyName background",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer
                    {
                        alpha = 0.8f
                    }
                    .background(Color.Black.copy(alpha = 0.6f))
            )
        }
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
            .clipToBounds()
            .height(90.dp),
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 12.dp, bottomEnd = 12.dp),
        onClick = onCardClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(90.dp)
                .padding(15.dp)
                .background(Color.Transparent)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = companyName,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start,
                    color = isDark(onSurfaceDark, onSurfaceLight),
                )
                Text(
                    text = companyCategory,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    color = isDark(onSurfaceDark, onSurfaceLight),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
