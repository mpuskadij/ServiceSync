package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.surfaceContainerDark
import com.example.compose.surfaceContainerLight

@Composable
fun FloatingCard(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 6.dp),
    containerColor: Color = isDark(surfaceContainerDark, surfaceContainerLight),
    shape: Shape = MaterialTheme.shapes.small,
    elevation: Dp = 4.dp,
    innerPadding: PaddingValues = PaddingValues(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            content()
        }
    }
}