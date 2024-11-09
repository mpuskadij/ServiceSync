package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun CompanyNameAndImage(companyName: String, imageID: Int)
{
    Box(modifier= Modifier.fillMaxWidth().height(175.dp), contentAlignment = Alignment.BottomStart)
    {
        Image(
            imageVector =  ImageVector.vectorResource(imageID),
            "Company image",
            modifier  = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            //TODO remove this filter when company images are implemented
            colorFilter = ColorFilter.tint(Color.Black)
        )
        Text(modifier = Modifier.padding(10.dp), text = companyName, color = MaterialTheme.colorScheme.inverseOnSurface, style = MaterialTheme.typography.displayMedium)
    }
}